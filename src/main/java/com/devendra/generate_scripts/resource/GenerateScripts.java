package com.devendra.generate_scripts.resource;

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
	public String generateScript(@RequestParam("file") MultipartFile file, @RequestParam("tableName") String tableName,
			@RequestParam("limitRows") Integer limitRows, @RequestParam("startingRowNum") Integer startingRowNum,
			@RequestParam("integerColumns") String integerColumns) throws InvalidFormatException, IOException {
		Workbook wb = getWorkbook(file);
		Sheet sheet = wb.getSheetAt(0);
		Row row;
		Cell cell;
		int totalRows = sheet.getLastRowNum();
		int noOfColumns = 0;
		StringBuilder query = new StringBuilder();
		query.append("insert into " + tableName + " (");
		if (limitRows==null || limitRows>totalRows) {
				limitRows=totalRows;
		}
		if (startingRowNum==null) {
			startingRowNum=1;
		}

		DataFormatter formatter = new DataFormatter();
		List<String> integerList = Arrays.asList(integerColumns.split(","));
		row = sheet.getRow(startingRowNum - 1);
		noOfColumns = row.getLastCellNum();
		for (int i = 0; i < noOfColumns; i++) {
			if (i == noOfColumns - 1)
				query.append(row.getCell(i) + ") values(");
			else
				query.append(row.getCell(i) + ",");
		}
		for (int i = startingRowNum; i <= limitRows; i++) {
			row = sheet.getRow(i);
			for (int j = 0; j < noOfColumns; j++) {
				cell = row.getCell(j);
				String val = formatter.formatCellValue(cell);
				if (val.isEmpty()) {
					val = null;
					query.append(val);
				} else if (integerList.contains(Integer.toString(j + 1))) {
					query.append(val);
				} else {
					query.append("'" + val + "'");
				}
				if (j == noOfColumns - 1)
					query.append(")");
				else
					query.append(",");
			}
			if (i != limitRows) {
				query.append(",(");
			}
		}
		return query.toString();

	}

	public Workbook getWorkbook(MultipartFile file) throws InvalidFormatException, IOException {
		Workbook wb = null;
		byte[] bytes = file.getBytes();
		String fileType = getFileExtension(file.getOriginalFilename());
		File tempFile = File.createTempFile("abc", "xlsx");
		FileOutputStream fos = new FileOutputStream(tempFile);
		fos.write(bytes);
		fos.close();
		if (fileType.equalsIgnoreCase("xlsx")) {
			wb = new XSSFWorkbook(tempFile);
		} else if (fileType.equalsIgnoreCase("xls")) {
			wb = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(tempFile)));
		}
		return wb;

	}

	private static String getFileExtension(String fileName) {
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
	}
}
