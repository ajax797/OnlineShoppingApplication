package testNG_OSA_Admin;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.ecomm.OSA.genericUtilities.BaseClass;

import objectRepo_Admin.AdminHomePage;
import objectRepo_Admin.CategoryPage;
import objectRepo_Admin.InsertProductPage;
import objectRepo_Admin.ManageProductPage;
import objectRepo_Admin.SubCategoryPage;
import objectRepo_Admin.TodaysOrderPage;
import objectRepo_Admin.UpdateOrderPage;
import objectRepo_User.MyCartPage;
import objectRepo_User.PaymentPage;
import objectRepo_User.UserHomePage;
import objectRepo_User.UserLoginPage;

@Listeners(com.ecomm.OSA.genericUtilities.ListenersImplementationClass.class)
public class Admin_Module extends BaseClass
{
	String expectedresult;
	String Uexpectedresult;
	HashMap<String, String> productDetails;
	public int rand;
	@Test(groups = {"Admin","smoke"}, retryAnalyzer = com.ecomm.OSA.genericUtilities.RetryAnalyzerImplementationClass.class )
	public void createProduct() throws IOException, InterruptedException
	{
		rand=jLib.getRandomNum();
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
		productDetails = eLib.getList("ProductTestdata", 0, 1);
		InsertProductPage ip = new InsertProductPage(driver);	
		expectedresult = ip.insertProduct(driver, rand, categoryName, subcategoryName, productDetails);	
		ahp.getManageProduct().click();
		fail();
		driver.findElement(By.xpath("//input")).sendKeys(expectedresult,Keys.ENTER);
		String ActualResult = driver.findElement(By.xpath("//td[text() ='"+expectedresult+"']")).getText();
		assertEquals(ActualResult, expectedresult);
		
	}
	
	@Test(dependsOnMethods = "createProduct",groups = {"Admin","smoke"})
	public void editProduct()
	{
		AdminHomePage ahp = new AdminHomePage(driver);
		ahp.getManageProduct().click();
		ManageProductPage mpp = new ManageProductPage(driver);
		mpp.getSearchTextfield().sendKeys(expectedresult);
		driver.findElement(By.xpath("//td[.='"+expectedresult+"']/..//a[1]")).click();
		for (Entry<String, String> e1:productDetails.entrySet())
		 {
			String key = e1.getKey();
			String value = e1.getValue();
			WebElement ele = driver.findElement(By.name(key));
			ele.clear();
			ele.sendKeys("updated"+value+rand);
			if(key.equals("productName"))
			{
				Uexpectedresult ="updated"+value+rand;
			}
		 }
		driver.findElement(By.xpath("//button[.='Update']")).click();
		ahp.getManageProduct().click();
		mpp.getSearchTextfield().sendKeys(Uexpectedresult);
		String ActualResult = driver.findElement(By.xpath("//td[text() ='"+Uexpectedresult+"']")).getText();
		assertEquals(ActualResult, expectedresult);
		
	}
	@Test(dependsOnMethods = "editProduct",groups = "User")
	public void placeOrder()
	{
		wLib.waitForPageLaod(driver);
		UserHomePage uhp = new UserHomePage(driver);
		uhp.getSearchTextfield().sendKeys(Uexpectedresult);
		uhp.getSearchButton().click();
		driver.findElement(By.xpath("//a[.='"+Uexpectedresult+"']")).click();
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
		System.out.println("Order for "+Uexpectedresult+" has been successfully placed");
	}
	
	@Test(dependsOnMethods = "placeOrder",groups = {"Admin","smoke","regression"})
	public void manageOrder() throws InterruptedException
	{
		wLib.waitForPageLaod(driver);
		AdminHomePage ahp = new AdminHomePage(driver);
		ahp.getTodaysOrder().click();
		TodaysOrderPage top = new TodaysOrderPage(driver);
		wLib.select("100", top.getEntryNumber());
		for(;;)
		{
			try 
			{
				driver.findElement(By.xpath("//td[.='"+Uexpectedresult+"']/..//a")).click();
				break;
			}
			catch (Exception e) 
			{
				top.getNextIcon().click();
				
			}
		}
		String parenttab = driver.getWindowHandle();
		wLib.switchToWindow(driver, "updateorder");
		UpdateOrderPage uo = new UpdateOrderPage(driver);
		uo.updateOrderToProgress();
		wLib.acceptAlert(driver);
		driver.switchTo().window(parenttab);
		ahp.getPendingOrder().click();
		top.getEntryNumber();
		wLib.select("100", top.getEntryNumber());
		for(;;)
		{
			try 
			{
				driver.findElement(By.xpath("//td[.='"+Uexpectedresult+"']/..//a")).click();
				break;
			}
			catch (Exception e) 
			{
				top.getNextIcon().click();
				
			}
		}
		String parenttab1 = driver.getWindowHandle();
		wLib.switchToWindow(driver, "updateorder");
		uo.updateOrderToDelivered();
		wLib.acceptAlert(driver);
		driver.switchTo().window(parenttab1);
		ahp.getDeliveredOrder().click();
		String actualresult;
		wLib.select("100", top.getEntryNumber());
		for(;;)
		{
			try 
			{
				actualresult = driver.findElement(By.xpath("//td[.='"+Uexpectedresult+"']")).getText();
				break;
			}
			catch (Exception e) 
			{
				top.getNextIcon().click();
			}
		}
		assertEquals(actualresult, Uexpectedresult);
		
	}
	
	@Test
	public void login_logInfo() throws InterruptedException, IOException
	{
		rand=jLib.getRandomNum();
		String userUrl = fLib.getPropertyValue("Uurl");
		driver.get(userUrl);
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
		loginApp();
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
