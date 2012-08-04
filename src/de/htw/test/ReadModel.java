package de.htw.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.Restriction;
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

public class ReadModel {

	public static void main(String[] args) {
		String ns = "http://semantische-interoperabilitaet-projekt#";
		//String ontopath = "./smartphones.owl";
		String ontopath = "inferredSmartphones.rdf";
	
		if ( System.getProperty("log4j.configuration") == null )	{
			System.setProperty("log4j.configuration", "jena-log4j.properties") ;    		
		}
            	   
		OntModel model = ModelFactory.createOntologyModel();
		
		//Jena does not support OWL/XML format
		//RDF/XML format ist needed
		model.read("file:" + ontopath);
		
		
		System.out.println("1.Es wird eine Sub-Klasse in der Klasse \"Zweck\" gesucht, die den Substring \"Spiele\" enthält. Das sollte dann \"SpieleZweck\" sein. ");
		
		
		OntClass zweck = model.getOntClass(ns + "Zweck");
		OntClass spieleZweck = null;
		for (Iterator<OntClass> i = zweck.listSubClasses(); i.hasNext();){
		        OntClass subClass = (OntClass) i.next();
		        if (subClass.getLocalName().toLowerCase().contains("spiele")) {
		        	spieleZweck = subClass;
		        	System.out.println(subClass.getLocalName());
		        }
		}
		
		System.out.println("2.Es wird in allen Smartphone Klassen, Klassen gesucht, die die Property \"hatZweck\" besitzen. ");
		
		OntClass smartphone = model.getOntClass(ns + "Smartphone");
		
		ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
		while (ri.hasNext()) {
			OntClass subClass = ri.next();
			for (Iterator<OntClass> supers = subClass.listSuperClasses(true); supers.hasNext(); ) {
	            OntClass superClass = supers.next();
	            if (superClass.isRestriction()) {
	            	Restriction restriction = superClass.asRestriction();
	                if (restriction.getOnProperty().getLocalName().contains("hatZweck")) {
	                	System.out.println(subClass.getLocalName());
	                	break;
	                }
	            }
	        }
		}
		
		System.out.println("3. Alle Klassen werden rausgefiltert, wo die Property \"hatZweck\" auf \"SpieleZweck\" zeigt. \"SpieleSmartphone\" sollte das einzige sein in unserem Fall. ");
		
		List<OntClass> spielesmartphones = new ArrayList<OntClass>();
		ri = smartphone.listSubClasses();
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
	                		if (range.equals(spieleZweck)) {
	                			spielesmartphones.add(subClass);
	                			System.out.println(subClass.getLocalName());
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
		
		
		System.out.println("4. Alle Properties der Klasse SpieleSmartphone anzeigen. ");
		for (int i = 0; i < spielesmartphones.size(); i++) {
			System.out.println("Spielesmartphon : " + spielesmartphones.get(i));
			for (Iterator<OntClass> supers = spielesmartphones.get(i).listSuperClasses(true); supers.hasNext(); ) {
	            OntClass superClass = supers.next();
	            if (superClass.isRestriction()) {
	            	Restriction restriction = superClass.asRestriction();
	            	if (restriction.isSomeValuesFromRestriction()) {
	            		Resource res = restriction.asSomeValuesFromRestriction().getSomeValuesFrom();
	            		
	            		System.out.print(restriction.getOnProperty().getLocalName());
	            		System.out.print(" some ");
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
                                System.out.print( literal.getInt() );
                            }
                        } else {
                        	System.out.print(res.getURI());
                        }
	            		System.out.println();
	            	} 
	            }
			}
		}
	}

	private static boolean isCoveringAxiom(OntClass ontClass) {
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


}
