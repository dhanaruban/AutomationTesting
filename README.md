# APPROACH FOR THE GIVEN SCENARIO

### GIVEN SCENARIO

when a Höffner login page [https://www.hoeffner.de/login] opens, a newsletter subscription input can be seen.

After entering the email in the input field,  "Absenden" button is clicked, a confirmation message displays that my subscription is in progress

I divided the scenario into two segments

1. Manual test cases for the given scenario
   
2. Automation Test Code for the given scenario


# Manual test cases for the given scenario

Let’s create a Test Case for the scenario: Newsletter Subscription Confirmation Message Functionality

### WorkFlow of Testing a Newsletter Subscription
Enter the necessary fields: Email ID

1. Validate the fields: Email

2. Verify the email ID: Send a confirmation mail to the entered Email ID after a successful validation.

3. Email Validation

Test for some invalid emails with cases like: without @, without(.), without domain, without alphabets before @. In such cases the user should be prompted to enter a valid email address.

Test for valid email addresses by entering valid email addresses and then click on absenden. User should get a confirmation message.

Test Cases are defined in the below excel link.
https://docs.google.com/spreadsheets/d/1Sd2Fxjm497jeZ51Sv1FCz2sdWRvDcr1BioVjgYsUNsA/edit?usp=sharing

# Automation Test Code for the given scenario

Tools Used: 

- Selenium (open-source) automated testing framework - used to validate web applications
- Java for writing tests in Selenium
- TestNG - TestNG is a testing framework designed to simplify a broad range of testing needs.

Data Driven testing is used to test the test cases.

# Why Data - Driven Testing

To ensure whether a particular functionality of the application is working correctly, we need to test with different sets of test data. For example, if we want to ensure the newsletter subscription functionality is working correctly, we need to provide all valid credentials as well as invalid credentials, and test. This ensures that the application is working as expected for all valid and invalid sets of test data.  

These test cases are stored in json file.

TestNG Data Provider approach is used to perform data driven testing.

This helps to write data-driven tests which essentially means that same test method can be run multiple times with different data-sets.

From my understanding,

NOTE: As the website is in german, I prefer following a generic format. 

I will store the input and output values as keys, I take the keys from the translator file. This will be in a json format. If the text changes, or the values of the keys changes, there is no need to modify the code we have to change only the values of the keys in the translator file. 
By doing this, the same test case can be performed without any confusion.

# CODE

I have written my Selenium automation code in Java programming language.

I created a Maven Project to execute this test.

To read data from json file, the following dependency have been added.

```html
<dependency>
      <groupId>com.googlecode.json-simple</groupId>
      <artifactId>json-simple</artifactId>
      <version>1.1.1</version>
	</dependency>
```
### Selenium Dependency
```html
<dependency>
      <groupId>org.seleniumhq.selenium</groupId>
      <artifactId>selenium-java</artifactId>
      <version>3.141.59</version>
    </dependency>
```

### To avoid browser related drivers
```html
<dependency>
      <groupId>io.github.bonigarcia</groupId>
      <artifactId>webdrivermanager</artifactId>
      <version>4.4.3</version>
	</dependency>
```
I added TestNG Library. It is developed to simplify a broad range of testing

I created the data file in json format as follows:

```json
{
	"email": [
		{
			"emailid":"example@gmail.com"
		},
		{
			"emailid":"sample@gmail.com"
		},
		{
			"emailid":"example.gmail.com"
		},
		{
			"emailid":"gmail.com"
		},
		{
			"emailid":"sample@com"
		}
	
	]
}
```
To control the flow of execution of test methods, TestNG Annotations has been used.

- @BeforeClass
  
  The @BeforeClass annotated method will be executed before the first method of the current class is invoked.
- @AfterClass

  The @AfterClass annotated method will be invoked after the execution of all the test methods of the current class.

- @Test

  The @Test annotated method will be invoked to execute the test methods.

- @DataProvider

  DataProviders in TestNG is used to pass the parameters in the test function

## Complete Code
```java
package Jsontest;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DDTestUsingJSON {
	
	WebDriver driver;
	
	@BeforeClass
	void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@AfterClass
	void tearDown()
	{
		driver.close();
	}
	
	@Test(dataProvider = "dp", alwaysRun = true, description = "User can get confirmation message after entering proper email address")
	void Subscribe(String data) throws InterruptedException
	{
        driver.get("https://www.hoeffner.de/login");
        driver.manage().window().maximize();
		driver.findElement(By.id("email")).sendKeys(data);
		driver.findElement(By.id("newsletterFormSubmitBtn")).submit();
		Thread.sleep(3000);
		if (driver.findElement(By.className("footerNewsletter__confirmation")).isDisplayed()) {
			System.out.println("For " + data + " Confirmation message is displayed");
		}
		else {
			System.out.println(data + " is an invalid email address");
		}
		
	}
	
	@DataProvider(name="dp")
	public String[] readJSON () throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		FileReader reader = new FileReader("JsonFiles/testdata.json");
		
		Object obj = jsonParser.parse(reader);
		
		JSONObject usersubscriptionJsonobj = (JSONObject) obj;
		JSONArray usersubscriptionArray = (JSONArray) usersubscriptionJsonobj.get("email");
		
		String arr[] = new String[usersubscriptionArray.size()];
		
		for(int i=0;i<usersubscriptionArray.size();i++) {
			JSONObject emails = (JSONObject) usersubscriptionArray.get(i);
			String email = (String) emails.get("emailid");
			
			arr[i]=email;
		}
		
		return arr;
	}
} 
```

## SCENARIO WHICH IS NOT COVERED

In the given scenario, the following actions are not covered.

*FUNCTIONALITY TESTING:*
1. Checking the email which has been received, after entering valid email address.
2. By confirming "Jetzt Anmeldung abschließen", redirection to the page confirming subscription [https://www.hoeffner.de/nl-anmeldung].

After proper validation, if we get a confirmation message after entering the valid email id, a email will be triggered. 

The test can be performed by validating from the SMTP (Simple Mail Transfer Protocol). As I don't have access to it, this cannot be implemented in this task. 

By creating a dummy SMTP server, we can send test emails.
		
*COMPATIBILTY TESTING (non-functional testing)*
1. To ensure your web page works on various operating systems, devices and applications, network environments, and with particular internal hardware specifications.

*MANUAL TESTING*
1. To verify the triggered email is as per the standard template requirement, it is not tested manually.







