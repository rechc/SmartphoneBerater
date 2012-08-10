package de.htw.berater;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.Restriction;
import com.hp.hpl.jena.rdf.model.Literal;
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

public class Berater1 extends Berater {

	public Berater1(String rdfPath, String ns) {
		super(rdfPath, ns);
	}

	@Override
	public String evaluateAndAskNewQuestion(String string) {
		switch (context) {
		case 1:
			return zweck(string);
		case 2:
			context = 3;
			nextAnswer = Answer.YESNO;
			return "Eh paar uffs Maul?";
		}
		throw new RuntimeException("wrong context");
	}

	@Override
	public String evaluateAndAskNewQuestion(boolean yes) {
		switch (context) {
		}
		throw new RuntimeException("wrong context");
	}

	private String zweck(String zweck) {
		OntClass zweckClass = model.getOntClass(ns + "Zweck");
		OntClass zweckSubClass = null;
		for (Iterator<OntClass> i = zweckClass.listSubClasses(); i.hasNext();){
		        OntClass subClass = (OntClass) i.next();
		        if (subClass.getLocalName().toLowerCase().contains(zweck)) {
		        	zweckSubClass = subClass;
		        	System.out.println(subClass.getLocalName());
		        }
		}
		
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
		List<OntClass> spielesmartphones = new ArrayList<OntClass>();
		while (ri.hasNext()) {
			OntClass subClass = ri.next();
			for (Iterator<OntClass> supers = subClass.listSuperClasses(true); supers.hasNext(); ) {
	            OntClass superClass = supers.next();
	            if (superClass.isRestriction()) {
	            	Restriction restriction = superClass.asRestriction();
	                if (restriction.getOnProperty().getLocalName().contains("hatZweck")) {
	                	//some
	                	if (restriction.isSomeValuesFromRestriction()) {
	                		OntClass range = restriction.asSomeValuesFromRestriction().getSomeValuesFrom().as(OntClass.class);
	                		if (range.equals(zweckSubClass)) {
	                			spielesmartphones.add(subClass);
	                		}
	                	}
	                }
	            } 
			}
		}
		for (int i = 0; i < spielesmartphones.size(); i++) {
			if (isCoveringAxiom(spielesmartphones.get(i))) {
				OntClass abstractClass = spielesmartphones.get(i);
				spielesmartphones.remove(i);
				ri = abstractClass.listSubClasses();
				while (ri.hasNext()) {
					spielesmartphones.add(ri.next());
				}
			}
		}
	
		List<SQLConstraint> sqlConstraints = new LinkedList<SQLConstraint>();
		for (int i = 0; i < spielesmartphones.size(); i++) {
			SQLConstraint sqlConstraint = new SQLConstraint();
			for (Iterator<OntClass> supers = spielesmartphones.get(i).listSuperClasses(true); supers.hasNext(); ) {
	            OntClass superClass = supers.next();
	            if (superClass.isRestriction()) {
	            	Restriction restriction = superClass.asRestriction();
	            	if (restriction.isSomeValuesFromRestriction()) {
	            		Resource res = restriction.asSomeValuesFromRestriction().getSomeValuesFrom();
	            		
	            		sqlConstraint.setKey(restriction.getOnProperty().getLocalName());
	            		if (res.hasProperty( RDF.type, RDFS.Datatype )) {
                            Property owlWithRestrictions = ResourceFactory.createProperty( OWL.getURI(), "withRestrictions" );
                            Property minInclusive = ResourceFactory.createProperty( XSD.getURI(), "minInclusive" );
                            Property minExclusive = ResourceFactory.createProperty( XSD.getURI(), "minExclusive" );

                            // the datatype restrictions are represented as a list
                            // we make some assumptions about the content of the list; this code
                            // could be more defensive about testing for expected values
                            Resource wr = res.getProperty( owlWithRestrictions ).getResource();
                            RDFList wrl = wr.as( RDFList.class );

                            for (Iterator<RDFNode> k = wrl.iterator(); k.hasNext(); ) {
                                Resource wrClause = (Resource) k.next();
                                Statement stmt = wrClause.getProperty( minInclusive );
                                if (stmt == null) {
                                	stmt = wrClause.getProperty(minExclusive);
                                }
                                Literal literal = stmt.getLiteral();
        	            		sqlConstraint.setValue(literal.getInt() + "");
                            }
                        } else {
                        	sqlConstraint.setValue(res.getLocalName() + "");
                        }
	            	} 
	            }
			}
			sqlConstraints.add(sqlConstraint);
		}
		currentSQLConstraints.addAll(sqlConstraints);
		String s = "Moechten sie ";
		for (OntClass smphone : spielesmartphones) {
			s += smphone.toString() + " "; 
		}
		s+= "?";
		s = s.replace(" ?", "?");
		context = 2;
		nextAnswer = Answer.KEYWORD;
		return s;
	}

	

	@Override
	public String askFirstQuestionZweck() {
		context = 1;
		nextAnswer = Answer.KEYWORD;
		return "Fuer welchen Zweck benoetigen Sie ein Smartphone?";
	}

	@Override
	public String askFirstQuestionGeneral() {
		throw new UnsupportedOperationException();
	}

}
