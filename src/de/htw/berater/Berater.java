package de.htw.berater;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.ontology.UnionClass;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

import de.htw.berater.controller.Answer;
import de.htw.berater.controller.Question;
import de.htw.berater.db.DBException;
import de.htw.berater.db.SQLClient;

public abstract class Berater {

	public static final int BERATER_1 = 0;
	public static final int BERATER_2 = 1;

	protected List<OntClass> rememberList = new LinkedList<OntClass>();
	protected Question nextQuestion;
	protected final String ns;
	protected final Set<OntClass> properties = new LinkedHashSet<OntClass>();
	protected int context; // irgendwie den kontext beachten um sinnvoll die
							// naechste frage zu stellen
	protected Customer customer = new Customer();
	protected final OntModel model;

	public Berater(String rdfPath, String ns) {
		this.ns = ns;

		if (System.getProperty("log4j.configuration") == null) {
			System.setProperty("log4j.configuration", "jena-log4j.properties");
		}

		model = ModelFactory.createOntologyModel();

		model.read("file:" + rdfPath);
	}

	public void addCustomerInfo(int info) {
		customer.addCustomerInfo(info);
	}

	public abstract void evaluateAnswer(Answer answer) throws Exception;

	public abstract Question firstQuestion();
	
	public final Question generateQuestion() {
		switch (context) {
		case 0:
			return firstQuestion();
		default:
			return nextQuestion;
		}
	}

	public final void reset() {
		context = 0;
		properties.clear();
		customer = new Customer();
	}

	public final Set<OntClass> getProperties() {
		return properties;
	}

	protected final List<Restriction> getRestrictionsDeep(OntClass ontClass) {
		List<Restriction> restrictions = new LinkedList<Restriction>();
		for (Iterator<OntClass> supers = ontClass.listSuperClasses(true); supers
				.hasNext();) {
			OntClass superClass = supers.next();
			restrictions.addAll(getRestrictionsRecursively(superClass));
		}
		return restrictions;
	}
	
	protected boolean isSmartphoneOkForCustumer(OntClass displaySmartphone, int sehbehindert) throws DBException {
		List<OntClass> restrictions = getRestrictionsFlat(displaySmartphone);
		for (OntClass restriction : restrictions) {
			if (customer.isCustomer(sehbehindert)) {
				if (!testSmartphoneOkForCustomerRecursively(restriction, true, "Sehbehindert")) {
					return false;
				}
			}
		}
		return true;
	}
	
	protected boolean testSmartphoneOkForCustomerRecursively(OntClass clazz, boolean isOk, String what) throws DBException {
		if (clazz.isRestriction()) {
			Restriction restriction = clazz.asRestriction();
			ReadableProperty constraint = getReadablePropertyFromRestriction(restriction);
			if (constraint.getKey().equals("fuerKunde")) {
				if (!constraint.getValue().equals(what)) {
					isOk = true;
				}
			}
		} else if (clazz.isIntersectionClass()) { 
			for (Iterator<? extends OntClass> it = clazz.asIntersectionClass()
					.listOperands(); it.hasNext();) {
				OntClass op = it.next();
				isOk = testSmartphoneOkForCustomerRecursively(op, isOk, what);
				if (!isOk) {
					break;
				}
			}
		} else if (clazz.isComplementClass()) {
			for (Iterator<? extends OntClass> it = clazz.asComplementClass()
					.listOperands(); it.hasNext();) {
				OntClass op = it.next();
				isOk = testSmartphoneOkForCustomerRecursively(op, !isOk, what);
			}
		}
		return isOk;
	}
	
	protected final List<OntClass> getRestrictionsFlat(OntClass ontClass) {
		List<OntClass> restrictions = new LinkedList<OntClass>();
		for (Iterator<OntClass> supers = ontClass.listSuperClasses(true); supers
				.hasNext();) {
			OntClass superClass = supers.next();
			if (superClass.isAnon())
				restrictions.add(superClass);
		}
		return restrictions;
	}
	
	protected List<Restriction> getRestrictionsRecursively(OntClass clazz) {
		List<Restriction> restrictions = new LinkedList<Restriction>();
		if (clazz.isRestriction()) {
			Restriction restriction = clazz.asRestriction();
			restrictions.add(restriction);
		} else if (clazz.isIntersectionClass()) { 
			for (Iterator<? extends OntClass> it = clazz.asIntersectionClass()
					.listOperands(); it.hasNext();) {
				OntClass op = it.next();
				restrictions.addAll(getRestrictionsRecursively(op));
			}
		} else if (clazz.isComplementClass()) {
			for (Iterator<? extends OntClass> it = clazz.asComplementClass()
					.listOperands(); it.hasNext();) {
				OntClass op = it.next();
				restrictions.addAll(getRestrictionsRecursively(op));
			}
		} else if (clazz.isUnionClass()) {
			for (Iterator<? extends OntClass> it = clazz.asUnionClass()
					.listOperands(); it.hasNext();) {
				OntClass op = it.next();
				restrictions.addAll(getRestrictionsRecursively(op));
			}
		}
		return restrictions;
	}

	/* is covering axiom(abstract class)? */
	protected boolean isCoveringAxiom(OntClass ontClass) {
		boolean isCoveringAxiom = false;
		for (Iterator<OntClass> supers = ontClass.listSuperClasses(true); supers.hasNext();) {
			OntClass superClass = supers.next();
			if (superClass.isUnionClass()) {
				isCoveringAxiom = true;
				for (Iterator<? extends OntClass> it = superClass.asUnionClass().listOperands(); it.hasNext();) {
					OntClass op = it.next();

					if (!op.isRestriction()) {
						boolean found = false;
						Iterator<OntClass> it2 = ontClass.listSubClasses();
						while (it2.hasNext()) {
							if (it2.next().equals(op)) {
								found = true;
							}
						}
						if (!found) {
							isCoveringAxiom = false;
						}
					} else {
						return false;
					}
				}
				if (isCoveringAxiom) {
					return true;
				}
			}
		}
		return false;
	}

	/* real super classes, no properties */
	protected final List<OntClass> getSuperclasses(OntClass ontClass) {
		List<OntClass> superClasses = new LinkedList<OntClass>();
		for (Iterator<OntClass> supers = ontClass.listSuperClasses(true); supers
				.hasNext();) {
			OntClass superClass = supers.next();
			if (!superClass.isAnon()) {
				superClasses.add(superClass);
			}
		}
		return superClasses;
	}

	/* get properties of a certain class */
	protected final List<OntClass> getProperties(OntClass ontClass) {
		List<OntClass> tmpProperties = new LinkedList<OntClass>();
		for (Iterator<OntClass> supers = ontClass.listSuperClasses(true); supers
				.hasNext();) {
			OntClass superClass = supers.next();
			if (superClass.isRestriction() || superClass.isUnionClass()
					|| superClass.isIntersectionClass()
					|| superClass.isComplementClass()
					&& !superClass.hasSubClass()) { //nur blatt-eigenschaften
				tmpProperties.add(superClass);
			}
		}
		return tmpProperties;
	}

	/* add all properties of a certain class */
	protected final void setCurrentProperties(OntClass ontClass) {
		if (ontClass.getLocalName().equals("Smartphone")) {
			return;
		} else {
			List<OntClass> superClasses = getSuperclasses(ontClass);
			for (OntClass superClass : superClasses) {
				setCurrentProperties(superClass);
			}
		}
		List<OntClass> tmpProperties = getProperties(ontClass);
		properties.addAll(tmpProperties);
	}

	/* remove all properties of a certain class */
	protected final void removeSQLConstraints(OntClass ontClass) {
		List<OntClass> tmpProperties = getProperties(ontClass);
		properties.removeAll(tmpProperties);
	}

	public String getSQLString() throws DBException {
		String s = "select * from Smartphones where ";
		for (OntClass property : properties) {
			String expression = processPropertyToSQL(property);
			if (!expression.equals("1") && !expression.equals("()")) {
				s += "(" + expression + ")" + " and ";
			}
		}
		if (s.equals("select * from Smartphones where ")) {
			return "select * from Smartphones";
		}
		return s.substring(0, s.length() - 5); //wegen " and "
	}
	
	private String extractActualIdentifier(String propertyName) {
		if (!propertyName.matches("^[A-Z].*")) throw new RuntimeException("Die Properties müssen immer mit einem grossbuchstaben beginnen");
		boolean lowerCaseFound = false;
		for (int i = 0; i < propertyName.length(); i++) {
			if (Character.isLowerCase(propertyName.charAt(i))) {
				lowerCaseFound = true;
			}
			if (lowerCaseFound) {
				if (Character.isUpperCase(propertyName.charAt(i))) {
					return propertyName.substring(0, i);
				}
			}
		}
		return propertyName;
	}
	
	private String extractGeneralIdentifier(String propertyName) {
		boolean lowerCaseFound = false;
		for (int i = 0; i < propertyName.length(); i++) {
			if (Character.isLowerCase(propertyName.charAt(i))) {
				lowerCaseFound = true;
			}
			if (lowerCaseFound) {
				if (Character.isUpperCase(propertyName.charAt(i))) {
					return propertyName.substring(i);
				}
			}
		}
		return null;
	}

	private String processPropertyToSQL(OntClass property) throws DBException {
		if (property.isRestriction()) {
			ReadableProperty constraint = getReadablePropertyFromRestriction(property
					.asRestriction());
			if (constraint.getKey().equals("hatEigenschaft")) {
				if (constraint.isBooleanValue()) {
					return constraint.getValue() + " = 1 ";
				} else {
					return constraint.getValue();
				}
			} else {
				if (constraint.getKey().equals("hatZweck")) { //braucht man nicht
					return "1";
				} else if (constraint.getKey().equals("fuerKunde")) { //braucht man nicht
					return "1";
				} else {
					return constraint.getKey() + " " + constraint.getValue();
				}
			}
		} else {
			if (property.isIntersectionClass()) {
				String s = "";
				for (Iterator<? extends OntClass> it = property.asIntersectionClass()
						.listOperands(); it.hasNext();) {
					OntClass op = it.next();
					String expression = processPropertyToSQL(op);
					if (!expression.equals("1") && !expression.equals("()")) {
						s += expression + " and ";
					}
				}
				if (s.length() > 0)
					s = s.substring(0, s.length() - 5);
				return "(" + s + ")";
			} else {
				if (property.isUnionClass()) {
					String s = "";
					for (Iterator<? extends OntClass> it = property.asUnionClass()
							.listOperands(); it.hasNext();) {
						OntClass op = it.next();
						String expression = processPropertyToSQL(op);
						if (!expression.equals("1") && !expression.equals("()")) {
							s += expression + " or ";
						}
					}
					if (s.length() > 0)
						s = s.substring(0, s.length() - 4);
					return "(" + s + ")";
				} else {
					if (property.isComplementClass()) {
						String s = "";
						for (Iterator<? extends OntClass> it = property.asComplementClass()
								.listOperands(); it.hasNext();) {
							OntClass op = it.next();
							String expression = processPropertyToSQL(op);
							if (!expression.equals("1") && !expression.equals("()")) {
								s = "not(" + expression + ")";
							}
							
						}
						return "(" + s + ")";
					}
				}
			}
		}
		return "1";
	}

	protected void setCustomerInfo() throws DBException {
		for (OntClass property : properties) {
			if (property.isRestriction()) {
				ReadableProperty sqlConstraint = getReadablePropertyFromRestriction(property
						.asRestriction());
				if (sqlConstraint.getKey().equals("fuerKunde")) {
					if (sqlConstraint.getValue().toLowerCase()
							.equals("sudokuhengst")) {
						this.customer.addCustomerInfo(Customer.SUDOKUHENGST);
					} else if (sqlConstraint.getValue().toLowerCase()
							.equals("spielefreak")) {
						this.customer.addCustomerInfo(Customer.SPIELEFREAK);
					}
				}
			}
		}
	}

	protected ReadableProperty getReadablePropertyFromRestriction(
			Restriction restriction) throws DBException {
		Resource res = null;
		if (restriction.isSomeValuesFromRestriction()) {
			res = restriction.asSomeValuesFromRestriction().getSomeValuesFrom();
		} else {
			throw new RuntimeException("nur \"some\" properties sind vorgesehen, Ontologie neu?");
		}

		ReadableProperty sqlConstraint = new ReadableProperty();
		sqlConstraint.setKey(restriction.getOnProperty().getLocalName());
		if (res.hasProperty(RDF.type, RDFS.Datatype)) {
			Property owlWithRestrictions = ResourceFactory.createProperty(
					OWL.getURI(), "withRestrictions");
			Property minInclusive = ResourceFactory.createProperty(
					XSD.getURI(), "minInclusive"); // >= x
			Property minExclusive = ResourceFactory.createProperty(
					XSD.getURI(), "minExclusive"); // > x
			Property maxInclusive = ResourceFactory.createProperty(
					XSD.getURI(), "maxInclusive"); // <= x
			Property maxExclusive = ResourceFactory.createProperty(
					XSD.getURI(), "maxExclusive"); // < x

			// the datatype restrictions are represented as a list
			// we make some assumptions about the content of the list; this code
			// could be more defensive about testing for expected values
			Resource wr = res.getProperty(owlWithRestrictions).getResource();
			RDFList wrl = wr.as(RDFList.class);

			for (Iterator<RDFNode> k = wrl.iterator(); k.hasNext();) {
				Resource wrClause = (Resource) k.next();
				Statement stmt = wrClause.getProperty(minInclusive);
				String str = ">=";
				if (stmt == null) {
					stmt = wrClause.getProperty(minExclusive);
					str = ">";
				}
				if (stmt == null) {
					stmt = wrClause.getProperty(maxExclusive);
					str = "<";
				}
				if (stmt == null) {
					stmt = wrClause.getProperty(maxInclusive);
					str = "<=";
				}
				Literal literal = stmt.getLiteral();
				sqlConstraint.setValue(str + literal.getInt(), true);
			}
		} else {
			if (res.canAs(UnionClass.class)) {
				String unionStr = "";
				UnionClass union = res.as(UnionClass.class);
				boolean isBooleanValue = false;
				for (Iterator<? extends OntClass> it = union.listOperands(); it.hasNext();) {
					OntClass op = it.next();
					isBooleanValue = SQLClient.getInstance().doesColumnExist(op.getLocalName());
					if (isBooleanValue)
						unionStr += op.getLocalName() + " = 1 " + (it.hasNext() ? " or " : "");
					else
						unionStr += extractGeneralIdentifier(op.getLocalName()) + " like '" + extractActualIdentifier(op.getLocalName()) + "%' " + (it.hasNext() ? " or " : "");
				}
				sqlConstraint.setValue(unionStr, isBooleanValue);
			} else {
				boolean isBooleanValue = SQLClient.getInstance().doesColumnExist(res.getLocalName());
				String s = res.getLocalName();
				if (!isBooleanValue) {
					String general = extractGeneralIdentifier(s);
					
					if (general == null) {
						isBooleanValue = true;
					} else {
						s = general + " like '" + extractActualIdentifier(s) + "'";
					}
				} 
				sqlConstraint.setValue(s, isBooleanValue);
			}
		}
		return sqlConstraint;
	}

	protected List<List<OntClass>> getCoveringAxiomClasses(List<OntClass> classes) {
		List<List<OntClass>> result = new LinkedList<List<OntClass>>();
		List<OntClass> concrereClasses = new LinkedList<OntClass>();
		for (OntClass clazz : classes) {
			if (isCoveringAxiom(clazz)) {
				ExtendedIterator<OntClass> ri = clazz.listSubClasses();
				List<OntClass> subClasses = new LinkedList<OntClass>();
				while (ri.hasNext()) {
					OntClass subClass = ri.next();
					subClasses.add(subClass);
				}
				result.addAll(getCoveringAxiomClasses(subClasses));
			} else {
				concrereClasses.add(clazz);
			}
		}
		result.add(concrereClasses);
		return result;
	}

	protected OntClass searchClassContaining(String keyword, String type) {
		keyword = keyword.toLowerCase();
		OntClass smartphone = model.getOntClass(ns + type);
		for (Iterator<OntClass> it = smartphone.listSubClasses(); it.hasNext();) {
			OntClass subClass = it.next();
			if (subClass.getLocalName().toLowerCase().contains(keyword)) {
				return subClass;
			}
		}
		return null;
	}

}
