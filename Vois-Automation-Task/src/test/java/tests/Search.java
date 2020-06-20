package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import common.JSONReader;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.PageBase;
import pages.Vodafone.GoogleSearchPage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Search extends TestBase {
    protected JSONObject searchTestData;
    GoogleSearchPage googleSearchPage;
    ExtentHtmlReporter htmlReporter;
    ExtentReports extent;
    //helps to generate the logs in test report.
    ExtentTest logger;

    public Search() throws IOException, ParseException {
        searchTestData = (JSONObject) JSONReader.readJson("src/test/resources/test-data/test-data.json");

    }

    @BeforeTest
    public void startReport() {
        // initialize the HtmlReporter
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "/test-report/testReport.html");

        //initialize ExtentReports and attach the HtmlReporter
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);

        //To add system or environment info by using the setSystemInfo method.
        extent.setSystemInfo("OS", "Windows");
        extent.setSystemInfo("Browser", "Chrome");

        //configuration items to change the look and feel
        //add content, manage tests etc
        htmlReporter.config().setChartVisibilityOnOpen(true);
        htmlReporter.config().setDocumentTitle("Extent Report Demo");
        htmlReporter.config().setReportName("Test Report");
        htmlReporter.config().setTestViewChartLocation(ChartLocation.TOP);
        htmlReporter.config().setTheme(Theme.STANDARD);
        htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
    }

    @Test
    public void searchVodafone_TestCase() throws IOException, ParseException, InterruptedException {
        logger = extent.createTest("Google-Search", (String) searchTestData.get("testCaseDescription"));

        driver.navigate().to((String) jsonConfig.get("applicationURL"));
        //put in variable
        logger.log(Status.INFO, "Navigated to "+jsonConfig.get("applicationURL")+ " successfully ");
        // intailize google page
        googleSearchPage = new GoogleSearchPage(driver);

        String searchValue = (String) searchTestData.get("searchBarText");
        googleSearchPage.setInputValue(googleSearchPage.getSearchBar(), searchValue);
        googleSearchPage.setInputValue(googleSearchPage.getSearchBar(), Keys.ENTER);
        logger.log(Status.INFO, "Write " + searchValue + " in Search Bar");
        Assert.assertEquals(googleSearchPage.getSearchBar().getAttribute("value"),
                searchValue, searchValue + " isn't written in the Search Bar");
        logger.log(Status.PASS, "Write " + searchValue + " in Search Bar successfully");

        logger.log(Status.INFO, "Scroll Down until 'Next' link appear");
        googleSearchPage.scrollDownOfPage(googleSearchPage.getNextAnchor());
        googleSearchPage.waitUntilVisible(googleSearchPage.getNextAnchor());
        Assert.assertTrue(googleSearchPage.getNextAnchor().isEnabled(),"Next link not Clickable");
        logger.log(Status.PASS, "Scroll Down and next link appear successfully");

        logger.log(Status.INFO, "Click on Next Link to navigate to page 2");
        googleSearchPage.clickBtn(googleSearchPage.getNextAnchor());
        Assert.assertTrue(googleSearchPage.IsElementTextContains(googleSearchPage.getResultPageInfo(),
                (String) searchTestData.get("secondPage")), "Didn't navigate to page 2");
        logger.log(Status.PASS, "Navigate to Page 2 successfully");


        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        int countOfSecondPage = PageBase.countSearchResult(googleSearchPage.getSearchResultList());
        googleSearchPage.scrollDownOfPage(googleSearchPage.getNextAnchor());
        googleSearchPage.clickBtn(googleSearchPage.getNextAnchor());
        this.IsNextLinkEnabled();
        Assert.assertTrue(googleSearchPage.IsElementTextContains(googleSearchPage.getResultPageInfo(), (String) searchTestData.get("thirdPage")));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        int countOfThirdPage = PageBase.countSearchResult(googleSearchPage.getSearchResultList());
        Assert.assertEquals(countOfSecondPage, countOfThirdPage, (String) searchTestData.get("issueDescription"));

    }

    public void IsNextLinkEnabled() {
        if (googleSearchPage.getNextAnchor().isEnabled()) {
            logger.log(Status.PASS, "Scroll Down and click on the next link");
        } else {
            logger.log(Status.FAIL, "Failed to verfie");
        }
    }

    @AfterMethod
    public void getResult(ITestResult result) throws Exception {
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.fail(result.getThrowable().getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(getScreenshot(
                            "screenshot-" + result.getName())).build());
            logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " FAILED ", ExtentColor.RED));
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.log(Status.PASS, MarkupHelper.createLabel(result.getName() + " PASSED ", ExtentColor.GREEN));
        } else {
            logger.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " SKIPPED ", ExtentColor.ORANGE));
            logger.skip(result.getThrowable());
        }
    }

    private String getScreenshot(String screenshotName) throws Exception {
        //below line is just to append the date format with the screenshot name to avoid duplicate names
        String currentTimeStamp = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        String filename = screenshotName + currentTimeStamp + ".png";


        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        //after execution, you could see a folder "FailedTestsScreenshots" under src folder
        String destination = System.getProperty("user.dir") + "/test-report/" + filename;
        File finalDestination = new File(destination);
        FileUtils.copyFile(source, finalDestination);

        //Returns the captured file path
        return filename;
    }

    @AfterTest
    public void tearDown() {
        //to write or update test information to reporter
        extent.flush();
        //  driver.quit();
    }
}
