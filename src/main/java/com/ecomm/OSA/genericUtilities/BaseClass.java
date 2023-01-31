package com.ecomm.OSA.genericUtilities;

import java.io.IOException;
import java.sql.SQLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import objectRepo_Admin.AdminHomePage;
import objectRepo_Admin.AdminLoginPage;
import objectRepo_User.UserHomePage;
import objectRepo_User.UserLoginPage;

public class BaseClass 
{
	public static WebDriver driver;
	public static ExcelUtility eLib = new ExcelUtility();
	public static DatabaseUtility dLib = new DatabaseUtility();
	public static FileUtility fLib = new FileUtility();
	public static WebdriverUtility wLib = new WebdriverUtility();
	public static JavaUtility jLib= new JavaUtility();
	
	@BeforeSuite
	public void connectDB() throws SQLException
	{
		//dLib.connectToDB();
	}
	//@Parameters("BROWSER")
	@BeforeClass()
	public void launchBrowser( /* String browser */ ) throws IOException
	{
		String browser = fLib.getPropertyValue("browser");
		if(browser.equalsIgnoreCase("chrome"))
		{
			driver=new ChromeDriver();
		}
		else if (browser.equalsIgnoreCase("firefox"))
		{
			driver=new FirefoxDriver();
		}
		else
		{
			System.out.println("Invalid Browser");
		}
		wLib.maximizeWindow(driver);
		wLib.waitForPageLaod(driver);
		
	}
	@BeforeMethod(onlyForGroups = "Admin",groups = "smoke")
	public void loginApp() throws IOException
	{
		String adminUrl = fLib.getPropertyValue("Aurl");
		driver.get(adminUrl);
		String adminUsername = fLib.getPropertyValue("Ausername");
		String adminPassword = fLib.getPropertyValue("Apassword");
		AdminLoginPage alp = new AdminLoginPage(driver);
		alp.loginAsAdmin(adminUsername, adminPassword);
		
		
	}
	@BeforeMethod(onlyForGroups = "User")
	public void loginApp1() throws IOException
	{
		String userUrl = fLib.getPropertyValue("Uurl");
		driver.get(userUrl);
		UserHomePage uhp = new UserHomePage(driver);
		uhp.getLogin().click();
		String userUsername = fLib.getPropertyValue("Uusername");
		String userPassword = fLib.getPropertyValue("Upassword");
		UserLoginPage ulp = new UserLoginPage(driver);
		ulp.loginAsUser(userUsername, userPassword);
		
		
	}
	@AfterMethod(onlyForGroups = "Admin")
	public void logoutApp()
	{
		AdminHomePage ahp = new AdminHomePage(driver);
		ahp.getLogoutButton().click(); 
		
	}
	@AfterMethod(onlyForGroups = "User")
	public void logoutApp1()
	{
		UserHomePage uhp = new UserHomePage(driver);
		uhp.getLogout().click();
	}
	@AfterClass
	public void closeBrowser()
	{
		driver.quit();
	}
	@AfterSuite
	public void disconnectDB () throws SQLException
	{
		//dLib.disconnectDB();
	}

}
