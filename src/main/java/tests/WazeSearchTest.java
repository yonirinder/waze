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
import java.util.*;


public class WazeSearchTest {

    private static HashMap<Long, WebDriver> webDrivers = new HashMap<>();
    private MailClient mail;
    private Properties prop = new Properties();
    private static List<RoadData> result = new ArrayList<>();


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
        webDrivers.put(Thread.currentThread().getId(), new SeleniumUtils().getDriver(browserType));
    }

    @Test(testName = "Waze Search Test")
    public void compareWays() throws UnsupportedEncodingException, MessagingException {
        LiveMapPage liveMap = new LiveMapPage(webDrivers.get(Thread.currentThread().getId()));
        liveMap.search("Yigal Alon 94, Tel Aviv", "HaTa'arucha Street");
        liveMap.selectTime("07:00");
        List<RoadData> options7 = new ArrayList<>(liveMap.getRoadData());
        liveMap.selectTime("08:00");
        List<RoadData> options8 = new ArrayList<>(liveMap.getRoadData());
        liveMap.selectTime("09:00");
        List<RoadData> options9 = new ArrayList<>(liveMap.getRoadData());
        List<RoadData> fullResult = new ArrayList<>();
        fullResult.addAll(options7);
        fullResult.addAll(options8);
        fullResult.addAll(options9);
        fullResult.sort(Comparator.comparing(RoadData::getTime).thenComparing(RoadData::getLength));
        String resultStr = "Seven o'clock: " + options7 + "\n" +
                "Eight o'clock: " + options8 + "\n" +
                "nine o'clock: " + options9 + "\n" +
                "List ordered by quickest time, length: " + "\n" +
                fullResult.toString();
        mail.send("List of ways", resultStr);
    }

    @Test(testName = "Compare Round Hours 0-12")
    public void compareRoundHours0To12() {
        LiveMapPage liveMap = new LiveMapPage(webDrivers.get(Thread.currentThread().getId()));
        liveMap.search("Yigal Alon 94, Tel Aviv", "HaTa'arucha Street");
        for (int i = 0; i < 13; i++) {
            liveMap.selectTime(String.format("%02d", i) + ":00");
            result.addAll(liveMap.getRoadData());
        }
    }

    @Test(testName = "Compare Round Hours 12-24")
    public void compareRoundHours12To24() {
        LiveMapPage liveMap = new LiveMapPage(webDrivers.get(Thread.currentThread().getId()));
        liveMap.search("Yigal Alon 94, Tel Aviv", "HaTa'arucha Street");
        for (int i = 13; i < 24; i++) {
            liveMap.selectTime(String.format("%02d", i) + ":00");
            result.addAll(liveMap.getRoadData());
        }
    }


    @AfterMethod
    public void closeBrowser() {
        webDrivers.get(Thread.currentThread().getId()).quit();

    }

    @AfterSuite
    private void sendResult() throws MessagingException, UnsupportedEncodingException {
        result.sort(Comparator.comparing(RoadData::getTime).thenComparing(RoadData::getLength));
        mail.send("Compare Round Hours Test",
                "Quickest time: " + result.get(0) + "\n" +
                        "Slowest time" + result.get(result.size() - 1) + "\n" +
                        "List ordered by quickest time, length: " + "\n" +
                        result.toString());
    }

}