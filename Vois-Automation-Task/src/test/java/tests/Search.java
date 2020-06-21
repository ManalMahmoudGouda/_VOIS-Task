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

    public Search() throws IOException, ParseException {
        searchTestData = (JSONObject) JSONReader.readJson("src/test/resources/test-data/test-data.json");

    }


    @Test
    public void searchVodafone_TestCase() throws IOException, ParseException, InterruptedException {
        logger = extent.createTest("Google-Search", (String) searchTestData.get("testCaseDescription"));

        driver.navigate().to((String) jsonConfig.get("applicationURL"));
        //put in variable
        logger.log(Status.INFO, "Navigated to " + jsonConfig.get("applicationURL") + " successfully ");
        // initialize google page
        googleSearchPage = new GoogleSearchPage(driver);

        String searchValue = (String) searchTestData.get("searchBarText");
        logger.log(Status.INFO, "Write " + searchValue + " in Search Bar");
        googleSearchPage.setInputValue(googleSearchPage.getSearchBar(), searchValue);
        Assert.assertEquals(googleSearchPage.getSearchBar().getAttribute("value"),
                searchValue, searchValue + " isn't written in the Search Bar");
        googleSearchPage.setInputValue(googleSearchPage.getSearchBar(), Keys.ENTER);
        logger.log(Status.PASS, "Write " + searchValue + " in Search Bar successfully");

        logger.log(Status.INFO, "Scroll Down until 'Next' link appear");
        googleSearchPage.scrollDownOfPage(googleSearchPage.getNextAnchor());
        googleSearchPage.waitUntilVisible(googleSearchPage.getNextAnchor());
        Assert.assertTrue(googleSearchPage.getNextAnchor().isEnabled(), "Next link not Clickable");
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
        Assert.assertTrue(googleSearchPage.IsElementTextContains(googleSearchPage.getResultPageInfo(), (String) searchTestData.get("thirdPage")));
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        int countOfThirdPage = PageBase.countSearchResult(googleSearchPage.getSearchResultList());
        Assert.assertEquals(countOfSecondPage, countOfThirdPage, (String) searchTestData.get("issueDescription"));

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
        }
    }


}
