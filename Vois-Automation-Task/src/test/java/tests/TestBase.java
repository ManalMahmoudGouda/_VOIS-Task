package tests;

import common.JSONReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class TestBase {
    public static WebDriver driver = null;
    protected JSONObject jsonConfig;

    public TestBase() {
    }

    @BeforeClass
    public void initialize() throws IOException, ParseException {
        jsonConfig = (JSONObject) JSONReader.readJson("src/test/resources/config.json");
        String driverPath = (String) jsonConfig.get("chromeDriverPath");
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        // Create object of ChromeOption class
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--lang=en-GB");
        System.setProperty("webdriver.chrome.driver", driverPath);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
    }

    //Test cleanup
    @AfterClass
    public void TeardownTest() {

        // TestBase.driver.quit();
    }


}
