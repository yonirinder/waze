package pages;


import entitys.RoadData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;


public class LiveMapPage extends BasePage {

    private final static String url = "https://www.waze.com/livemap";
    private final static String title = "Free Driving Directions, Traffic Reports & GPS Navigation App by Waze";

    private By searchBox = By.xpath("//input[@placeholder='Search for an address']");
    private By getDirectionsBtn = By.id("gtm-poi-card-get-directions");
    private By whereFromSearchBox = By.xpath("//input[@placeholder='Where from?']");
    private By leaveAtSelect = By.tagName("select");
    private By timeText = By.className("wm-route-item__time");
    private By lengthText = By.className("wm-route-item__length");
    private By section = By.className("wm-route-item__title");
    private By loader = By.className("s-loading__spinner");


    public LiveMapPage(WebDriver driver) {
        super(driver, url, title);
        navigate();
        validateTitle();
    }

    public void search(String to, String from) {
        sendKeys(searchBox, to);
        click(searchBox);
        selectFirst();
        click(getDirectionsBtn);
        sendKeys(whereFromSearchBox, from);
        click(whereFromSearchBox);
        selectFirst();

    }

    public void selectTime(String time) {
        select(leaveAtSelect, "at", 0);
        select(leaveAtSelect, time, 1);
        waitForLoader(loader);

    }

    public List<RoadData> getRoadData() {
        List<RoadData> options = new ArrayList<>();
        for (int i = 0; i < getWebElements(section, 30).size(); i++) {
            String time = getText(timeText, i);
            String length = getText(lengthText, i);
            options.add(new RoadData(time, length));
        }
        return options;

    }

}



