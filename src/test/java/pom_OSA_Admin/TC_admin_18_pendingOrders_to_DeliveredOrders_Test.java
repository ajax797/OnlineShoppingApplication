package pom_OSA_Admin;

import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.ecomm.OSA.genericUtilities.ExcelUtility;
import com.ecomm.OSA.genericUtilities.FileUtility;
import com.ecomm.OSA.genericUtilities.JavaUtility;
import com.ecomm.OSA.genericUtilities.WebdriverUtility;

import objectRepo_Admin.AdminHomePage;
import objectRepo_Admin.AdminLoginPage;
import objectRepo_Admin.CategoryPage;
import objectRepo_Admin.InsertProductPage;
import objectRepo_User.MyCartPage;
import objectRepo_User.PaymentPage;
import objectRepo_Admin.SubCategoryPage;
import objectRepo_Admin.TodaysOrderPage;
import objectRepo_Admin.UpdateOrderPage;
import objectRepo_User.UserHomePage;
import objectRepo_User.UserLoginPage;

public class TC_admin_18_pendingOrders_to_DeliveredOrders_Test {

	public static void main(String[] args) throws IOException, InterruptedException
	{
		//DatabaseUtility dLib = new DatabaseUtility();
		ExcelUtility eLib = new ExcelUtility();
		FileUtility fLib = new FileUtility();
		JavaUtility jLib = new JavaUtility();
		WebdriverUtility wLib = new WebdriverUtility();
		int rand = jLib.getRandomNum();
		String adminUrl = fLib.getPropertyValue("Aurl");
		String adminUsername = fLib.getPropertyValue("Ausername");
		String adminPassword = fLib.getPropertyValue("Apassword");
		String userUrl = fLib.getPropertyValue("Uurl");
		String userUsername = fLib.getPropertyValue("Uusername");
		String userPassword = fLib.getPropertyValue("Upassword");
		
		WebDriver driver=new ChromeDriver();
		driver.get(adminUrl);
		wLib.waitForPageLaod(driver);
		wLib.maximizeWindow(driver);
		AdminLoginPage alp = new AdminLoginPage(driver);
		alp.loginAsAdmin(adminUsername, adminPassword);
		AdminHomePage ahp = new AdminHomePage(driver);
		ahp.getCreateCategory().click();
		String categoryName = eLib.readDataFromExcel("CatTestdata", 0, 1)+rand;
		String categorydescription = eLib.readDataFromExcel("CatTestdata", 1, 1);
		CategoryPage cp = new CategoryPage(driver);
		cp.createCategory(categoryName, categorydescription);
		ahp.getSubCategory().click();
		String subcategoryName = eLib.readDataFromExcel("SubTestdata", 0, 1)+rand;
		SubCategoryPage scp = new SubCategoryPage(driver);
		scp.createSubcategory(subcategoryName, categoryName);
		ahp.getInsertProduct().click();
		
		
		
		HashMap<String, String> productDetails = eLib.getList("ProductTestdata", 0, 1);
		InsertProductPage ip = new InsertProductPage(driver);
		String expectedresult = ip.insertProduct(driver, rand, categoryName, subcategoryName, productDetails);
		
		
		ahp.getLogoutButton().click(); 
		driver.get(userUrl);
		UserHomePage uhp = new UserHomePage(driver);
		uhp.getLogin().click();
		UserLoginPage ulp = new UserLoginPage(driver);
		ulp.loginAsUser(userUsername, userPassword);
		uhp.getSearchTextfield().sendKeys(expectedresult);
		uhp.getSearchButton().click();
		driver.findElement(By.xpath("//a[.='"+expectedresult+"']")).click();
		uhp.getAddToCart().click();
		try
		{
		wLib.acceptAlert(driver);
		}
		catch(Exception e)
		{
			System.out.println("Alert handled");
		}
		uhp.getMyCart().click();
		MyCartPage mcp = new MyCartPage(driver);
		mcp.getProceedToCheckout().click();
		PaymentPage pp = new PaymentPage(driver);
		pp.getcODOption().click();
		pp.getSubmitButton().click();
		driver.get(adminUrl);
		alp.loginAsAdmin(adminUsername, adminPassword);
		ahp.getTodaysOrder().click();
		TodaysOrderPage top = new TodaysOrderPage(driver);
		wLib.select("100", top.getEntryNumber());
		for(;;)
		{
			try 
			{
				driver.findElement(By.xpath("//td[.='"+expectedresult+"']/..//a")).click();
				break;
			}
			catch (Exception e) 
			{
				top.getNextIcon().click();
				
			}
		}
		//driver.findElement(By.xpath("//td[.='"+expectedresult+"']/..//a")).click();
		String parenttab = driver.getWindowHandle();
		wLib.switchToWindow(driver, "Update Compliant");
		UpdateOrderPage uo = new UpdateOrderPage(driver);
		uo.updateOrderToProgress();
		wLib.acceptAlert(driver);
		driver.close();
		driver.switchTo().window(parenttab);
		ahp.getPendingOrder().click();
		wLib.select("100", top.getEntryNumber());
		for(;;)
		{
			try 
			{
				driver.findElement(By.xpath("//td[.='"+expectedresult+"']/..//a")).click();
				break;
			}
			catch (Exception e) 
			{
				top.getNextIcon().click();
				
			}
		}
		String parenttab1 = driver.getWindowHandle();
		wLib.switchToWindow(driver, "Update Compliant");
		uo.updateOrderToDelivered();
		wLib.acceptAlert(driver);
		driver.switchTo().window(parenttab1);
		ahp.getDeliveredOrder().click();
		wLib.select("100", top.getEntryNumber());
		String actualresult;
		for(;;)
		{
			try 
			{
				actualresult = driver.findElement(By.xpath("//td[.='"+expectedresult+"']")).getText();
				break;
			}
			catch (Exception e) 
			{
				driver.findElement(By.xpath("(//a[@role='button'])[2]")).click();
				
			}
		}
		if(actualresult.equals(expectedresult))
		{
		System.out.println(actualresult+" is delivered");
		}
		else
		{
			System.out.println("failed");
		}
		driver.quit();
		
	}

}
