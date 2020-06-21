package tests;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pages.Vodafone.GoogleSearchPage;
import util.JSONReader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SearchTest extends TestBase {
    protected JSONObject searchTestData;

    public SearchTest() throws IOException, ParseException {
        searchTestData = (JSONObject) JSONReader.readJson("src/test/resources/test-data/test-data.json");
    }

    @Test
    public void searchVodafone_TestCase() throws IOException, ParseException {
        logger = extent.createTest("Google-SearchTest", (String) searchTestData.get("testCaseDescription"));
        String appURL = (String) jsonConfig.get("applicationURL");
        // Step 1 Navigate to Google.com
        driver.navigate().to(appURL);
        logger.log(Status.INFO, "Navigated to " + appURL + " successfully ");
        // initialize google page
        GoogleSearchPage googleSearchPage = new GoogleSearchPage(driver);
        // Step 2 write 'Vodafone' in search Bar
        String searchValue = (String) searchTestData.get("searchBarText");
        logger.log(Status.INFO, "Write " + searchValue + " in SearchTest Bar");
        googleSearchPage.setInputValue(googleSearchPage.getSearchBar(), searchValue);
        Assert.assertEquals(googleSearchPage.getSearchBar().getAttribute("value"),
                searchValue, searchValue + " isn't written in the SearchTest Bar");
        googleSearchPage.setInputValue(googleSearchPage.getSearchBar(), Keys.ENTER);
        logger.log(Status.PASS, "Write " + searchValue + " in SearchTest Bar successfully");
        // Step 3 Scroll Down and click on next link
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

        // Step 3 Count SearchTest result in page 2 and scroll down to click on the next link to navigate to page 3
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        int countOfSecondPage = googleSearchPage.getSearchResultList().size();
        logger.log(Status.INFO, "Scroll Down until 'Next' link appear");
        googleSearchPage.scrollDownOfPage(googleSearchPage.getNextAnchor());
        googleSearchPage.waitUntilVisible(googleSearchPage.getNextAnchor());
        Assert.assertTrue(googleSearchPage.getNextAnchor().isEnabled(), "Next link not Clickable");
        logger.log(Status.PASS, "Scroll Down and next link appear successfully");
        logger.log(Status.INFO, "Click on Next Link to navigate to page 3");
        googleSearchPage.clickBtn(googleSearchPage.getNextAnchor());
        Assert.assertTrue(googleSearchPage.IsElementTextContains(googleSearchPage.getResultPageInfo(), (String) searchTestData.get("thirdPage")), "Didn't navigate to page 3");
        logger.log(Status.PASS, "Navigate to Page 3 successfully");

        // Step 4 Count SearchTest results in page 3 and compare with the  count of SearchTest  results in page 2
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        int countOfThirdPage = googleSearchPage.getSearchResultList().size();
        Assert.assertEquals(countOfSecondPage, countOfThirdPage, (String) searchTestData.get("issueDescription"));

    }

    /**
     * Log the Test Case result in the Report, and In case of failure it take Screenshot with
     * the Step Assertion Message
     * @param result Test Case Result
     * @throws Exception
     */
    @AfterMethod
    public void evaluateTestCaseResult(ITestResult result) throws Exception {
        if (result.getStatus() == ITestResult.FAILURE) {
            logger.fail(result.getThrowable().getMessage(),
                    MediaEntityBuilder.createScreenCaptureFromPath(getScreenshot(
                            "screenshot-" + result.getName())).build());
            logger.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " FAILED ", ExtentColor.RED));
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            logger.pass(MarkupHelper.createLabel(result.getName() + " PASSED ", ExtentColor.GREEN));
        }
    }


}
