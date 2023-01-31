package com.ecomm.OSA.genericUtilities;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebdriverUtility extends JavaUtility
{
	public void maximizeWindow(WebDriver driver)
	{
		driver.manage().window().maximize();
	}
	
	public void waitForPageLaod(WebDriver driver)	
	{
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	public WebElement elementToBeVisible(WebDriver driver, WebElement element)
	{
		WebDriverWait wait = new WebDriverWait(driver, 10);
		return wait.until(ExpectedConditions.visibilityOf(element));
	}
	
	public void select(WebElement element, String value) throws InterruptedException
	{
		Select sel = new Select(element);
		Thread.sleep(500);
		sel.selectByValue(value);
	}
	
	public void select(WebElement element, int index) throws InterruptedException
	{
		Select sel = new Select(element);
		Thread.sleep(500);
		sel.selectByIndex(index);
	}
	
	public void select(String visibleText, WebElement element) throws InterruptedException
	{
		Select sel = new Select(element);
		sel.selectByVisibleText(visibleText);
	}
	public List<WebElement> getOptionsOfDropdown(WebElement element) throws InterruptedException
	{
		Select sel = new Select(element);
		List<WebElement> options = sel.getOptions();
		return options;
	}
	
	public void mousehover(WebDriver driver, WebElement element)
	{
		Actions act = new Actions(driver);
		act.moveToElement(element).perform();
	}
	
	public void dragAndDrop(WebDriver driver, WebElement src, WebElement dst)
	{
		Actions act = new Actions(driver);
		act.dragAndDrop(src, dst).perform();
	}
	
	public void doubleClickAction(WebDriver driver)
	{
		Actions act= new Actions(driver);
		act.doubleClick().perform();
	}
	
	public void rightClick(WebDriver driver)
	{
		Actions act= new Actions(driver);
		act.contextClick().perform();
	}
	
	public void rightClick(WebDriver driver, WebElement element)
	{
		Actions act= new Actions(driver);
		act.contextClick(element).perform();
	}
	
	public void enterKeyPress(WebDriver driver)
	{
		Actions act= new Actions(driver);
		act.sendKeys(Keys.ENTER).perform();
	}
	
	public void enterKey(WebDriver driver) throws AWTException 
	{
		Robot rb= new Robot();
		rb.keyPress(KeyEvent.VK_ENTER);
	}
	
	public void enterRelease(WebDriver driver) throws AWTException 
	{
		Robot rb= new Robot();
		rb.keyRelease(KeyEvent.VK_ENTER);
	}
	
	public void switchToFrame(WebDriver driver, int index)
	{
		driver.switchTo().frame(index);
	}
	
	public void switchToFrame(WebDriver driver, String nameOrID)
	{
		driver.switchTo().frame(nameOrID);
	}
	
	public void switchToFrame(WebDriver driver, WebElement address)
	{
		driver.switchTo().frame(address);
	}
	
	public void acceptAlert(WebDriver driver)
	{
		driver.switchTo().alert().accept();
	}
	
	public void cancelAlert(WebDriver driver)
	{
		driver.switchTo().alert().dismiss();
	}
	
	public void switchToWindow(WebDriver driver, String partialTitle)
	{
		Set<String> windows = driver.getWindowHandles();
		Iterator<String> it = windows.iterator();
		while(it.hasNext())
		{
			String winId = it.next();
			String currentWinTitle = driver.switchTo().window(winId).getCurrentUrl();
			if(currentWinTitle.contains(partialTitle))
			{
				break;
			}
		}
	}
	
	public String getScreenShot(WebDriver driver) throws IOException
	{
		String date = getSystemDateAndTimeInFormat();
		String name = "ScreenShot_"+date;
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		String path = "./screenshots/"+name+".png";
		File dst = new File(path);
		FileUtils.copyFile(src, dst);
		return path;
		
	}
	
	public void scrollBarAction(WebDriver driver, int yPixel)
	{
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(0,"+yPixel+")");
	}
	
	public void scrollToElement(WebDriver driver, WebElement element)
	{
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("argument[0].scrollIntoView();", element);
	}
	
	
	

}
