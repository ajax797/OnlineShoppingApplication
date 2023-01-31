package com.ecomm.OSA.genericUtilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtility extends JavaUtility
{
	Workbook wb;
	{
		
		
		try {
			FileInputStream fis = new FileInputStream(IPathConstants.excelPath);
			wb = WorkbookFactory.create(fis);
			} 
		catch (IOException e) 
			{

			e.printStackTrace();
			}
		
	}
	public String readDataFromExcel(String sheetName, int rowNm, int cellNm) 
	{
		String value = wb.getSheet(sheetName).getRow(rowNm).getCell(cellNm).getStringCellValue();
		return value;
		
	}
	
	public void writeDataIntoExcel(String sheetName, int rowNm, int cellNm, String data) throws EncryptedDocumentException, IOException
	{
		Row row = wb.getSheet(sheetName).getRow(rowNm);
		if(row==null)
		{
			row=wb.getSheet(sheetName).createRow(rowNm);
		}
		row.createCell(cellNm).setCellValue(data);
		
		
		
		FileOutputStream fout=new FileOutputStream(IPathConstants.excelPath);
		wb.write(fout);
	}
	
	public int getLastRowNo(String sheetName) throws EncryptedDocumentException, IOException
	{
		int count = wb.getSheet(sheetName).getLastRowNum();
		return count;
	}
	
	public HashMap<String, String> getList(String sheetName, int keyCell, int valueCell) throws EncryptedDocumentException, IOException
	{
		Sheet sh = wb.getSheet(sheetName);
		int count =sh.getLastRowNum();
		HashMap<String, String> map = new HashMap<String, String>();
		for(int i=0;i<=count;i++)
		{
			String key = sh.getRow(i).getCell(keyCell).getStringCellValue();
			String value = sh.getRow(i).getCell(valueCell).toString();
			map.put(key, value);
		}
		return map;
	}
	
	public Object[][] readFromExcelToDP(String sheetName)
	{
		Sheet sh = wb.getSheet(sheetName);
		int lastRow =sh.getLastRowNum()+1;
		int lastCell = sh.getRow(0).getLastCellNum();
		Object[][] obj = new Object[lastRow][lastCell];
		for(int i=0;i<lastRow;i++)
		{
			for(int j=0;j<lastCell;j++)
			{
				obj[i][j]=sh.getRow(i).getCell(j).toString();
			}
		}
		return obj;
	}

}
