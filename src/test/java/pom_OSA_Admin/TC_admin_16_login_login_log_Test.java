package pom_OSA_Admin;

import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.ecomm.OSA.genericUtilities.ExcelUtility;
import com.ecomm.OSA.genericUtilities.FileUtility;
import com.ecomm.OSA.genericUtilities.JavaUtility;
import com.ecomm.OSA.genericUtilities.WebdriverUtility;

import objectRepo_Admin.AdminHomePage;
import objectRepo_Admin.AdminLoginPage;
import objectRepo_User.UserHomePage;
import objectRepo_User.UserLoginPage;

public class TC_admin_16_login_login_log_Test {

	public static void main(String[] args) throws IOException, InterruptedException
	{
		ExcelUtility eLib = new ExcelUtility();
		FileUtility fLib = new FileUtility();
		JavaUtility jLib = new JavaUtility();
		WebdriverUtility wLib = new WebdriverUtility();
		int rand = jLib.getRandomNum();
		String adminUrl = fLib.getPropertyValue("Aurl");
		String adminUsername = fLib.getPropertyValue("Ausername");
		String adminPassword = fLib.getPropertyValue("Apassword");
		String userUrl = fLib.getPropertyValue("Uurl");
		
		System.setProperty("webdriver", "./chromedriver.exe");
		WebDriver driver=new ChromeDriver();
		driver.get(userUrl);
		wLib.waitForPageLaod(driver);
		wLib.maximizeWindow(driver);
		UserHomePage uhp = new UserHomePage(driver);
		uhp.getLogin().click();
		HashMap<String, String> newuser = eLib.getList("NewUser", 0, 1);
		UserLoginPage ulp = new UserLoginPage(driver);
		HashMap<String, String> credentials = ulp.createUser(driver, newuser, rand);
		String username = credentials.get("Username");
		String password = credentials.get("Password");
		wLib.acceptAlert(driver);
		uhp.getLogin().click();
		ulp.loginAsNewUser(username, password);
		uhp.getLogout().click();
		driver.get(adminUrl);
		AdminLoginPage alp = new AdminLoginPage(driver);
		alp.loginAsAdmin(adminUsername, adminPassword);
		AdminHomePage ahp = new AdminHomePage(driver);
		ahp.getUserLoginInfo().click();
		driver.findElement(By.xpath("//input")).sendKeys(username);
		String actualResult="";
		WebElement listcount = driver.findElement(By.xpath("//select[@size='1']"));
		wLib.select("100", listcount);
		for(;;)
		{
			try 
			{
				actualResult = driver.findElement(By.xpath("//td[.='"+username+"']")).getText();
				break;
			}
			catch (Exception e) 
			{
				driver.findElement(By.xpath("(//a[@role='button'])[2]")).click();
				
			}
		}
		if(actualResult.equals(username))
		{
		System.out.println(actualResult+"'s is present");
		}
		else
		{
			System.out.println("failed");
		}
		driver.quit();
	}

}
