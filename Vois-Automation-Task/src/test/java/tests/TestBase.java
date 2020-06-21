package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import common.JSONReader;
import org.apache.commons.io.FileUtils;
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
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class TestBase {
    public static WebDriver driver = null;
    protected JSONObject jsonConfig;
    private ExtentHtmlReporter htmlReporter;
    protected ExtentReports extent;
    //helps to generate the logs in test report.
    protected ExtentTest logger;

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
                // ffProfile.setPreference("javascript.enabled", false);
                ffProfile.setPreference("intl.accept_languages", "en-GB");

                FirefoxOptions ffOptions = new FirefoxOptions();
//                ffOptions.setLegacy(true);
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
        this.startReport();
    }

    private void startReport() {
        // initialize the HtmlReporter
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")
                + jsonConfig.get("extentReportPath") + "testReport.html");

        //initialize ExtentReports and attach the HtmlReporter
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        //To add system or environment info by using the setSystemInfo method.
        extent.setSystemInfo("OS", (String) jsonConfig.get("osValue"));
        switch ((String) jsonConfig.get("browserToBeUsed")) {
            case "FF":
                extent.setSystemInfo("Browser", "Firefox");
                break;
            case "CH":
                extent.setSystemInfo("Browser", "Chrome");
                break;
            case "IE":
                extent.setSystemInfo("Browser", "Internet Explorer");
                break;
        }

        //configuration items to change the look and feel
        //add content, manage tests etc
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setDocumentTitle("Test Report");
        htmlReporter.config().setReportName("Test Report");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
    }

    protected String getScreenshot(String screenshotName) throws Exception {
        //below line is just to append the date format with the screenshot name to avoid duplicate names
        String currentTimeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String filename = screenshotName + currentTimeStamp + ".png";


        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        //after execution, you could see a folder "FailedTestsScreenshots" under src folder
        String destination = System.getProperty("user.dir") + jsonConfig.get("extentReportPath") + filename;
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);

        //Returns the captured file path
        return filename;
    }

    //Test cleanup
    @AfterClass
    public void TeardownTest() {
        extent.flush();
        driver.quit();
    }
}
