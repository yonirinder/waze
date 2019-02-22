package tests;

import entitys.RoadData;
import infra.SeleniumUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import pages.LiveMapPage;
import utils.MailClient;

import javax.mail.MessagingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;


public class WazeSearchTest {

    private WebDriver driver;
    private MailClient mail;
    private Properties prop = new Properties();


    @BeforeSuite
    public void init() throws IOException {
        InputStream ip = getClass().getClassLoader().getResourceAsStream("config.properties");
        prop.load(ip);
        mail = new MailClient(prop.getProperty("to"),
                prop.getProperty("from"), prop.getProperty("password"));
    }

    @Parameters({"browserType"})
    @BeforeMethod
    public void startBrowser(String browserType) {
        driver = new SeleniumUtils().getDriver(browserType);
    }

    @Test(testName = "Waze Search Test")
    public void compareWays() throws UnsupportedEncodingException, MessagingException {
        LiveMapPage liveMap = new LiveMapPage(driver);
        liveMap.search("Yigal Alon 94, Tel Aviv", "חיפה");
        liveMap.selectTime("07:00");
        List<RoadData> options7 = new ArrayList<>(liveMap.getRoadData());
        liveMap.selectTime("08:00");
        List<RoadData> options8 = new ArrayList<>(liveMap.getRoadData());
        liveMap.selectTime("09:00");
        List<RoadData> options9 = new ArrayList<>(liveMap.getRoadData());
        options7.addAll(options8);
        options7.addAll(options9);
        options7.sort(Comparator.comparing(RoadData::getTime).thenComparing(RoadData::getLength));
        String result = "Seven o'clock: " + options7 + "\n" +
                "Eight o'clock: " + options8 + "\n" +
                "nine o'clock: " + options9 + "\n" +
                "List ordered by quickest time, length: " + "\n" +
                options7.toString();
        mail.send("List of ways", result);
    }

    @Test(testName = "Compare Round Hours")
    public void compareRoundHours() throws UnsupportedEncodingException, MessagingException {
        LiveMapPage liveMap = new LiveMapPage(driver);
        liveMap.search("Yigal Alon 94, Tel Aviv", "חיפה");
        List<RoadData> options = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            liveMap.selectTime(String.format("%02d", i) + ":00");
            options.addAll(liveMap.getRoadData());
        }
        options.sort(Comparator.comparing(RoadData::getTime).thenComparing(RoadData::getLength));
        mail.send("Compare Round Hours Test",
                "Quickest time: " + options.get(0) + "\n" +
                        "Slowest time" + options.get(options.size() - 1) + "\n" +
                        "List ordered by quickest time, length: " + "\n" +
                        options.toString());
    }

    @AfterMethod
    public void closeBrowser() {
        driver.quit();

    }

}