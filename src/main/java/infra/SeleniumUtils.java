package infra;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;


public class SeleniumUtils {
    private WebDriver driver;
    private static final String path = System.getProperty("user.dir") + File.separator + "drivers" + File.separator;


    public WebDriver getDriver(String browserType) {
        driver = setBrowser(browserType);
        driver.manage().window().maximize();
        return driver;
    }


    private WebDriver setBrowser(String browserType) {
        System.out.println("Starting web browser switch: " + browserType);
        if ("FF".equals(browserType)) {
            System.setProperty("webdriver.gecko.driver", path + "geckodriver.exe");
            driver = new FirefoxDriver();
        } else if ("CHROME".equals(browserType)) {
            System.out.println("Starting CHROME web driver");
            System.setProperty("webdriver.chrome.driver", path + "chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("test-type");
            options.addArguments("--incognito");
            driver = new ChromeDriver(options);
        }
        System.out.println(browserType + " web driver started successfully");
        return driver;

    }


}