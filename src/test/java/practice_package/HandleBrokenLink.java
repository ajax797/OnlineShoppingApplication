package practice_package;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.ecomm.OSA.genericUtilities.ExcelUtility;

public class HandleBrokenLink {
	

	public static void main(String[] args) throws IOException 
	{
		ExcelUtility exl = new ExcelUtility();
		WebDriver driver=new ChromeDriver();
		driver.get("https://www.google.com");
		List<WebElement> links = driver.findElements(By.xpath("//a"));
		Integer statuscode=0;
		String statusmsg=null;
		String code=null;
		for (int i=0;i<links.size();i++)
		{
			String linktext = links.get(i).getAttribute("href");
			String texts = links.get(i).getText();
			try
			{
			URL url=new URL(linktext);
			URLConnection urlconnection = url.openConnection();
			HttpURLConnection http = (HttpURLConnection)urlconnection;
			statuscode = http.getResponseCode();
			 code = statuscode.toString();
			 statusmsg = http.getResponseMessage();
			}
			catch(Exception e)
			{
				
			}
			exl.writeDataIntoExcel("Sheet6", i, 0, texts);
			exl.writeDataIntoExcel("Sheet6", i, 1, linktext);
			exl.writeDataIntoExcel("Sheet6", i, 2, code);
			exl.writeDataIntoExcel("Sheet6", i, 3, statusmsg);
			
		}
	}
	
	
}
