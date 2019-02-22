package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;


abstract class BasePage {

    private WebDriver driver;
    private String url;
    private String title;


    BasePage(WebDriver driver, String url, String title) {
        this.driver = driver;
        this.url = url;
        this.title = title;
    }

    void navigate() {
        driver.get(this.url);
    }

    void validateTitle() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 60);
            wait.until(ExpectedConditions.titleIs(title));
        } catch (TimeoutException e) {
            Assert.fail(String.format("Title should be %s, Browser on %S", title, driver.getTitle()), e);
        }
    }

    void click(By loc) {
        WebElement element = getWebElement(loc, 50, ExpectedConditions.elementToBeClickable(loc));
        element.click();
    }

    void sendKeys(By loc, String text) {
        WebElement element = getWebElement(loc, 30, ExpectedConditions.elementToBeClickable(loc));
        element.clear();
        element.sendKeys(text);
    }

    String getText(By loc, int index) {
        WebElement element = getWebElements(
                loc, 40).get(index);
        return element.getText();
    }

    void selectFirst() {
        By loc = By.tagName("li");
        getWebElement(loc, 30, ExpectedConditions.elementToBeClickable(By.tagName("li"))).click();
    }

    void select(By loc, String value, int index) {
        WebElement element = getWebElements(
                loc, 30).get(index);
        element.click();
        new Select(element).selectByValue(value);
    }

    void waitForLoader(By loc) {
        try {
            WebDriverWait element = new WebDriverWait(driver, 2);
            element.until(ExpectedConditions.visibilityOfElementLocated(loc));
            WebDriverWait wait = new WebDriverWait(driver, 3);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(loc));
        } catch (TimeoutException ignored) {

        }
    }

    private WebElement getWebElement(By loc, int timeOut, ExpectedCondition<WebElement> expectedCon) {
        waitForElement(loc, timeOut, expectedCon);
        return driver.findElement(loc);
    }

    List<WebElement> getWebElements(By loc, int timeOut) {
        waitForElement(loc, timeOut, ExpectedConditions.visibilityOfElementLocated(loc));
        return driver.findElements(loc);
    }


    private void waitForElement(By loc, int timeOut, ExpectedCondition<WebElement> expectedCon) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, timeOut);
            wait.until(expectedCon);
        } catch (TimeoutException e) {
            Assert.fail("Element Not Found For Locator: " + loc.toString(), e);
        }
    }

}
