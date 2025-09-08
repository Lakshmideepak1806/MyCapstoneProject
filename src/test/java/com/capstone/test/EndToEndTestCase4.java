package com.capstone.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.capstone.base.BaseTest;
import com.capstone.utilities.ScreenshotUtiles;

import java.time.Duration;

public class EndToEndTestCase4 extends BaseTest {
    WebDriver driver;
    WebDriverWait wait;

    // Locators
    By loginLink = By.className("ico-login");
    By input_Email = By.id("Email");
    By input_Password = By.id("Password");
    By btn_Login = By.xpath("//button[contains(@class,'login-button')]");
    By logoutLink = By.className("ico-logout");
    By wishlistButton = By.className("wishlist-label");
    By notificationBar = By.id("bar-notification");
    By loginButton = By.className("ico-login");

    @BeforeMethod
    public void beforeMethod() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
        	driver.quit();  
        }
    }

    @Test
    public void testAddToWishlistFlow() throws Exception {
        driver.get("https://demo.nopcommerce.com");
        test = extent.createTest("TC_E2E_004 - Login > Add to Wishlist > Verify > Logout");

        // Click Login
        WebElement login = wait.until(ExpectedConditions.elementToBeClickable(loginLink));
        if (login.isDisplayed()) {
            login.click();
            test.pass("Login link clicked successfully.");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "LoginLink_NotVisible");
            test.fail("Login link not visible").addScreenCaptureFromPath(screenPath);
            return;
        }

        // Enter credentials
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(input_Email));
        WebElement password = driver.findElement(input_Password);
        email.sendKeys("Lakshmideepak18@gmail.com"); 
        password.sendKeys("Dp@123");
        test.pass("Entered email and password");
        driver.findElement(btn_Login).click();

        // Verify login
        WebElement logout = wait.until(ExpectedConditions.visibilityOfElementLocated(logoutLink));
        if (logout.isDisplayed()) {
            test.pass("User logged in successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "LoginFailed");
            test.fail("Login failed").addScreenCaptureFromPath(screenPath);
            return;
        }

        // Navigate to "Books" category
        driver.findElement(By.linkText("Books")).click();
        test.pass("Navigated to Books category");

        // Click "Add to Wishlist" for first visible product
        WebElement addToWishlist = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[@class='button-2 add-to-wishlist-button'])[1]")));
        addToWishlist.click();
        test.pass("Clicked Add to Wishlist");

        // Wait for notification
        WebElement notification = wait.until(ExpectedConditions.visibilityOfElementLocated(notificationBar));
        if (notification.getText().toLowerCase().contains("wishlist")) {
            test.pass("Product added to wishlist");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Wishlist_Failed");
            test.fail("Product not added to wishlist").addScreenCaptureFromPath(screenPath);
            return;
        }

        wait.until(ExpectedConditions.invisibilityOfElementLocated(notificationBar));

        // Click Wishlist link in header
        WebElement wishlistHeader = wait.until(ExpectedConditions.elementToBeClickable(wishlistButton));
        wishlistHeader.click();
        test.pass("Navigated to Wishlist page");

        // Verify product in wishlist
        WebElement wishlistItem = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='wishlist-content']//a")));
        if (wishlistItem.isDisplayed()) {
            test.pass("Product appears in wishlist");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "WishlistItem_NotVisible");
            test.fail("Product not visible in wishlist").addScreenCaptureFromPath(screenPath);
        }

        // Logout
        WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(logoutLink));
        logoutBtn.click();
        test.pass("Logout clicked");

        WebElement loginAgain = wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton));
        if (loginAgain.isDisplayed()) {
            test.pass("User logged out successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "LogoutFailed");
            test.fail("Logout failed").addScreenCaptureFromPath(screenPath);
        }
    }
}
