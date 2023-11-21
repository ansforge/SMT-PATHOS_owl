package fr.gouv.esante.pml.smt.pathos;


import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.rdf4j.model.vocabulary.DCTERMS;
import org.eclipse.rdf4j.model.vocabulary.SKOS;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AddOntologyAnnotation;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.SetOntologyID;
import org.semanticweb.owlapi.vocab.DublinCoreVocabulary;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import fr.gouv.esante.pml.smt.utils.PathosVocabulary;
import fr.gouv.esante.pml.smt.utils.ChargerMapping;
import fr.gouv.esante.pml.smt.utils.DCTVocabulary;
import fr.gouv.esante.pml.smt.utils.DCVocabulary;
import fr.gouv.esante.pml.smt.utils.PropertiesUtil;
import fr.gouv.esante.pml.smt.utils.SKOSVocabulary;
import uk.ac.manchester.cs.owl.owlapi.OWL2DatatypeImpl;

import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;



public class XLSXOWLTransformer_V1 {
	
	//*********
	private static String xlsxPathosFileName = PropertiesUtil.getProperties("xlsxPathosFile");
	  private static String owlPathosFileName = PropertiesUtil.getProperties("owlPathosFileName");

	  private static OWLOntologyManager man = null;
	  private static OWLOntology onto = null;
	  private static OWLDataFactory fact = null;
	  
	  private static OWLAnnotationProperty skosNotation  = null;
	  private static OWLAnnotationProperty skosDefinition  = null;
	  private static OWLAnnotationProperty skosAltLabel  = null;
	  
	  private static OWLAnnotationProperty rdfsLabel  = null;
	  private static OWLAnnotationProperty dcType  = null;

	  private static OWLAnnotationProperty rdfsComments  = null;
	  private static OWLAnnotationProperty pathosProfilsRetenus  = null;
	
	
	public static void main(String[] args) throws Exception {
		
		ChargerMapping.chargeExcelConceptToList(xlsxPathosFileName);
		//ChargerMapping.chargeExcelC(xlsxPathosFileName);
		  
		final OutputStream fileoutputstream = new FileOutputStream(owlPathosFileName);
		 man = OWLManager.createOWLOntologyManager();
		 onto = man.createOntology(IRI.create(PropertiesUtil.getProperties("terminologie_IRI")));
		 fact = onto.getOWLOntologyManager().getOWLDataFactory();
		
		 skosNotation =  fact.getOWLAnnotationProperty(SKOSVocabulary.NOTATION.getIRI());
		 skosDefinition =  fact.getOWLAnnotationProperty(SKOSVocabulary.DEFINITION.getIRI());
		 skosAltLabel =  fact.getOWLAnnotationProperty(SKOSVocabulary.ALTLABEL.getIRI());
		 rdfsLabel =  fact.getOWLAnnotationProperty(fr.gouv.esante.pml.smt.utils.OWLRDFVocabulary.RDFS_LABEL.getIRI());
		 dcType = fact.getOWLAnnotationProperty(DCVocabulary.type.getIRI());
		 rdfsComments =  fact.getOWLAnnotationProperty(fr.gouv.esante.pml.smt.utils.OWLRDFVocabulary.RDFS_COMMENT.getIRI());
		 pathosProfilsRetenus =  fact.getOWLAnnotationProperty(PathosVocabulary.profils_retenus.getIRI());
		 
		 
		    OWLClass owlClass = null;
		    
		    
		    createPrincipalNoeud();
		    createConceptProfilsPATHOS();
		    //createConceptRetires2023Noeud();
		    
		    
		    
		    for(String id: ChargerMapping.listProfilsPathos.keySet()) {
		    	
		    if(!id.isEmpty() && id!=null) {	
		    	
		    	final String about = PropertiesUtil.getProperties("terminologie_URI") + id;
		    	 owlClass = fact.getOWLClass(IRI.create(about));
			     OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
			     man.applyChange(new AddAxiom(onto, declare));
			     
			     String aboutSubClass = null;
			      aboutSubClass = "http://data.esante.gouv.fr/pathos/Profils"  ;
			      OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
			        
			      OWLAxiom axiom = fact.getOWLSubClassOfAxiom(owlClass, subClass);
			      man.applyChange(new AddAxiom(onto, axiom));
			      
			      addLateralAxioms(rdfsLabel, ChargerMapping.listProfilsPathos.get(id).get(0), owlClass, "fr");
			      addLateralAxioms(skosNotation, id, owlClass);
			      addLateralAxioms(skosDefinition, ChargerMapping.listProfilsPathos.get(id).get(1), owlClass);
			      if(!ChargerMapping.listProfilsPathos.get(id).get(2).isEmpty() && ChargerMapping.listProfilsPathos.get(id).get(2)!=null)
			       addLateralAxioms(skosAltLabel, ChargerMapping.listProfilsPathos.get(id).get(2), owlClass);
			      
		    }  
		    	
		  }
		    

		    
		    for(String id: ChargerMapping.listConceptPathos.keySet()) {
		    	
		     if(!id.isEmpty() && id!=null) {	
		    	
		    	final String about = PropertiesUtil.getProperties("terminologie_URI") + id;
		    	//if(!id.isEmpty() && id!=null)
		    	 //about =	PropertiesUtil.getProperties("terminologie_URI") + id;
		    	//else
		    	 //about =	PropertiesUtil.getProperties("terminologie_URI") + ChargerMapping.listConceptPathos.get(id).get(0);
		    	
		        owlClass = fact.getOWLClass(IRI.create(about));
		        OWLAxiom declare = fact.getOWLDeclarationAxiom(owlClass);
		        man.applyChange(new AddAxiom(onto, declare));
		        
		        
		        
		        String aboutSubClass = null;
		        aboutSubClass = PropertiesUtil.getProperties("URI_parent")  ;
		        OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
		        
		        OWLAxiom axiom = fact.getOWLSubClassOfAxiom(owlClass, subClass);
		        man.applyChange(new AddAxiom(onto, axiom));
		        
		        
		       
		        addLateralAxioms(rdfsLabel, ChargerMapping.listConceptPathos.get(id).get(0), owlClass, "fr");
		        
		        //if(!id.isEmpty() && id!=null)
		         addLateralAxioms(skosNotation, id, owlClass);
		        //else
		          //addLateralAxioms(skosNotation, ChargerMapping.listConceptPathos.get(id).get(0), owlClass);
		        
		        if(!ChargerMapping.listConceptPathos.get(id).get(1).isEmpty() &&  ChargerMapping.listConceptPathos.get(id).get(1)!=null)
		         addLateralAxioms(skosDefinition, ChargerMapping.listConceptPathos.get(id).get(1), owlClass);
		        
		        if(!ChargerMapping.listConceptPathos.get(id).get(4).isEmpty() && ChargerMapping.listConceptPathos.get(id).get(4)!=null)
		         addLateralAxioms(dcType, ChargerMapping.listConceptPathos.get(id).get(4), owlClass);
		        
		        if(!ChargerMapping.listConceptPathos.get(id).get(3).isEmpty() &&  ChargerMapping.listConceptPathos.get(id).get(3)!=null)
		         addLateralAxioms(rdfsComments, ChargerMapping.listConceptPathos.get(id).get(3), owlClass);
		        
		        String [] listePathosProfils =  ChargerMapping.listConceptPathos.get(id).get(2).split(",");
		       
		        for(String profilsPathos: listePathosProfils) {
		        	
		        	if(!profilsPathos.trim().isEmpty() && profilsPathos.trim()!=null) {
		        	 String profils = profilsPathos.trim();	
		        	 addURIAxioms(pathosProfilsRetenus, profils.replace(".", ""), owlClass);
		        	 
		        	}
		        }
			      
		      
		       
		     
		      
		       
		       
		     }else {
		    	 
		    	 System.out.println("********label "+ChargerMapping.listConceptPathos.get(id).get(0));
		     }
		        
		    }
		    
		    
		    final RDFXMLDocumentFormat ontologyFormat = new RDFXMLDocumentFormat();
		    ontologyFormat.setPrefix("dct", "http://purl.org/dc/terms/");
		    //ontologyFormat.setPrefix("ans", "http://www.data.esante.gouv.fr/ANS-CGTS/MetaModel/");
		    
		    
		    IRI iri = IRI.create(PropertiesUtil.getProperties("terminologie_IRI"));
		    man.applyChange(new SetOntologyID(onto,  new OWLOntologyID(iri)));
		   
		  //  addPropertiesOntology();
		    
		    man.saveOntology(onto, ontologyFormat, fileoutputstream);
		    fileoutputstream.close();
		    System.out.println("Done.");
		
		

	}
	
	public static void addLateralAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {
	    final OWLAnnotation annotation =
	        fact.getOWLAnnotation(prop, fact.getOWLLiteral(val));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	  }
  
  public static void addLateralAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass, String lang) {
	    final OWLAnnotation annotation =
	        fact.getOWLAnnotation(prop, fact.getOWLLiteral(val, lang));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	  }
  
  public static void addDatelAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {
	    final OWLAnnotation annotation =
	    		fact.getOWLAnnotation(prop, fact.getOWLLiteral(val, OWL2Datatype.XSD_DATE_TIME));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	  }
  
  
  public static void addURIAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {

	    IRI iri_creator = IRI.create(PropertiesUtil.getProperties("terminologie_URI")+val);
		   
	    OWLAnnotationProperty prop_creator =fact.getOWLAnnotationProperty(prop.getIRI());
	    
	    OWLAnnotation annotation = fact.getOWLAnnotation(prop_creator, iri_creator);
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	    
	    
	  }
  
  public static void addBooleanAxioms(OWLAnnotationProperty prop, String val, OWLClass owlClass) {
	    final OWLAnnotation annotation =
	        fact.getOWLAnnotation(prop, fact.getOWLLiteral(val,OWL2Datatype.XSD_BOOLEAN));
	    final OWLAxiom axiom = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(), annotation);
	    man.applyChange(new AddAxiom(onto, axiom));
	  }
  
  
  
  
  public static void createPrincipalNoeud() {
	  
	  // String parent_label_en=PropertiesUtil.getProperties("label_noeud_parent_en");
	 //  String parent_label_fr=PropertiesUtil.getProperties("label_noeud_parent_fr");

	   String noeud_parent_notation=PropertiesUtil.getProperties("notation_noeud_parent");
	    
	   final String classRacine = PropertiesUtil.getProperties("URI_parent") ;
	   OWLClass noeudRacine = fact.getOWLClass(IRI.create(classRacine));
       addLateralAxioms(skosNotation, noeud_parent_notation, noeudRacine);
       addLateralAxioms(rdfsLabel, "PATHOS", noeudRacine, "fr");
       //addLateralAxioms(rdfsLabel, parent_label_en, noeudRacine, "en");
	  
  }
  
  
  
  public static void createConceptRetires2023Noeud() {
	  
	  
	    
	   final String classRacine = "http://data.esante.gouv.fr/pathos/Profils2022" ;
	   OWLClass noeudRacine = fact.getOWLClass(IRI.create(classRacine));
	   
	   String aboutSubClass = null;
      aboutSubClass = "http://data.esante.gouv.fr/pathos/Profils"  ;
      OWLClass subClass = fact.getOWLClass(IRI.create(aboutSubClass));
      
      OWLAxiom axiom = fact.getOWLSubClassOfAxiom(noeudRacine, subClass);
      man.applyChange(new AddAxiom(onto, axiom));
    
	  addLateralAxioms(skosNotation, "Profils 2023", noeudRacine);
     
	  
}
  
  
  
  public static void createConceptProfilsPATHOS() {
	  
	  
	    
	   final String classRacine = "http://data.esante.gouv.fr/pathos/Profils" ;
	   OWLClass noeudRacine = fact.getOWLClass(IRI.create(classRacine));
	    addLateralAxioms(skosNotation, "Profils PATHOS", noeudRacine);
     
	  
   }
  
  
   
  
  
  
  

}
