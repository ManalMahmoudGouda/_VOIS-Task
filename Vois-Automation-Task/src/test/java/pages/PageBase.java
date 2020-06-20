package pages;

import org.json.simple.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class PageBase {
    protected String URL;
    protected WebDriver driver;

    protected JSONObject elementLocatorsJson;

    public PageBase(WebDriver driver, String appURL) {
        this.driver = driver;
        this.URL = appURL;
        PageFactory.initElements(driver, this);
    }

    public PageBase(WebDriver driver) {
        this(driver, null);
    }

    /** Reads the element locator Information from Page JSON File and Initialize the Web Element
     * @param elementNameInJSON name of the element attribute in Page JSON Locators File
     * @return WebElement
     */
    protected WebElement findElementByJSON(String elementNameInJSON) {
        JSONObject searchBar = (JSONObject) this.elementLocatorsJson.get(elementNameInJSON);

        String locatorUsing = (String) searchBar.get("locatorUsing");
        String locatorValue = (String) searchBar.get("locatorValue");

        WebElement element = null;

        if (locatorUsing.equals("xpath"))
            element = driver.findElement(By.xpath(locatorValue));
        else if (locatorUsing.equals("id"))
            element = driver.findElement(By.id(locatorValue));

        return element;
    }

    protected List<WebElement> findElementsByJSON(String elementNameInJSON) {
        JSONObject searchBar = (JSONObject) this.elementLocatorsJson.get(elementNameInJSON);

        String locatorUsing = (String) searchBar.get("locatorUsing");
        String locatorValue = (String) searchBar.get("locatorValue");

        List<WebElement> element = null;

        if (locatorUsing.equals("xpath"))
            element = driver.findElements(By.xpath(locatorValue));
        else if (locatorUsing.equals("id"))
            element = driver.findElements(By.id(locatorValue));

        return element;
    }


    public void setInputValue(WebElement input, String value) {
        this.waitUntilVisible(input);
        input.sendKeys(value);
    }

    public void setInputValue(WebElement input, Keys key) {
        this.waitUntilVisible(input);
        input.sendKeys(key);
    }

    public void clickBtn(WebElement btn) {
        this.waitUntilVisible(btn);
        btn.click();
    }

    public void waitUntilVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, 2, 5000);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static int countSearchResult(List<WebElement> webElementList) {
        return webElementList.size();
    }

    public boolean IsElementTextContains(WebElement element, String value) {
        this.waitUntilVisible(element);
        return element.getText().contains(value);
    }

    public void scrollDownOfPage(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);

    }
}
