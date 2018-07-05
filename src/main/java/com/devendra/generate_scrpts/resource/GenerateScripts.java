package com.devendra.generate_scrpts.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/generatescripts")
public class GenerateScripts {
	
	@PostMapping
	public String generateScript(@RequestParam("file")MultipartFile file,@RequestParam("tableName")String tableName,
			@RequestParam("maxRows")int maxRows,
			@RequestParam("columnNamesRowNum")int columnNamesRowNum)
					throws InvalidFormatException, IOException {
		Workbook wb=getWorkbook(file);
		
		Sheet sheet = wb.getSheetAt(0);
	    Row row;
	    Cell cell;
	  
	    StringBuilder query = new StringBuilder();
	    query.append("insert into "+tableName+" (");
	    
	    int headerColumnsCount=0;
	    
	    DataFormatter formatter = new DataFormatter();
	    List<Integer> integerList=Arrays.asList();
	    row=sheet.getRow(columnNamesRowNum-1);
	    headerColumnsCount=row.getPhysicalNumberOfCells();
	    for(int i=0;i<headerColumnsCount;i++) {
			   
			   if(i==headerColumnsCount-1)
				   query.append(row.getCell(i)+") values(");
			   else
				   query.append(row.getCell(i)+",");
		   }
		   for(int i=columnNamesRowNum;i<maxRows;i++) {
			   row=sheet.getRow(i);
			   int pCells=row.getPhysicalNumberOfCells();
			   System.out.println(pCells);
			   for(int j=0;j<headerColumnsCount;j++) {
				   cell=row.getCell(j);
				   String val = formatter.formatCellValue(cell);
				   if(val.isEmpty()) {
					   val=null;
					   query.append(val);
				   }
				   else if(integerList.contains(j+1)) {
					query.append(val);
				    }
				    else {
				    	query.append("'"+val+"'");
				    }
				    if(j==headerColumnsCount-1)
				    	query.append(")");
				    else
				    	query.append(",");
			   }
			   if(i!=maxRows-1) {
				   query.append(",(");
			   }
		   }
		   
		   System.out.println(query.toString());
	
		return query.toString();
		
	}
	public Workbook getWorkbook(MultipartFile file) throws InvalidFormatException, IOException {
		Workbook wb = null;
		byte[] bytes = file.getBytes();
		String fileType=getFileExtension(file.getOriginalFilename());
		File tempFile=File.createTempFile("abc", "xlsx");
		FileOutputStream fos=new FileOutputStream(tempFile);
		fos.write(bytes);
		fos.close();
		System.out.println(file.getOriginalFilename());
		if(fileType.equalsIgnoreCase("xlsx")) {
			wb = new XSSFWorkbook(tempFile);
		}
		else if(fileType.equalsIgnoreCase("xls")) {
			wb=new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(tempFile)));
		}
		return wb;
		
	}
	
	public static void main(String... args) throws IOException, InvalidFormatException {
		Workbook wb=null;
		
			//Workbook wb = new XSSFWorkbook(new File("E:\\7LP\\Scripts\\for insert.xlsx"));
		 wb=new HSSFWorkbook(new POIFSFileSystem(new FileInputStream("E:\\7LP\\Scripts\\for insert.xlsx")));
			Sheet sheet = wb.getSheetAt(0);
		    Row row;
		    Cell cell;
		    String tableName="rfr_pgs_fspevent";
		    StringBuilder query = new StringBuilder();
		    query.append("insert into "+tableName+" (");
		    int maxRows=3;
		    int headerColumnsCount=0;
		    int columnNamesRow=1;
		    DataFormatter formatter = new DataFormatter();
		    List<Integer> integerList=Arrays.asList();
		    row=sheet.getRow(columnNamesRow-1);
		    headerColumnsCount=row.getPhysicalNumberOfCells();
		   for(int i=0;i<headerColumnsCount;i++) {
			   
			   if(i==headerColumnsCount-1)
				   query.append(row.getCell(i)+") values(");
			   else
				   query.append(row.getCell(i)+",");
		   }
		   for(int i=columnNamesRow;i<maxRows;i++) {
			   row=sheet.getRow(i);
			   int pCells=row.getPhysicalNumberOfCells();
			   System.out.println(pCells);
			   for(int j=0;j<headerColumnsCount;j++) {
				   cell=row.getCell(j);
				   String val = formatter.formatCellValue(cell);
				   if(val.isEmpty()) {
					   val=null;
					   query.append(val);
				   }
   				   else if(integerList.contains(j+1)) {
   					query.append(val);
				    }
				    else {
				    	query.append("'"+val+"'");
				    }
				    if(j==headerColumnsCount-1)
				    	query.append(")");
				    else
				    	query.append(",");
			   }
			   if(i!=maxRows-1) {
				   query.append(",(");
			   }
		   }
		   
		   System.out.println(query.toString());
		}
	
	private static String getFileExtension(String fileName) {
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
	}


