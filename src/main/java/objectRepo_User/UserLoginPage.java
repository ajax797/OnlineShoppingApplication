package objectRepo_User;

import java.util.HashMap;
import java.util.Map.Entry;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class UserLoginPage
{
	@FindBy(name = "email")
	private WebElement emailText;
	
	@FindBy(name = "password")
	private WebElement passwordText;
	
	@FindBy(name = "login")
	private WebElement loginButton;
	
	@FindBy(name = "fullname")
	private WebElement fullName;
	
	@FindBy(name = "emailid")
	private WebElement emailId;
	
	@FindBy(name = "contactno")
	private WebElement contactNo;
	
	@FindBy(xpath = "(//input[@name='password'])[2]")
	private WebElement password;
	
	@FindBy(name = "confirmpassword")
	private WebElement confirmPassword;
	
	@FindBy(name = "submit")
	private WebElement submitButton;
	
	
	public UserLoginPage(WebDriver driver)
	{
		PageFactory.initElements(driver, this);
	}


	public WebElement getEmailText() {
		return emailText;
	}


	public WebElement getPasswordText() {
		return passwordText;
	}


	public WebElement getLoginButton() {
		return loginButton;
	}


	public WebElement getFullName() {
		return fullName;
	}


	public WebElement getEmailId() {
		return emailId;
	}


	public WebElement getContactNo() {
		return contactNo;
	}


	public WebElement getPassword() {
		return password;
	}


	public WebElement getConfirmPassword() {
		return confirmPassword;
	}


	public WebElement getSubmitButton() {
		return submitButton;
	}
	
	public void loginAsNewUser(String userUsername,String userPassword)
	{
		getEmailText().sendKeys(userUsername);
		getPasswordText().sendKeys(userPassword);
		getLoginButton().click();
	}
	
	public HashMap<String, String> createUser(WebDriver driver, HashMap<String, String> newuser, int rand)
	{
		HashMap<String, String> credentials = new HashMap<String, String>();
		
		 for (Entry<String, String> e:newuser.entrySet())
		 {
			String key = e.getKey();
			String value = e.getValue();
				
			if(key.equals("password"))
			{
				credentials.put("Password", value+rand);
				driver.findElement(By.xpath("(//input[@name='"+key+"'])[2]")).sendKeys(value+rand);
			}
			else
			{
				if(key.equals("emailid"))
				{
					credentials.put("Username", value+rand+"@gmail.com");
					driver.findElement(By.name(key)).sendKeys(value+rand+"@gmail.com");
					
				}
				else
				{
					driver.findElement(By.name(key)).sendKeys(value+rand);
				}
			}
		 }
		 getSubmitButton().click();
		 return credentials;
	}
	
	
	

}
