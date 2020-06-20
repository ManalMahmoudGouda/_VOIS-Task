package pages.Vodafone;

import common.JSONReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import pages.PageBase;

import javax.xml.xpath.XPath;
import java.io.IOException;
import java.util.List;

public class GoogleSearchPage extends PageBase {
    public GoogleSearchPage(WebDriver driver) throws IOException, ParseException {
        super(driver);
        this.elementLocatorsJson = (JSONObject) JSONReader.readJson("src/test/resources/page-locators/google-search-locators.json");
    }

    public WebElement getSearchBar() throws InterruptedException {
        return findElementByJSON("searchBar");
    }

    public WebElement getResultPageInfo() throws InterruptedException {
        return findElementByJSON("resultPageInfo");
    }

    public WebElement getNextAnchor() throws InterruptedException {
        return findElementByJSON("nextAnchor");
    }

    public List<WebElement> getSearchResultList(){
        return findElementsByJSON("searchResultList");
    }
}
