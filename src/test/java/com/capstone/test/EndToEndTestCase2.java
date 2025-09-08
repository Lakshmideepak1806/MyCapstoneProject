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

public class EndToEndTestCase2 extends BaseTest {
	static String projectpath = System.getProperty("user.dir");
    WebDriver driver;
    WebDriverWait wait;
    // Locators
    By input_Email = By.id("Email");
    By input_Password = By.id("Password");
    By btn_Login = By.xpath("//button[contains(@class,'login-button')]");
    By loginLink = By.className("ico-login");
    By searchBox = By.id("small-searchterms");
    By searchButton = By.xpath("//button[contains(@class,'search-box-button')]");
    By addToCartBtn = By.xpath("(//button[contains(@class,'product-box-add-to-cart-button')])[1]");
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
    @Test(dataProvider="logindata")
    public void testLoginSearchAddToCartLogout(String username,String password,String search) throws Exception {
        driver.get("https://demo.nopcommerce.com");
        test = extent.createTest("TC_E2E_002 - Login > Search > Add to Cart > Logout");
        // Click on Login
        WebElement login = wait.until(ExpectedConditions.elementToBeClickable(loginLink));
        if (login.isDisplayed()) {
            login.click();
            test.pass("Login link clicked successfully.");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "LoginLink_NotVisible");
            test.fail("Login link is not visible").addScreenCaptureFromPath(screenPath);
        }
        // Check if email and password fields are visible
        if (driver.findElement(input_Email).isDisplayed() &&
            driver.findElement(input_Password).isDisplayed()) {
            driver.findElement(input_Email).sendKeys(username);
            driver.findElement(input_Password).sendKeys(password);
            test.pass("Email and Password fields are visible and data entered.");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "LoginFields_NotVisible");
            test.fail("Email or Password field is not visible").addScreenCaptureFromPath(screenPath);
        }
        // Click Login button
        driver.findElement(btn_Login).click();
        // Verify successful login by checking logout link
        WebElement logout = wait.until(ExpectedConditions.visibilityOfElementLocated(logoutLink));
        if (logout.isDisplayed()) {
            test.pass("User logged in successfully. Logout link is visible.");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "LoginFailed");
            test.fail("Login failed - Logout link not visible").addScreenCaptureFromPath(screenPath);
        }
        // Search for "Laptop"
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        if (searchInput.isDisplayed()) {
            searchInput.clear();
            searchInput.sendKeys("Laptop");
            driver.findElement(searchButton).click();
            test.pass("Search for 'Laptop' initiated.");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "SearchBox_NotVisible");
            test.fail("Search box is not visible").addScreenCaptureFromPath(screenPath);
        }
        // Validate Search Results
        String pageTitle = driver.getTitle().toLowerCase();
        if (pageTitle.contains("laptop") || driver.getPageSource().toLowerCase().contains(search)) {
            test.pass("Search results for 'Laptop' displayed successfully.");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "SearchResults_Incorrect");
            test.fail("Search results do not seem relevant to 'Laptop'").addScreenCaptureFromPath(screenPath);
        }
        // Add to Cart              
        WebElement addToCart = wait.until(ExpectedConditions.elementToBeClickable(addToCartBtn));
        Thread.sleep(3000);
        if (addToCart.isDisplayed()) {
            addToCart.click();
            test.pass("Add to Cart button clicked for first laptop product.");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "AddToCart_NotVisible");
            test.fail("Add to Cart button is not visible").addScreenCaptureFromPath(screenPath);
        }
        // Validate Product Added Notification
        WebElement notification = wait.until(ExpectedConditions.visibilityOfElementLocated(notificationBar));
        Thread.sleep(3000);
        if (notification.getText().toLowerCase().contains("the product has been added")) {
            test.pass("Product added to cart successfully.");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "AddToCart_Failed");
            test.fail("Product was not added to cart").addScreenCaptureFromPath(screenPath);
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(notificationBar));
        test.info("Notification bar is no longer visible.");
        // Logout
        Thread.sleep(3000);
        WebElement logoutLinkElement = driver.findElement(logoutLink);
        if (logoutLinkElement.isDisplayed()) {
            logoutLinkElement.click();
            test.pass("Logout link clicked.");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "LogoutLink_NotVisible");
           
            test.fail("logout link not clicked").addScreenCaptureFromPath(screenPath);
        }
        WebElement loginAgain = wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton));
        if (loginAgain.isDisplayed()) {
            test.pass("User logged out successfully. Login link is visible.");
        } else {
            String screenPath = ScreenshotUtiles.Capture(driver, "LogoutFailed");
            test.fail("Logout failed - Login link not visible").addScreenCaptureFromPath(screenPath);
        }
    }
    @DataProvider
    public Object[][] logindata() throws IOException {
        String excelpath = projectpath + "\\src\\test\\resources\\Testdata\\data.xlsx";
        return ExcelUtiles.getData(excelpath, "Sheet1");
    }

}
