package pages;

import org.json.simple.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
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
    protected WebElement findElementByJSON(String elementNameInJSON){
        JSONObject searchBar = (JSONObject) this.elementLocatorsJson.get(elementNameInJSON);

        String locatorUsing = (String) searchBar.get("locatorUsing");
        String locatorValue = (String) searchBar.get("locatorValue");

        WebElement element = null;
        Wait wait = new WebDriverWait(driver, 3000);
        if (locatorUsing.equals("xpath")) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatorValue)));
            element = driver.findElement(By.xpath(locatorValue));
        } else if (locatorUsing.equals("id")) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(locatorValue)));
            element = driver.findElement(By.id(locatorValue));
        }

        return element;
    }

    /** Reads the element locator Information from Page JSON File and Initialize the Web Element List
     * @param elementNameInJSON name of the element attribute in Page JSON Locators File
     * @return WebElement
     */
    protected List<WebElement> findElementsByJSON(String elementNameInJSON) {
        JSONObject searchBar = (JSONObject) this.elementLocatorsJson.get(elementNameInJSON);

        String locatorUsing = (String) searchBar.get("locatorUsing");
        String locatorValue = (String) searchBar.get("locatorValue");

        List<WebElement> element = null;

        Wait wait = new WebDriverWait(driver, 3000);
        if (locatorUsing.equals("xpath")) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locatorValue)));
            element = driver.findElements(By.xpath(locatorValue));
        } else if (locatorUsing.equals("id")) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id(locatorValue)));
            element = driver.findElements(By.id(locatorValue));
        }

        return element;
    }

    /**
     * Waits Until the  Text Box appear and Enter value passed in the parameter
     * @param input WebElement (Text Input/Text Area)
     * @param value text
     */
    public void setInputValue(WebElement input, String value) {
        this.waitUntilVisible(input);
        input.sendKeys(value);
    }

    /**
     * It waits until the Text Box appear and Press Key passed in the parameter
     * @param element WebElement
     * @param key Key to be pressed
     */
    public void setInputValue(WebElement element, Keys key) {
        this.waitUntilVisible(element);
        element.sendKeys(key);
    }

    /**
     * It waits until the Buttons appear, then Click on it
     * @param btn
     */
    public void clickBtn(WebElement btn) {
        this.waitUntilVisible(btn);
        btn.click();
    }

    /**
     * Waits until the element is visible
     * @param element
     */
    public void waitUntilVisible(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, 2, 5000);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Checks if element Text contains value or not
     * @param element WebElement to be searched in
     * @param value String text to be searched
     * @return boolean is the element contains the value or not
     */
    public boolean IsElementTextContains(WebElement element, String value) {
        this.waitUntilVisible(element);
        return element.getText().contains(value);
    }

    /**
     * Scrolls down in the Page until reaching the input element
     * @param element WebElement to be reached while scrolling
     */
    public void scrollDownOfPage(WebElement element) {
        waitUntilVisible(element);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);

    }
}
