package testScripts;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.ecomm.OSA.genericUtilities.BaseClass;

import objectRepo_User.MyCartPage;
import objectRepo_User.PaymentPage;
import objectRepo_User.UserHomePage;

public class Place_Order extends BaseClass{

	
	@Test(dependsOnMethods = "editProduct",groups = "User")
	public void placeOrder()
	{
		wLib.waitForPageLaod(driver);
		UserHomePage uhp = new UserHomePage(driver);
		uhp.getSearchTextfield().sendKeys(Editing_product.Uexpectedresult);
		uhp.getSearchButton().click();
		driver.findElement(By.xpath("//a[.='"+Editing_product.Uexpectedresult+"']")).click();
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
		System.out.println("Order for "+Editing_product.Uexpectedresult+" has been successfully placed");
	}
	

}
