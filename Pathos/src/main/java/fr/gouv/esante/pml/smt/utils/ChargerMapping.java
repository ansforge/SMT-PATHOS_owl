package fr.gouv.esante.pml.smt.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ChargerMapping {
	
	public static HashMap<String, List<String>> listConceptPathos = new HashMap<String, List<String>>();
	public static HashMap<String, List<String>> listProfilsPathos = new HashMap<String, List<String>>();

	public static  void  chargeExcelConceptToList(final String xlsFile) throws IOException, ParseException {
		
		
		
		FileInputStream file = new FileInputStream(new File(xlsFile));
		
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		
		XSSFSheet sheet = workbook.getSheet("Feuil1");
		
		XSSFSheet sheetProfils = workbook.getSheet("Profils");
		
		Iterator<Row> rowIterator = sheet.iterator();
		
		rowIterator.next(); 
		
		
		while (rowIterator.hasNext()) {
			 
			 
			 Row row = rowIterator.next();
	    	 Cell c1 = row.getCell(0); //skos:notation
		     Cell c2 = row.getCell(1); // rdfs:label
		     Cell c3 = row.getCell(2); // skos:definition
		     Cell c4 = row.getCell(3); //pathos:profils_retenus
		     Cell c5 = row.getCell(4); //rdfs:comment
		     Cell c6 = row.getCell(5); //dc:type
		    

		     
		 
		     
	
		       List<String> listedonnees= new ArrayList<>();
		       
		       listedonnees.add(0, c2.getStringCellValue());
		       listedonnees.add(1, c3.getStringCellValue());
		       listedonnees.add(2, c4.getStringCellValue());
		       listedonnees.add(3, c5.getStringCellValue());
		       listedonnees.add(4, c6.getStringCellValue());
	            
	            
	            
	            listConceptPathos.put(c1.getStringCellValue(), listedonnees);
		    	 
		   
		     
		     
		     
		     
		   
		}
		
		
       Iterator<Row> rowIteratorProfils = sheetProfils.iterator();
		
       rowIteratorProfils.next(); 
		
		
		while (rowIteratorProfils.hasNext()) {
			 
			 
			 Row row = rowIteratorProfils.next();
	    	 Cell c1 = row.getCell(0); //skos:notation
		     Cell c2 = row.getCell(1); // rdfs:label
		     Cell c3 = row.getCell(2); // skos:definition
		     Cell c4 = row.getCell(3); //skosaltLabel
		     
		    

		     
		 
		     
	
		       List<String> listeProfils= new ArrayList<>();
		       
		       listeProfils.add(0, c2.getStringCellValue());
		       listeProfils.add(1, c3.getStringCellValue());
		       listeProfils.add(2, c4.getStringCellValue());
		       
	            
	            
	            
	            listProfilsPathos.put(c1.getStringCellValue(), listeProfils);
		    	 
		   
		     
		     
		     
		     
		   
		}
		
		
	    

	}

}
