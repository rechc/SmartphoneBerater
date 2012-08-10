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
import de.htw.berater.db.SQLConstraint;

public abstract class Berater {
	
	protected String rdfPath;
	protected String ns;
	protected Set<SQLConstraint> currentSQLConstraints = new LinkedHashSet<SQLConstraint>();
	protected int context; //irgendwie den kontext beachten um sinnvoll die naechste frage zu stellen
	protected Answer nextAnswer;
	protected Customer customer;
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
	
	public abstract String evaluateAndAskNewQuestion(String string);

	public abstract String evaluateAndAskNewQuestion(boolean yes);

	public final void reset() {
	
	}

	public final Set<SQLConstraint> getCurrentSQLConstraintsList() {
		return currentSQLConstraints;
	}

	public final boolean expectsYesNoAnswer() {
		return nextAnswer == Answer.YESNO;
	}
	
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
	
	/*hatEigenschaft some AppstoreEigenscahft*/
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
	
	/*(hatZweck some BilderMachenZweck) or (hatZweck some SpieleZweck) or (hatSpeicherplatz some integer[> 1000])*/
	protected final List<Restriction> getRestrictionsFromUnion(OntClass ontClass) {
		List<Restriction> restrictions = new LinkedList<Restriction>();
		for (Iterator<OntClass> supers = ontClass.listSuperClasses(true); supers.hasNext(); ) {
            OntClass superClass = supers.next();
            if (superClass.isUnionClass()) {
            	for (Iterator<?> it = superClass.asUnionClass().listOperands(); it.hasNext(); ) {
					OntClass op = (OntClass) it.next();
					if (op.isRestriction()) {
						restrictions.add(op.asRestriction());
					}
				}
            } 
		}
		return restrictions;
	}
	
	protected final List<SQLConstraint> getSQLConstraints(OntClass ontClass) {
		List<SQLConstraint> sqlConstraints = new LinkedList<SQLConstraint>();
		if (ontClass.getLocalName().equals("Smartphone")) {
			return new LinkedList<SQLConstraint>();
		} else {
			List<OntClass> superClasses = getSuperclasses(ontClass);
			for (OntClass superClass : superClasses) {
				sqlConstraints.addAll(getSQLConstraints(superClass));
			}
		}
		List<Restriction> restrictions = getRestrictions(ontClass);
		restrictions.addAll(getRestrictionsFromUnion(ontClass));
		for (Restriction restriction : restrictions) {
			Resource res = null;
        	if (restriction.isSomeValuesFromRestriction()) {
        		res = restriction.asSomeValuesFromRestriction().getSomeValuesFrom();
        	}
        	if (restriction.isComplementClass()) {
        		continue;
        	}
        	SQLConstraint sqlConstraint = new SQLConstraint();
    		sqlConstraint.setKey(restriction.getOnProperty().getLocalName());
    		if (res.hasProperty( RDF.type, RDFS.Datatype )) {
                Property owlWithRestrictions = ResourceFactory.createProperty( OWL.getURI(), "withRestrictions" );
                Property minInclusive = ResourceFactory.createProperty( XSD.getURI(), "minInclusive" ); // >= x
                Property minExclusive = ResourceFactory.createProperty( XSD.getURI(), "minExclusive" ); // > x

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
                    Literal literal = stmt.getLiteral();
            		sqlConstraint.setValue(str + literal.getInt());
                }
            } else {
            	if (res.canAs(UnionClass.class)) {
            		String unionStr = "";
            		UnionClass union = res.as(UnionClass.class);
            		for (Iterator<?> it = union.listOperands(); it.hasNext(); ) {
    					OntClass op = (OntClass) it.next();
    					unionStr += op.getLocalName() + (it.hasNext() ? " or " : "");
    				}
            		sqlConstraint.setValue(unionStr);
            	} else {
            		sqlConstraint.setValue(res.getLocalName());
            	}
            }
    		sqlConstraints.add(sqlConstraint);
        }
		return sqlConstraints;
	}
	
	
	/* noch mehr allgemeine Methoden ??*/
	public abstract String askFirstQuestionZweck(); //Szenario1
	
	public abstract String askFirstQuestionGeneral(); //Szenario2
}
