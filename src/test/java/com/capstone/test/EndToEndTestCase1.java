package com.capstone.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.capstone.base.BaseTest;
import com.capstone.utilities.ExcelUtiles;
import com.capstone.utilities.ScreenshotUtiles;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class EndToEndTestCase1 extends BaseTest {
	static String projectpath = System.getProperty("user.dir");
    WebDriver driver;
    WebDriverWait wait;

    // Locators
    By link_Register = By.className("ico-register");
    By input_FirstName = By.id("FirstName");
    By input_LastName = By.id("LastName");
    By input_Email = By.id("Email");
    By input_Password = By.id("Password");
    By input_ConfirmPassword = By.id("ConfirmPassword");
    By radio_Male = By.id("gender-male");
    By btn_Register = By.id("register-button");
    By link_Logout = By.className("ico-logout");
    By register_SuccessMsg = By.className("result");
    By loginButton = By.className("ico-login");
    By btn_Continue = By.xpath("//a[contains(@class, 'register-continue-button')]");
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

    @Test(dataProvider = "registerdata")
    public void testRegisterAndLogout(String firstname,String lastname,String email,String password,String comfirmpass) throws Exception {
    	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.get("https://demo.nopcommerce.com");
        test = extent.createTest("Verify Register and Logout Functionality");

        // Verify homepage
        String expectedHomeTitle = "nopCommerce demo store";
        String actualHomeTitle = driver.getTitle();
        if (actualHomeTitle.contains(expectedHomeTitle)) {
            test.pass("Home page is opened successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "HomePage_NotLoaded");
            test.fail("Home page is not loaded").addScreenCaptureFromPath(screenPath);
        }
        
        // Click Register link
        driver.findElement(link_Register).click();

        // Verify URL contains "register"
        String currentURL = driver.getCurrentUrl();
        if (currentURL.contains("register")) {
            test.pass("Register page URL is correct");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Incorrect_Register_URL");
            test.fail("Register page URL is incorrect").addScreenCaptureFromPath(screenPath);
        }

        // Verify heading is "Register"
        WebElement heading = driver.findElement(By.tagName("h1"));
        if (heading.getText().equals("Register")) {
            test.pass("Register page heading is correct");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Incorrect_Register_Heading");
            test.fail("Register page heading is incorrect").addScreenCaptureFromPath(screenPath);
        }

        // Fill the registration form
        driver.findElement(radio_Male).click();
        driver.findElement(input_FirstName).sendKeys(firstname);
        driver.findElement(input_LastName).sendKeys(lastname);
        driver.findElement(input_Email).sendKeys(email);
        driver.findElement(input_Password).sendKeys(password);
        driver.findElement(input_ConfirmPassword).sendKeys(comfirmpass);

        // Click Register
        driver.findElement(btn_Register).click();

        // Wait for success message
        WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(register_SuccessMsg));
        if (successMsg.getText().contains("Your registration completed")) {
            test.pass("User registered successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Registration_Failed");
            test.fail("Registration failed").addScreenCaptureFromPath(screenPath);
        }
        
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(btn_Continue));
        continueButton.click();

        // Verify URL after clicking Continue
        String expectedURL = "https://demo.nopcommerce.com/";
        String actualURL = driver.getCurrentUrl();
        if (actualURL.equalsIgnoreCase(expectedURL)) {
            test.pass("Continue button redirected to homepage successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Continue_Redirection_Failed");
            test.fail("Continue button did not redirect correctly")
                .addScreenCaptureFromPath(screenPath)
                .info("Expected URL: " + expectedURL + " | Actual URL: " + actualURL);
        }
        // Logout
        driver.findElement(link_Logout).click();

        // Verify user is logged out by checking  of Login button
        if ( driver.findElement(loginButton).isDisplayed()) {
            test.pass("Login button is visible after logout");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "LoginButton_NotVisible");
            test.fail("Login button is not visible after logout").addScreenCaptureFromPath(screenPath);
        }
        

    }
    @DataProvider
    public Object[][] registerdata() throws IOException {
        String excelpath = projectpath + "\\src\\test\\resources\\Testdata\\registerdata.xlsx";
        return ExcelUtiles.getData(excelpath, "Sheet1");
    }
}
