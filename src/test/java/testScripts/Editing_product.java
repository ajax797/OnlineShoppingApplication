package testScripts;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.poi.EncryptedDocumentException;
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

@Listeners(com.ecomm.OSA.genericUtilities.ListenersImplementationClass.class)
public class Editing_product extends BaseClass
{
	String categoryName;
	String subcategoryName;
	String expectedresult;
	public static String Uexpectedresult;
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
		ahp.getSubCategory().click();
		SubCategoryPage scp = new SubCategoryPage(driver);
		List<WebElement> catOptions = wLib.getOptionsOfDropdown(scp.getCategoryDropdown());
		String actualResult=null;
		for (WebElement catOption : catOptions) 
		{
			if(catOption.getText().equals(categoryName))
			{
				actualResult = catOption.getText();
				break;
			}
		}
		assertEquals(categoryName, actualResult);
		
	}
	@Test(dependsOnMethods = "createCategory",groups = {"Admin","smoke"})
	public void createSubcategory() throws InterruptedException, EncryptedDocumentException, IOException
	{
		AdminHomePage ahp = new AdminHomePage(driver);
		ahp.getSubCategory().click();
		subcategoryName = eLib.readDataFromExcel("SubTestdata", 0, 1)+rand;
		SubCategoryPage scp = new SubCategoryPage(driver);
		scp.createSubcategory(subcategoryName, categoryName);
		ahp.getInsertProduct().click();
		InsertProductPage ip = new InsertProductPage(driver);
		List<WebElement> subcatOptions = wLib.getOptionsOfDropdown(ip.getSubcategoryDropdown());
		String actualResult=null;
		for (WebElement subcatOption : subcatOptions) 
		{
			if(subcatOption.getText().equals(categoryName))
			{
				actualResult = subcatOption.getText();
				break;
			}
		}
		assertEquals(subcategoryName, actualResult);
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
	
	@Test(dependsOnMethods = "insert_Product",groups = {"Admin","smoke"})
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
	
}
