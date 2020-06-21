## VOIS Automation Task
Testing the number of Search Results in Google in Page 2 is equal to in Page 3 using Selenium Automation, It's fully configurable through JSON Configuration, and supports Cross Broswer "Chrome", "Firefox", and "Internet Explorer

### Project Structure:
1. **pages:** contains all Classes of Application Pages, Each Class has the elements of the Page located by (xpath, id, ... so on)
2. **tests:** contains all Classes contains Test Cases of the Application
3. **util:** contains all Helper Classes

### Parent Classes:
1. **TestBase:** is the Parent of all Test Classes, contains @BeforeClass & @AfterClass, responsible for initializing the Driver and Report
2. **PageBase:** is the Parent of all Page Classes, contains general functions for operations on the page 

### Configuration Files: *(src/test/resources)*
1. **config.json**: contains general configuration related to the current environment
2. **test-data/test-data.json:** contains the test data values of the test case
3. **page-locators/google-search-locators.json:** contains the page locators of the page

### How the Solution Starts:
1. Adjust the right configurations in src/test/resources/config.json to be compatible with running environment
2. Run the Test Class "SearchTest"
3. After Running Test Report is generated in [Project-Path]/test-report/testReport.html
