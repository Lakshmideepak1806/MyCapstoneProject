package com.capstone.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.capstone.base.BaseTest;
import com.capstone.utilities.ScreenshotUtiles;

import java.time.Duration;

public class EndToEndTestCase3 extends BaseTest {
    WebDriver driver;
    WebDriverWait wait;

    // Locators
    By loginLink = By.className("ico-login");
    By input_Email = By.id("Email");
    By input_Password = By.id("Password");
    By btn_Login = By.xpath("//button[contains(@class,'login-button')]");
    By featuredProductAddToCart = By.xpath("((//button[@class='button-2 product-box-add-to-cart-button'])[1])");
    By cartIcon = By.xpath("//span[@class='cart-label']");
    By cartItem = By.linkText("Build your own computer");
    By logoutLink = By.className("ico-logout");
    By loginButton = By.className("ico-login");
    By notificationBar = By.id("bar-notification");

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
    public void testFeaturedProductAddToCartFlow() throws Exception {
        driver.get("https://demo.nopcommerce.com");
        test = extent.createTest("TC_E2E_003 - Login > Add from Featured Products > Verify Cart > Logout");
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
        }
        // Add to cart from Featured Products
        WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(featuredProductAddToCart));
        if (addToCart.isDisplayed()) {
            addToCart.click();
            
            test.pass("Clicked Add to Cart from Featured Products");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "AddToCart_NotVisible");
            test.fail("Add to Cart button not visible").addScreenCaptureFromPath(screenPath);
        }
        // Wait for RAM dropdown to be visible and select an option and Wait for HDD radio button and click it
        WebElement ramDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("product_attribute_2")));
        Select selectRAM = new Select(ramDropdown);
        selectRAM.selectByVisibleText("8GB [+$60.00]");
        WebElement hddRadio = wait.until(ExpectedConditions.elementToBeClickable(By.id("product_attribute_3_7"))); 
        hddRadio.click();
        // Now click the actual "Add to cart" button
        WebElement finalAddToCart = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart-button-1")));
        finalAddToCart.click();
        test.pass("Selected RAM and HDD, and clicked Add to Cart");
        // Validate Add to Cart notification
        WebElement notification = wait.until(ExpectedConditions.visibilityOfElementLocated(notificationBar));
        if (notification.getText().toLowerCase().contains("the product has been added")) {
            test.pass("Product successfully added to cart");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "AddToCart_Failed");
            test.fail("Product not added").addScreenCaptureFromPath(screenPath);
        }

        wait.until(ExpectedConditions.invisibilityOfElementLocated(notificationBar));

        // Go to Cart
        WebElement cart = wait.until(ExpectedConditions.elementToBeClickable(cartIcon));
        cart.click();
        test.pass("Navigated to Cart page");

        // Validate item in cart
        WebElement cartItemElement = wait.until(ExpectedConditions.visibilityOfElementLocated(cartItem));
        if (cartItemElement.isDisplayed()) {
            test.pass("Product is visible in the cart");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "CartItem_NotVisible");
            test.fail("Product not found in cart").addScreenCaptureFromPath(screenPath);
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
