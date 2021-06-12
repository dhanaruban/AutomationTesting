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
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DDTestUsingJSON {
	
	WebDriver driver;
	
	@SuppressWarnings("deprecation")
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
