package testScripts;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.EncryptedDocumentException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;

import com.ecomm.OSA.genericUtilities.BaseClass;

import objectRepo_Admin.AdminHomePage;
import objectRepo_Admin.CategoryPage;
import objectRepo_Admin.InsertProductPage;
import objectRepo_Admin.SubCategoryPage;
import objectRepo_Admin.TodaysOrderPage;
import objectRepo_Admin.UpdateOrderPage;
import objectRepo_User.MyCartPage;
import objectRepo_User.PaymentPage;
import objectRepo_User.UserHomePage;

public class Manage_Order extends BaseClass
{
	String categoryName;
	String subcategoryName;
	String expectedresult;
	String Uexpectedresult;
	HashMap<String, String> productDetails;
	public int rand;
	@Test(groups = {"Admin","smoke"}, retryAnalyzer = com.ecomm.OSA.genericUtilities.RetryAnalyzerImplementationClass.class )
	public void createCategory() throws IOException, InterruptedException
	{
		rand=jLib.getRandomNum();
		AdminHomePage ahp = new AdminHomePage(driver);
		ahp.getCreateCategory().click();
		categoryName = eLib.readDataFromExcel("CatTestdata", 0, 1)+rand;
		String categorydescription = eLib.readDataFromExcel("CatTestdata", 1, 1);
		CategoryPage cp = new CategoryPage(driver);
		cp.createCategory(categoryName, categorydescription);
	}
	@Test(dependsOnMethods = "createCategory",groups = {"Admin","smoke"})
	public void createSubcategory() throws InterruptedException, EncryptedDocumentException, IOException
	{
		AdminHomePage ahp = new AdminHomePage(driver);
		ahp.getSubCategory().click();
		subcategoryName = eLib.readDataFromExcel("SubTestdata", 0, 1)+rand;
		SubCategoryPage scp = new SubCategoryPage(driver);
		scp.createSubcategory(subcategoryName, categoryName);
	}
	@Test(dependsOnMethods = "createSubcategory",groups = {"Admin","smoke"})
	public void insert_Product() throws EncryptedDocumentException, IOException, InterruptedException
	{
		AdminHomePage ahp = new AdminHomePage(driver);
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
	@Test(dependsOnMethods = "insert_Product",groups = {"User","smoke"})
	public void placeOrder()
	{
		wLib.waitForPageLaod(driver);
		UserHomePage uhp = new UserHomePage(driver);
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
		System.out.println("Order for "+expectedresult+" has been successfully placed");
	}
	@Test(dependsOnMethods = "placeOrder",groups = {"Admin","smoke"})
	public void updateToPending() throws InterruptedException
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
				driver.findElement(By.xpath("//td[.='"+expectedresult+"']/..//a")).click();
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
	}
		
	@Test(dependsOnMethods = "updateToPending",groups = {"Admin","smoke"})
	public void updateToDelivered() throws InterruptedException
	{
		AdminHomePage ahp = new AdminHomePage(driver);
		ahp.getPendingOrder().click();
		TodaysOrderPage top = new TodaysOrderPage(driver);
		top.getEntryNumber();
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
		wLib.switchToWindow(driver, "updateorder");
		UpdateOrderPage uo = new UpdateOrderPage(driver);
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
				actualresult = driver.findElement(By.xpath("//td[.='"+expectedresult+"']")).getText();
				break;
			}
			catch (Exception e) 
			{
				top.getNextIcon().click();
			}
		}
		assertEquals(actualresult,expectedresult);
		
	}
	

}
