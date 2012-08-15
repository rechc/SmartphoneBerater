package de.htw.berater;

import de.htw.berater.controller.Answer;
//import de.htw.berater.db.SQLConstraint;

public class Berater2 extends Berater {

	public Berater2(String rdfPath, String ns) {
		super(rdfPath, ns);
	}

	@Override
	public String evaluateAndAskNewQuestion(String string) {
		switch (context) {
		case 1:
			return profiSmartphone(string);
		case 2:
			context = 3;
			nextAnswer = Answer.YESNO;
			return "Ka war hier her, oder wat?";
		}
		throw new RuntimeException("wrong context");
	}

	@Override
	public String evaluateAndAskNewQuestion(boolean yes) {
		switch (context) {
		}
		throw new RuntimeException("wrong context");
	}

	@Override
	public String askFirstQuestionZweck() {
		context = 1;
		nextAnswer = Answer.KEYWORD;
		return "Guten Tag! Wie kann ich Ihnen helfen??";
	}

	@Override
	public String askFirstQuestionGeneral() {
		throw new UnsupportedOperationException();
	}
	
	public String profiSmartphone(String profiPhone){
//		OntClass smartphone = model.getOntClass(ns + "ProfiSmartphone");
//		ExtendedIterator<OntClass> ri = smartphone.listSubClasses();
//		List<OntClass> spielesmartphones = new ArrayList<OntClass>();
//		while (ri.hasNext()) {
//			OntClass subClass = ri.next();
//			for (Iterator<OntClass> supers = subClass.listSuperClasses(true); supers.hasNext(); ) {
//	            OntClass superClass = supers.next();
//	            if (superClass.isRestriction()) {
//	            	Restriction restriction = superClass.asRestriction();
//	                if (restriction.getOnProperty().getLocalName().contains("hatZweck")) {
//	                	//some
//	                	if (restriction.isSomeValuesFromRestriction()) {
//	                		OntClass range = restriction.asSomeValuesFromRestriction().getSomeValuesFrom().as(OntClass.class);
////	                		if (range.equals(zweckSubClass)) {
////	                			spielesmartphones.add(subClass);
////	                		}
//	                	}
//	                }
//	            } 
//			}
//		}
//		for (int i = 0; i < spielesmartphones.size(); i++) {
//			if (isCoveringAxiom(spielesmartphones.get(i))) {
//				OntClass abstractClass = spielesmartphones.get(i);
//				spielesmartphones.remove(i);
//				ri = abstractClass.listSubClasses();
//				while (ri.hasNext()) {
//					spielesmartphones.add(ri.next());
//				}
//			}
//		}
//	
//		List<SQLConstraint> sqlConstraints = new LinkedList<SQLConstraint>();
//		for (int i = 0; i < spielesmartphones.size(); i++) {
//			SQLConstraint sqlConstraint = new SQLConstraint();
//			for (Iterator<OntClass> supers = spielesmartphones.get(i).listSuperClasses(true); supers.hasNext(); ) {
//	            OntClass superClass = supers.next();
//	            if (superClass.isRestriction()) {
//	            	Restriction restriction = superClass.asRestriction();
//	            	if (restriction.isSomeValuesFromRestriction()) {
//	            		Resource res = restriction.asSomeValuesFromRestriction().getSomeValuesFrom();
//	            		
//	            		sqlConstraint.setKey(restriction.getOnProperty().getLocalName());
//	            		if (res.hasProperty( RDF.type, RDFS.Datatype )) {
//                            Property owlWithRestrictions = ResourceFactory.createProperty( OWL.getURI(), "withRestrictions" );
//                            Property minInclusive = ResourceFactory.createProperty( XSD.getURI(), "minInclusive" );
//                            Property minExclusive = ResourceFactory.createProperty( XSD.getURI(), "minExclusive" );
//
//                            // the datatype restrictions are represented as a list
//                            // we make some assumptions about the content of the list; this code
//                            // could be more defensive about testing for expected values
//                            Resource wr = res.getProperty( owlWithRestrictions ).getResource();
//                            RDFList wrl = wr.as( RDFList.class );
//
//                            for (Iterator<RDFNode> k = wrl.iterator(); k.hasNext(); ) {
//                                Resource wrClause = (Resource) k.next();
//                                Statement stmt = wrClause.getProperty( minInclusive );
//                                if (stmt == null) {
//                                	stmt = wrClause.getProperty(minExclusive);
//                                }
//                                Literal literal = stmt.getLiteral();
//        	            		sqlConstraint.setValue(literal.getInt() + "");
//                            }
//                        } else {
//                        	sqlConstraint.setValue(res.getLocalName() + "");
//                        }
//	            	} 
//	            }
//			}
//			sqlConstraints.add(sqlConstraint);
//		}
////		currentSQLConstraints.addAll(sqlConstraints);
//		String s = "Moechten sie ";
//		for (OntClass smphone : spielesmartphones) {
//			s += smphone.toString() + " "; 
//		}
//		s+= "?";
//		s = s.replace(" ?", "?");
//		context = 2;
//		nextAnswer = Answer.KEYWORD;
//		return s;
		return null;
	}

}
