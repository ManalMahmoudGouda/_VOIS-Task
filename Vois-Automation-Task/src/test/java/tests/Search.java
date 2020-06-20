package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import common.JSONReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.PageBase;
import pages.Vodafone.GoogleSearchPage;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Search extends TestBase {
    protected JSONObject searchTestData;
    GoogleSearchPage googleSearchPage;
    ExtentHtmlReporter htmlReporter;
    ExtentReports extent;
    //helps to generate the logs in test report.
    ExtentTest test;
    public Search() throws IOException, ParseException {
        searchTestData = (JSONObject) JSONReader.readJson("src/test/resources/test-data/test-data.json");

    }
    @BeforeTest
    public void startReport() {
        // initialize the HtmlReporter
        htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") +"/test-output/testReport.html");

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
    public void searchTC() throws IOException, ParseException {
        test = extent.createTest("Google-Search", (String) searchTestData.get("testCaseDescription"));

        driver.navigate().to((String) jsonConfig.get("applicationURL"));
        test.log(Status.INFO, "Navigated to Google.com successfully");
        googleSearchPage = new GoogleSearchPage(driver);

        googleSearchPage.setInputValue(googleSearchPage.getSearchBar(), (String) searchTestData.get("searchBarText"));
       googleSearchPage.setInputValue(googleSearchPage.getSearchBar(), Keys.ENTER);
        test.log(Status.INFO, "Write Vodafone in Search Bar");
        if(driver.getTitle().equals("Vodafone - Google Search"))
        {        test.log(Status.PASS, "Write Vodafone in Search Bar successfully");
        }
        else
        {
            test.log(Status.FAIL, "Failed to verfie");
        }

        googleSearchPage.scrollDownOfPage(googleSearchPage.getNextAnchor());
        googleSearchPage.waitUntilVisible(googleSearchPage.getNextAnchor());
        googleSearchPage.clickBtn(googleSearchPage.getNextAnchor());
        test.log(Status.INFO, "Scroll Down on the page ");
        this.IsNextLinkEnabled();

        Assert.assertTrue(googleSearchPage.IsElementTextContains(googleSearchPage.getResultPageInfo(),
        (String) searchTestData.get("secondPage")), "Page 2 isn't found");
        test.log(Status.PASS, "Navigate to Second Page");

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
public void IsNextLinkEnabled(){
    if(googleSearchPage.getNextAnchor().isEnabled())
    {        test.log(Status.PASS, "Scroll Down and click on the next link");
    }
    else
    {
        test.log(Status.FAIL, "Failed to verfie");
    }
}

    @AfterMethod
    public void getResult(ITestResult result) {
        if(result.getStatus() == ITestResult.FAILURE) {
            test.log(Status.FAIL, MarkupHelper.createLabel(result.getName()+" FAILED ", ExtentColor.RED));
            test.fail(result.getThrowable().getMessage());
        }
        else if(result.getStatus() == ITestResult.SUCCESS) {
            test.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" PASSED ", ExtentColor.GREEN));
        }
        else {
            test.log(Status.SKIP, MarkupHelper.createLabel(result.getName()+" SKIPPED ", ExtentColor.ORANGE));
            test.skip(result.getThrowable());
        }
    }
    @AfterTest
    public void tearDown() {
        //to write or update test information to reporter
        extent.flush();
      //  driver.quit();
    }
}
