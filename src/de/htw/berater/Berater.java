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

public abstract class Berater {
	
	protected String rdfPath;
	protected String ns;
	protected Set<OntClass> properties = new LinkedHashSet<OntClass>();
	protected int context; //irgendwie den kontext beachten um sinnvoll die naechste frage zu stellen
	protected Answer nextAnswer;
	protected Customer customer = new Customer();
	protected OntModel  model;
	
	public Berater(String rdfPath, String ns) {
		this.rdfPath = rdfPath;
		this.ns = ns;
	
		if ( System.getProperty("log4j.configuration") == null )	{
			System.setProperty("log4j.configuration", "jena-log4j.properties") ;    		
		}
            	   
		model = ModelFactory.createOntologyModel();
		
		model.read("file:" + rdfPath);
	}
	
	public void addCustomerInfo(int info) {
		customer.addCustomerInfo(info);
	}
	
	public abstract String evaluateAndAskNewQuestion(String string);

	public abstract String evaluateAndAskNewQuestion(boolean yes);

	public final void reset() {
	
	}

	public final Set<OntClass> getProperties() {
		return properties;
	}
	
	public final boolean expectsYesNoAnswer() {
		return nextAnswer == Answer.YESNO;
	}
	
	protected final List<Restriction> getRestrictions(OntClass ontClass) {
		List<Restriction> restrictions = new LinkedList<Restriction>();
		for (Iterator<OntClass> supers = ontClass.listSuperClasses(true); supers.hasNext(); ) {
		            OntClass superClass = supers.next();
		            if (superClass.isRestriction()) {
		             Restriction restriction = superClass.asRestriction();
		                restrictions.add(restriction);
		            }
		}
		return restrictions;
	}
	
	/*is covering axiom(abstract class)?*/
	protected boolean isCoveringAxiom(OntClass ontClass) {
		boolean isCoveringAxiom = false;
		for (Iterator<OntClass> supers = ontClass.listSuperClasses(true); supers.hasNext(); ) {
			OntClass superClass = supers.next();
			if (superClass.isUnionClass()) {
				isCoveringAxiom = true;
				for (Iterator<?> it = superClass.asUnionClass().listOperands(); it.hasNext(); ) {
					OntClass op = (OntClass) it.next();

					if (!op.isRestriction()) {
						boolean found = false;
						ExtendedIterator<OntClass> it2 = ontClass.listSubClasses();
						while (it2.hasNext()) {
							if (it2.next().equals(op)) {
								found = true;
							}
						}
						if (!found) {
							isCoveringAxiom = false;
						}
					}
				}
				if (isCoveringAxiom) {
					return true;
				}
			}
		}
		return false;
	}

	public final boolean expectsKeywordAnswer() {
		return nextAnswer == Answer.KEYWORD;
	}

	/*real super classes, no properties*/
	protected final List<OntClass> getSuperclasses(OntClass ontClass) {
		List<OntClass> superClasses = new LinkedList<OntClass>();
		for (Iterator<OntClass> supers = ontClass.listSuperClasses(true); supers.hasNext(); ) {
			OntClass superClass = supers.next();
			if (!superClass.isAnon()) {
				superClasses.add(superClass);
			}
		}
		return superClasses;
	}
	
	/*get properties of a certain class*/
	protected final List<OntClass> getProperties(OntClass ontClass) {
		List<OntClass> tmpProperties = new LinkedList<OntClass>();
		for (Iterator<OntClass> supers = ontClass.listSuperClasses(true); supers.hasNext(); ) {
            OntClass superClass = supers.next();
            if (superClass.isRestriction() ||
            		superClass.isUnionClass() ||
            		superClass.isIntersectionClass() ||
            		superClass.isComplementClass()) {
                tmpProperties.add(superClass);
            } 
		}
		return tmpProperties;
	}
	
	/*add all properties of a cerain class*/
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
	

	/*remove all properties of a certain class*/
	protected final void removeSQLConstraints(OntClass ontClass) {
		List<OntClass> tmpProperties = getProperties(ontClass);
		properties.removeAll(tmpProperties);
	}
	
	
	public String getSQLString() {
		String s = "select * from TABLE where ";
		for (OntClass property : properties) {
			s += "(" + processPropertyToSQL(property) + ")" + " and ";
		}
		return s.substring(0, s.length() - 5);
	}
	
	private String processPropertyToSQL(OntClass property) {
		if (property.isRestriction()) {
			SQLConstraint constraint = getSQLConstraintFromRestriction(property.asRestriction());
			if (constraint.getKey().equals("hatEigenschaft")) {
				return constraint.getValue() + " = 1 ";
			} else {
				if (constraint.getKey().equals("hatZweck")) {
					return "1";
				} else if (constraint.getKey().equals("fuerKunde")) {
					return "1";
				} else {
					return constraint.getKey() + " " + constraint.getValue();
				}
			}
		} else {
			if (property.isIntersectionClass()) {
				String s = "";
				for (Iterator<?> it = property.asIntersectionClass().listOperands(); it.hasNext(); ) {
					OntClass op = (OntClass) it.next();
					s += processPropertyToSQL(op) + " and ";
				} 
				s = s.substring(0, s.length() - 5);
				return "(" + s + ")";
			} else {
				if (property.isUnionClass()) {
					String s = "";
					for (Iterator<?> it = property.asUnionClass().listOperands(); it.hasNext(); ) {
						OntClass op = (OntClass) it.next();
						s += processPropertyToSQL(op) + " or ";
					} 
					s = s.substring(0, s.length() - 4);
					return "(" + s + ")";
				} else {
					if (property.isComplementClass()) {
						String s = "";
						for (Iterator<?> it = property.asComplementClass().listOperands(); it.hasNext(); ) {
							OntClass op = (OntClass) it.next();
							s = "not(" + processPropertyToSQL(op) + ")";
						} 
						return "(" + s + ")";
					}
				}
			}
		}
		return "1";
	}

	protected void setCustomerInfo() {
		for (OntClass property : properties) {
			if (property.isRestriction()) {
				SQLConstraint sqlConstraint = getSQLConstraintFromRestriction(property.asRestriction());
				if (sqlConstraint.getKey().equals("fuerKunde")) {
					if (sqlConstraint.getValue().toLowerCase().equals("sudokuhengst")) {
						this.customer.addCustomerInfo(Customer.SUDOKUHENGST);
					} else if (sqlConstraint.getValue().toLowerCase().equals("spielefreak")) {
						this.customer.addCustomerInfo(Customer.SPIELEFREAK);
					} 
				}
			}
		}
	}

	private SQLConstraint getSQLConstraintFromRestriction(Restriction restriction) {
		Resource res = null;
		if (restriction.isSomeValuesFromRestriction()) {
			res = restriction.asSomeValuesFromRestriction().getSomeValuesFrom();
		}
		
		SQLConstraint sqlConstraint = new SQLConstraint();
		sqlConstraint.setKey(restriction.getOnProperty().getLocalName());
		if (res.hasProperty( RDF.type, RDFS.Datatype )) {
			Property owlWithRestrictions = ResourceFactory.createProperty( OWL.getURI(), "withRestrictions" );
			Property minInclusive = ResourceFactory.createProperty( XSD.getURI(), "minInclusive" ); // >= x
			Property minExclusive = ResourceFactory.createProperty( XSD.getURI(), "minExclusive" ); // > x
			Property maxInclusive = ResourceFactory.createProperty( XSD.getURI(), "maxInclusive" ); // <= x
			Property maxExclusive = ResourceFactory.createProperty( XSD.getURI(), "maxExclusive" ); // < x
			
			// the datatype restrictions are represented as a list
			// we make some assumptions about the content of the list; this code
			// could be more defensive about testing for expected values
			Resource wr = res.getProperty( owlWithRestrictions ).getResource();
			RDFList wrl = wr.as( RDFList.class );
			
			for (Iterator<RDFNode> k = wrl.iterator(); k.hasNext(); ) {
				Resource wrClause = (Resource) k.next();
				Statement stmt = wrClause.getProperty( minInclusive );
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
				sqlConstraint.setValue(str + literal.getInt());
			}
		} else {
			if (res.canAs(UnionClass.class)) {
				String unionStr = "";
				UnionClass union = res.as(UnionClass.class);
				for (Iterator<?> it = union.listOperands(); it.hasNext(); ) {
					OntClass op = (OntClass) it.next();
					unionStr += op.getLocalName() + (it.hasNext() ? " = 1 or " : "");
				}
				sqlConstraint.setValue(unionStr);
			} else {
				sqlConstraint.setValue(res.getLocalName());
			}
		}
		return sqlConstraint;
	}
	

	protected List<OntClass> getCoveringAxiomClasses(List<OntClass> classes) {
		List<OntClass> coveringAxiomClasses = new LinkedList<OntClass>();
		for (int i = 0; i < classes.size(); i++) {
			if (isCoveringAxiom(classes.get(i))) {
				OntClass abstractClass = classes.get(i);
				ExtendedIterator<OntClass> ri = abstractClass.listSubClasses();
				while (ri.hasNext()) {
					coveringAxiomClasses.add(ri.next());
				}
				coveringAxiomClasses.addAll(getCoveringAxiomClasses(coveringAxiomClasses));
			}
		}
		return coveringAxiomClasses;
	}
	
	/* noch mehr allgemeine Methoden ??*/
	public abstract String askFirstQuestionZweck(); //Szenario1
	
	public abstract String askFirstQuestionGeneral(); //Szenario2
}
