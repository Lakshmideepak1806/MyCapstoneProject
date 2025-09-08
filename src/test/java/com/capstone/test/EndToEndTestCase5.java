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

public class EndToEndTestCase5 extends BaseTest {
    WebDriver driver;
    WebDriverWait wait;

    // Locators
    By loginLink = By.className("ico-login");
    By input_Email = By.id("Email");
    By input_Password = By.id("Password");
    By btn_Login = By.xpath("//button[contains(@class,'login-button')]");
    By logoutLink = By.className("ico-logout");
    By loginButton = By.className("ico-login");
    By cartIcon = By.xpath("//span[@class='cart-label']");
    By notificationBar = By.id("bar-notification");

    @BeforeMethod
    public void beforeMethod() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }
    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
            driver.quit();
        }
    }
    @Test
    public void testCartCheckoutUpToConfirmOrder() throws Exception {
        driver.get("https://demo.nopcommerce.com");
        test = extent.createTest("TC_E2E_005 - Login > Add to Cart > Checkout (No Payment) > Logout");
        // Login
        WebElement login = wait.until(ExpectedConditions.elementToBeClickable(loginLink));
        login.click();
        test.pass("Clicked on login");
        WebElement email = wait.until(ExpectedConditions.visibilityOfElementLocated(input_Email));
        WebElement password = driver.findElement(input_Password);
        email.sendKeys("Lakshmideepak18@gmail.com"); // Use valid user
        password.sendKeys("Dp@123");
        test.pass("Entered email and password");
        driver.findElement(btn_Login).click();
        WebElement logout = wait.until(ExpectedConditions.visibilityOfElementLocated(logoutLink));
        if (logout.isDisplayed()) {
            test.pass("Login successful");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "LoginFailed");
            test.fail("Login failed").addScreenCaptureFromPath(screenPath);     
        }
        // Navigate to Books category
        driver.findElement(By.linkText("Books")).click();
        test.pass("Navigated to Books");
        
        // Add first book to cart
        WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//button[contains(@class,'add-to-cart-button')])[1]")));
        addToCart.click();
        test.pass("Clicked Add to Cart");
        // Wait for cart notification
        WebElement notification = wait.until(ExpectedConditions.visibilityOfElementLocated(notificationBar));
        if (notification.getText().toLowerCase().contains("added")) {
            test.pass("Product added to cart");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Cart_Add_Failed");
            test.fail("Product not added to cart").addScreenCaptureFromPath(screenPath);   
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(notificationBar));
     // Click on cart icon
        WebElement cart = wait.until(ExpectedConditions.elementToBeClickable(cartIcon));
        if (cart.isDisplayed()) {
            cart.click();
            test.pass("Navigated to Cart");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "CartIcon_NotVisible");
            test.fail("Cart icon not clickable or visible").addScreenCaptureFromPath(screenPath);
        }
        // Proceed to Checkout
        WebElement agreeCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id("termsofservice")));
        if (agreeCheckbox.isDisplayed()) {
            agreeCheckbox.click();
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "TermsCheckbox_NotVisible");
            test.fail("Terms of service checkbox not found").addScreenCaptureFromPath(screenPath);     
        }
     // Click Checkout Button
        WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout")));
        if (checkoutBtn.isDisplayed()) {
            checkoutBtn.click();
            test.pass("Clicked Checkout");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "CheckoutBtn_NotVisible");
            test.fail("Checkout button not found or not clickable").addScreenCaptureFromPath(screenPath);
        }
//        // Wait for Billing Form
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BillingNewAddress_FirstName")));
//
//        // Fill Billing Address Fields
//        driver.findElement(By.id("BillingNewAddress_FirstName")).clear();
//        driver.findElement(By.id("BillingNewAddress_FirstName")).sendKeys("Lakshmi Deepak");
//
//        driver.findElement(By.id("BillingNewAddress_LastName")).clear();
//        driver.findElement(By.id("BillingNewAddress_LastName")).sendKeys("N");
//
//        driver.findElement(By.id("BillingNewAddress_Email")).clear();
//        driver.findElement(By.id("BillingNewAddress_Email")).sendKeys("Lakshmideepak18@gmail.com");
//
//        Select countryDropdown = new Select(driver.findElement(By.id("BillingNewAddress_CountryId")));
//        countryDropdown.selectByVisibleText("United States of America");
//        Select stateDropdown = new Select(driver.findElement(By.id("BillingNewAddress_StateProvinceId")));
//        stateDropdown.selectByVisibleText("California"); // Change as needed

//        driver.findElement(By.id("BillingNewAddress_City")).sendKeys("Los Angeles");
//        driver.findElement(By.id("BillingNewAddress_Address1")).sendKeys("123 Main St");
//        driver.findElement(By.id("BillingNewAddress_Address2")).sendKeys("Apt 4B"); // Optional
//        driver.findElement(By.id("BillingNewAddress_ZipPostalCode")).sendKeys("90001");
//        driver.findElement(By.id("BillingNewAddress_PhoneNumber")).sendKeys("1234567890");

        // Click Billing Continue Button
        WebElement billingContinue = wait.until(
            ExpectedConditions.elementToBeClickable(By.name("save"))
        );

        if (billingContinue.isDisplayed()) {
            billingContinue.click();
            
            test.pass("Billing info continued");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "BillingContinue_NotVisible");
            test.fail("Billing Continue button not found").addScreenCaptureFromPath(screenPath);
        }
        

        // Shipping Method
        WebElement shippingMethodContinue = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@class='button-1 shipping-method-next-step-button']")));
        if (shippingMethodContinue.isDisplayed()) {
            shippingMethodContinue.click();
            test.pass("Shipping method selected");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "ShippingMethodContinue_NotVisible");
            test.fail("Shipping Method Continue button not found").addScreenCaptureFromPath(screenPath);
        }
        Thread.sleep(3000);
        driver.findElement(By.xpath("//button[@Class='button-1 payment-method-next-step-button']")).click();
//        // Payment Method
//        WebElement paymentMethodContinue = wait.until(ExpectedConditions.elementToBeClickable(By.name("save")));
//        if (paymentMethodContinue.isDisplayed()) {
//            paymentMethodContinue.click();
//            test.pass("Payment method selected");
//        } else {
//            String screenPath = ScreenshotUtiles.Capture(driver, "PaymentMethodContinue_NotVisible");
//            test.fail("Payment Method Continue button not found").addScreenCaptureFromPath(screenPath);
//            
//        }
//        Thread.sleep(3000);
        // Payment Info
        WebElement paymentInfoContinue = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@class='button-1 payment-info-next-step-button']")));
        if (paymentInfoContinue.isDisplayed()) {
            paymentInfoContinue.click();
            test.pass("Payment info continued");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "PaymentInfoContinue_NotVisible");
            test.fail("Payment Info Continue button not found").addScreenCaptureFromPath(screenPath);
           
        }

        // Confirm Order Page (do not place actual order)
        WebElement confirmOrderBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[@class='button-1 payment-info-next-step-button']")));
        if (confirmOrderBtn.isDisplayed()) {
            test.pass("Reached Confirm Order page successfully (skipping actual payment)");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "ConfirmOrder_NotReached");
            test.fail("Failed to reach Confirm Order page").addScreenCaptureFromPath(screenPath);
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
