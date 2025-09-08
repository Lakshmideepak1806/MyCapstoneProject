package manualTestcaseAutomition;

import org.testng.annotations.Test;
import com.capstone.base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import com.capstone.utilities.ScreenshotUtiles;

public class RegisterTestCase1 extends BaseTest {
    WebDriver driver;

    static String projectpath = System.getProperty("user.dir");

    By link_Register = By.className("ico-register");
    By input_FirstName = By.id("FirstName");
    By input_LastName = By.id("LastName");
    By input_Email = By.id("Email");
    By input_Password = By.id("Password");
    By input_ConfirmPassword = By.id("ConfirmPassword");
    By radio_Male = By.id("gender-male");
    By btn_Register = By.id("register-button");

    @BeforeMethod
    public void beforeMethod() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void verifyRegisterPageIsVisible() throws InterruptedException, IOException {
        driver.get("https://demo.nopcommerce.com");
        test = extent.createTest("Verify Register Page is Visible");

        String expectedHomeTitle = "nopCommerce demo store";
        String actualHomeTitle = driver.getTitle();
        if (actualHomeTitle.contains(expectedHomeTitle)) {
            test.pass("Home page is opened successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "HomePage_NotLoaded");
            test.fail("Home page is not loaded").addScreenCaptureFromPath(screenPath);
        }

        driver.findElement(link_Register).click();
        if (driver.getCurrentUrl().contains("register")) {
            test.pass("Register page URL is correct: " + driver.getCurrentUrl());
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Incorrect_Register_URL");
            test.fail("Register page URL is incorrect").addScreenCaptureFromPath(screenPath);
        }

        WebElement actualTitle = driver.findElement(By.tagName("h1"));
        if (actualTitle.getText().equals("Register")) {
            test.pass("Register page title is correct and visible");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Verification_Failed_" + actualTitle.getText());
            test.fail("Register page title is incorrect").addScreenCaptureFromPath(screenPath);
        }
    }

    @Test
    public void verifyRegisterPageFieldsAreVisible() throws InterruptedException, IOException {
        driver.get("https://demo.nopcommerce.com");
        test = extent.createTest("Verify all registration fields are displayed");

        String expectedHomeTitle = "nopCommerce demo store";
        String actualHomeTitle = driver.getTitle();
        if (actualHomeTitle.contains(expectedHomeTitle)) {
            test.pass("Home page is opened successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "HomePage_NotLoaded");
            test.fail("Home page is not loaded").addScreenCaptureFromPath(screenPath);
        }

        driver.findElement(link_Register).click();
        if (driver.getCurrentUrl().contains("register")) {
            test.pass("Register page URL is correct: " + driver.getCurrentUrl());
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Incorrect_Register_URL");
            test.fail("Register page URL is incorrect").addScreenCaptureFromPath(screenPath);
        }

        WebElement actualTitle = driver.findElement(By.tagName("h1"));
        if (actualTitle.getText().equals("Register")) {
            test.pass("Register page title is correct and visible");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Verification_Failed_" + actualTitle.getText());
            test.fail("Register page title is incorrect").addScreenCaptureFromPath(screenPath);
        }

        boolean firstNameVisible = driver.findElement(input_FirstName).isDisplayed();
        boolean lastNameVisible = driver.findElement(input_LastName).isDisplayed();
        boolean emailVisible = driver.findElement(input_Email).isDisplayed();
        boolean passwordVisible = driver.findElement(input_Password).isDisplayed();
        boolean confirmPasswordVisible = driver.findElement(input_ConfirmPassword).isDisplayed();

        if (firstNameVisible && lastNameVisible && emailVisible && passwordVisible && confirmPasswordVisible) {
            test.pass("All registration fields were displayed");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Missing_Registration_Fields");
            test.fail("One or more registration fields were not displayed")
                .addScreenCaptureFromPath(screenPath);
        }
    }

    @Test
    public void testRegistrationWithInvalidData() throws InterruptedException, IOException {
        driver.get("https://demo.nopcommerce.com");
        test = extent.createTest("Verify registration with password mismatch and duplicate email");

        String expectedHomeTitle = "nopCommerce demo store";
        String actualHomeTitle = driver.getTitle();
        if (actualHomeTitle.contains(expectedHomeTitle)) {
            test.pass("Home page is opened successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "HomePage_NotLoaded");
            test.fail("Home page is not loaded").addScreenCaptureFromPath(screenPath);
        }

        driver.findElement(link_Register).click();
        if (driver.getCurrentUrl().contains("register")) {
            test.pass("Register page URL is correct: " + driver.getCurrentUrl());
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Incorrect_Register_URL");
            test.fail("Register page URL is incorrect").addScreenCaptureFromPath(screenPath);
        }

        driver.findElement(input_FirstName).sendKeys("Jane");
        driver.findElement(input_LastName).sendKeys("Smith");
        driver.findElement(input_Email).sendKeys("Deepu122@gmail");
        driver.findElement(input_Password).sendKeys("Abcd@1234");
        driver.findElement(input_ConfirmPassword).sendKeys("Xyz@1234");
        driver.findElement(btn_Register).click();

        WebElement actualTitle = driver.findElement(By.tagName("h1"));
        if (actualTitle.getText().equals("Register")) {
            test.pass("Register page title is correct and visible");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Verification_Failed_" + actualTitle.getText());
            test.fail("Register page title is incorrect").addScreenCaptureFromPath(screenPath);
        }

        boolean mismatchError = driver.getPageSource().contains("The password and confirmation password do not match.");
        if (mismatchError) {
            test.pass("Password mismatch validation is working.");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "PasswordMismatchFailed");
            test.fail("Password mismatch error not shown").addScreenCaptureFromPath(screenPath);
        }

        driver.navigate().refresh();

        driver.findElement(input_FirstName).sendKeys("Jane");
        driver.findElement(input_LastName).sendKeys("Smith");
        driver.findElement(input_Email).sendKeys("lakshmideepak@gmail.com");
        driver.findElement(input_Password).sendKeys("Deepu@123");
        driver.findElement(input_ConfirmPassword).sendKeys("Deepu@123");
        driver.findElement(btn_Register).click();

        boolean duplicateEmailError = driver.getPageSource().contains("The specified email already exists");
        if (duplicateEmailError) {
            test.pass("Duplicate email error is shown correctly.");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "DuplicateEmailFailed");
            test.fail("Duplicate email error not shown").addScreenCaptureFromPath(screenPath);
        }
    }

    @Test
    public void testRegistrationWithoutMandatoryFields() throws InterruptedException, IOException {
        driver.get("https://demo.nopcommerce.com");
        test = extent.createTest("Verify registration without filling mandatory fields");

        String expectedHomeTitle = "nopCommerce demo store";
        String actualHomeTitle = driver.getTitle();
        if (actualHomeTitle.contains(expectedHomeTitle)) {
            test.pass("Home page is opened successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "HomePage_NotLoaded");
            test.fail("Home page is not loaded").addScreenCaptureFromPath(screenPath);
        }

        driver.findElement(link_Register).click();
        if (driver.getCurrentUrl().contains("register")) {
            test.pass("Register page URL is correct: " + driver.getCurrentUrl());
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Incorrect_Register_URL");
            test.fail("Register page URL is incorrect").addScreenCaptureFromPath(screenPath);
        }

        WebElement actualTitle = driver.findElement(By.tagName("h1"));
        if (actualTitle.getText().equals("Register")) {
            test.pass("Register page title is correct and visible");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Verification_Failed_" + actualTitle.getText());
            test.fail("Register page title is incorrect").addScreenCaptureFromPath(screenPath);
        }

        driver.findElement(btn_Register).click();

        boolean firstNameError = driver.getPageSource().contains("First name is required.");
        boolean lastNameError = driver.getPageSource().contains("Last name is required.");
        boolean emailError = driver.getPageSource().contains("Email is required.");
        boolean passwordError = driver.getPageSource().contains("Password is required.");
        boolean confirmPasswordError = driver.getPageSource().contains("Password is required.");

        if (firstNameError && lastNameError && emailError && passwordError && confirmPasswordError) {
            test.pass("Validation messages appeared for all required fields.");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "MandatoryFieldsValidationFailed");
            test.fail("Missing validation messages for required fields.").addScreenCaptureFromPath(screenPath);
        }
    }

    @Test
    public void testValidRegistration() throws InterruptedException, IOException {
        driver.get("https://demo.nopcommerce.com");
        test = extent.createTest("Verify valid registration");

        String expectedHomeTitle = "nopCommerce demo store";
        String actualHomeTitle = driver.getTitle();
        if (actualHomeTitle.contains(expectedHomeTitle)) {
            test.pass("Home page is opened successfully");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "HomePage_NotLoaded");
            test.fail("Home page is not loaded").addScreenCaptureFromPath(screenPath);
        }

        driver.findElement(link_Register).click();
        if (driver.getCurrentUrl().contains("register")) {
            test.pass("Register page URL is correct: " + driver.getCurrentUrl());
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Incorrect_Register_URL");
            test.fail("Register page URL is incorrect").addScreenCaptureFromPath(screenPath);
        }

        WebElement actualTitle = driver.findElement(By.tagName("h1"));
        if (actualTitle.getText().equals("Register")) {
            test.pass("Register page title is correct and visible");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "Verification_Failed_" + actualTitle.getText());
            test.fail("Register page title is incorrect").addScreenCaptureFromPath(screenPath);
        }

        driver.findElement(radio_Male).click();
        driver.findElement(input_FirstName).sendKeys("Deepak");
        driver.findElement(input_LastName).sendKeys("N L");
        driver.findElement(input_Email).sendKeys("Lakshmideepak12@gmail.com");
        driver.findElement(input_Password).sendKeys("D@1234");
        driver.findElement(input_ConfirmPassword).sendKeys("D@1234");
        driver.findElement(btn_Register).click();

        boolean successMessage = driver.getPageSource().contains("Your registration completed");
        if (successMessage) {
            test.pass("User registered successfully with valid data");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "ValidRegistrationFailed");
            test.fail("Registration failed.").addScreenCaptureFromPath(screenPath);
        }
    }
}

