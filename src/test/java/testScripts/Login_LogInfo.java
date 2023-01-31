package testScripts;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.ecomm.OSA.genericUtilities.BaseClass;

import objectRepo_Admin.AdminHomePage;
import objectRepo_Admin.TodaysOrderPage;
import objectRepo_User.UserHomePage;
import objectRepo_User.UserLoginPage;

public class Login_LogInfo extends BaseClass{
	///////
	String username;
	public int rand;
	@Test(groups = {"Admin","smoke"})
	public void SignUpUser() throws InterruptedException, IOException
	{
		rand=jLib.getRandomNum();
		String userUrl = fLib.getPropertyValue("Uurl");
		driver.get(userUrl);
		UserHomePage uhp = new UserHomePage(driver);
		uhp.getLogin().click();
		HashMap<String, String> newuser = eLib.getList("NewUser", 0, 1);
		UserLoginPage ulp = new UserLoginPage(driver);
		HashMap<String, String> credentials = ulp.createUser(driver, newuser, rand);
		username = credentials.get("Username");
		String password = credentials.get("Password");
		wLib.acceptAlert(driver);
		uhp.getLogin().click();
		ulp.loginAsNewUser(username, password);
		uhp.getLogout().click();
	}
	
	@Test(groups = {"Admin","smoke"})
	public void LoginInfo() throws InterruptedException, IOException
	{
		AdminHomePage ahp = new AdminHomePage(driver);
		ahp.getUserLoginInfo().click();
		driver.findElement(By.xpath("//input")).sendKeys(username,Keys.BACK_SPACE);
		String actualResult="";
		WebElement listcount = driver.findElement(By.xpath("//select[@size='1']"));
		TodaysOrderPage top = new TodaysOrderPage(driver);
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
				top.getNextIcon().click();
			}
		}
		assertEquals(actualResult, username);
	}
}
