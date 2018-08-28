package com.insider;

import static org.testng.Assert.assertEquals;
import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class testRunner {
	static String baseURL="https://www.amazon.com";
	static String username="seautotest@dispostable.com";
	static String passwordValue = "123456";
	static String search = "samsung";
	static String title = "Amazon.com: Online Shopping for Electronics, Apparel, Computers, Books, DVDs & more";
	
	public static void main(String args[]) throws InterruptedException {
		
		
		//Set browser config && start the browser && openURL && GetTitle
		System.setProperty("webdriver.chrome.driver", "chromedriver/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get(baseURL);
		driver.manage().window().maximize();
		title = driver.getTitle();
		assertEquals(title,driver.getTitle());
		
		//Login
		driver.findElement(By.xpath("//a[@id='nav-link-accountList']")).click();
		driver.findElement(By.id("ap_email")).sendKeys(username);
		driver.findElement(By.id("continue")).click();
		driver.findElement(By.id("ap_password")).sendKeys(passwordValue);
		driver.findElement(By.id("signInSubmit")).click();	
		
		//Enter search string and click on search
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys(search);
		driver.findElement(By.xpath("//input[@type='submit']")).click();
		
		
		//Verify Search Results
		String resultSet1 = driver.findElement(By.xpath("//h2[@class='a-size-medium s-inline  s-access-title  a-text-normal']")).getText();
		System.out.println("Does the search returned the results based on entered string: " +resultSet1.toLowerCase().contains((search)));
		
		//Navigate to 2nd page
		WebDriverWait wait = new WebDriverWait(driver,10000);
		driver.findElement(By.id("pagnNextString")).click();

		
		//Verify the current page
		WebElement currentPage = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='pagnCur']")));
		currentPage.getText();
		assertEquals(currentPage.getText(),"2");
		
		
		//Select 3rd item from the search results
		ArrayList<WebElement> resultSet = new ArrayList<WebElement>();
		resultSet = (ArrayList<WebElement>) driver.findElements(By.xpath("//h2[@class='a-size-medium s-inline  s-access-title  a-text-normal']"));
		String chosenItem =resultSet.get(4).getText(); 
		System.out.println(String.format("Item selected to add to WishList: %s", chosenItem));
		resultSet.get(4).click();
		//Thread.sleep(10000);
		
		
		
		//Add to the WishList
		driver.findElement(By.id("add-to-wishlist-button-submit")).click();
		WebElement popup = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='a-popover a-popover-modal a-declarative a-popover-modal-fixed-height pop-huc-add']")));
		popup.click();	
		WebElement viewList = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@class='w-button-text']")));
		viewList.click();
		
		
		//Navigate to Accounts and lists and click on List
		WebElement actionsList = driver.findElement(By.xpath("//a[@id='nav-link-accountList']"));
		actionsList.click();				
		driver.findElement(By.linkText("Lists")).click();
		
		//Feed the contents of the Wishlist to an Arraylist
		ArrayList<WebElement> wishlistItems = new ArrayList<WebElement>();
		wishlistItems = (ArrayList<WebElement>) driver.findElements(By.xpath("//a[@class='a-link-normal']"));
		
		
		//Get the First item in the WishList and delete it
		assertEquals(wishlistItems.get(8).getText(),chosenItem);
		driver.findElement(By.xpath("//input[@name='submit.deleteItem']")).click();
		
		driver.navigate().refresh();
		
		//Verify whether the wishlisted item is deleted or not
		ArrayList<WebElement> wishlistItemsAfterDelete = new ArrayList<WebElement>();
		wishlistItemsAfterDelete = (ArrayList<WebElement>) driver.findElements(By.xpath("//a[@class='a-link-normal']"));


		for(WebElement element:wishlistItemsAfterDelete) {
			Assert.assertFalse(element.getText().contains(chosenItem));
		}
		//assertNotEquals(wishlistItemsAfterDelete.get(8).getText(), chosenItem);
		
		System.out.println("TestCompleted");
		driver.quit();
		
	}
	

}
