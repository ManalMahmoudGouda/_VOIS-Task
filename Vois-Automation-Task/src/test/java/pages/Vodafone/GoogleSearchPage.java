package pages.Vodafone;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.PageBase;
import util.JSONReader;

import java.io.IOException;
import java.util.List;

public class GoogleSearchPage extends PageBase {
    public GoogleSearchPage(WebDriver driver) throws IOException, ParseException {
        super(driver);
        this.elementLocatorsJson = (JSONObject) JSONReader.readJson("src/test/resources/page-locators/google-search-locators.json");
    }

    public WebElement getSearchBar(){
        return findElementByJSON("searchBar");
    }

    public WebElement getResultPageInfo(){
        return findElementByJSON("resultPageInfo");
    }

    public WebElement getNextAnchor() {
        return findElementByJSON("nextAnchor");
    }

    public List<WebElement> getSearchResultList(){

        return findElementsByJSON("searchResultList");
    }
}
