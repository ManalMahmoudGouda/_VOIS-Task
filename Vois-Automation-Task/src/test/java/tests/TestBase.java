package tests;

import common.JSONReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;
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


        String browserToBeUsed = (String) jsonConfig.get("browserToBeUsed");
        switch (browserToBeUsed) {
            case "FF":
                System.setProperty("webdriver.gecko.driver", (String) jsonConfig.get("fireFoxDriverPath"));
                FirefoxProfile ffProfile = new FirefoxProfile();
                ffProfile.setPreference("javascript.enabled", false);

                FirefoxOptions ffOptions = new FirefoxOptions();
                ffOptions.setLegacy(true);
                ffOptions.setProfile(ffProfile);
                driver = new FirefoxDriver(ffOptions);
                break;
            case "CH":
                String driverPath = (String) jsonConfig.get("chromeDriverPath");
                System.setProperty("webdriver.chrome.driver", driverPath);

                Map<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("profile.default_content_setting_values.notifications", 2);

                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("prefs", prefs);
                options.addArguments("--lang=en-GB");
                driver = new ChromeDriver(options);
                break;
            case "IE":
                System.setProperty("webdriver.ie.driver", (String) jsonConfig.get("ieDriverPath"));

                InternetExplorerOptions ieOptions = new InternetExplorerOptions();
                ieOptions.disableNativeEvents();
                ieOptions.requireWindowFocus();
                driver = new InternetExplorerDriver(ieOptions);
        }

        driver.manage().window().maximize();
    }

    //Test cleanup
    @AfterClass
    public void TeardownTest() {

        // TestBase.driver.quit();
    }


}
