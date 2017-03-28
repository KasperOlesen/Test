/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author kAlex
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CarTest1 {

    private static final int WAIT_MAX = 4;
    static WebDriver driver;

    @BeforeClass
    public static void setup() {
        /*########################### IMPORTANT ######################*/
        /*## Change this, according to your own OS and location of driver(s) ##*/
        /*############################################################*/
        System.setProperty("webdriver.gecko.driver", "C:\\drivers\\geckodriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver.exe");

        //Reset Database
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
        driver = new ChromeDriver();
        driver.get("http://localhost:3000");
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
        //Reset Database 
        com.jayway.restassured.RestAssured.given().get("http://localhost:3000/reset");
    }

    @Test
    //Verify that page is loaded and all expected data are visible
    public void test1() throws Exception {
        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement e = d.findElement(By.tagName("tbody"));
            List<WebElement> rows = e.findElements(By.tagName("tr"));
            Assert.assertThat(rows.size(), is(5));
            return true;
        });
    }

    @Test
    //Verify the filter functionality 
    public void test2() throws Exception {
        //No need to WAIT, since we are running test in a fixed order, we know the DOM is ready (because of the wait in test1)
        WebElement element = driver.findElement(By.id("filter"));
        element.sendKeys("2002");
        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        Assert.assertThat(rows.size(), is(2));
    }

    @Test
    //Clear text and verify 5 rows
    public void test3() throws Exception {
        tearDown();
        setup();
        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement e = d.findElement(By.tagName("tbody"));
            List<WebElement> rows = e.findElements(By.tagName("tr"));
            Assert.assertThat(rows.size(), is(5));
            return true;
        });
    }

    @Test
    public void test4() throws Exception {
        WebElement element = driver.findElement(By.id("h_year"));
        element.click();
        int firstRow = driver.findElements(By.xpath("//tbody/tr[1 and td = '938']")).size();
        int secondRow = driver.findElements(By.xpath("//tbody/tr[5 and td = '940']")).size();

        System.out.println(driver.findElements(By.xpath("//tbody/tr[5 and td = '940']")));

        Assert.assertThat(firstRow, is(1));
        Assert.assertThat(secondRow, is(1));
    }

    @Test
    public void test5() throws Exception {

        WebElement row = driver.findElement(By.xpath("//tbody/tr[td='938']"));
        WebElement a = row.findElements(By.tagName("a")).get(0);
        a.click();
        WebElement desc = driver.findElement(By.name("description"));
        desc.clear();
        desc.sendKeys("Cool car");

        WebElement submit = driver.findElement(By.id("save"));
        submit.click();

        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement newRow = d.findElement(By.xpath("//tbody/tr[td = '938']"));
            String b = newRow.findElements(By.tagName("td")).get(5).getText();
            Assert.assertEquals("Cool car", b);
            return true;
        });
    }

    @Test
    public void test6() throws Exception {
        WebElement newCar = driver.findElement(By.id("new"));
        newCar.click();
        WebElement submit = driver.findElement(By.id("save"));
        submit.click();

        WebElement e = driver.findElement(By.tagName("tbody"));
        List<WebElement> rows = e.findElements(By.tagName("tr"));
        Assert.assertTrue(driver.findElement(By.id("submiterr")).isDisplayed());
        Assert.assertEquals("All fields are required", driver.findElement(By.id("submiterr")).getText());
        Assert.assertThat(rows.size(), is(5));
    }

    @Test
    public void test7() throws Exception {
        WebElement newCar = driver.findElement(By.id("new"));
        newCar.click();

        WebElement year = driver.findElement(By.name("year"));
        WebElement reg = driver.findElement(By.name("registered"));
        WebElement make = driver.findElement(By.name("make"));
        WebElement model = driver.findElement(By.name("model"));
        WebElement desc = driver.findElement(By.name("description"));
        WebElement price = driver.findElement(By.name("price"));
        WebElement submit = driver.findElement(By.id("save"));

        year.sendKeys("2008");
        reg.sendKeys("2002-5-5");
        make.sendKeys("Kia");
        model.sendKeys("Rio");
        desc.sendKeys("As new");
        price.sendKeys("31000");

        submit.click();

        (new WebDriverWait(driver, WAIT_MAX)).until((ExpectedCondition<Boolean>) (WebDriver d) -> {
            WebElement e = d.findElement(By.tagName("tbody"));
            List<WebElement> rows = e.findElements(By.tagName("tr"));
            Assert.assertThat(rows.size(), is(6));
            return true;
        });
    }
}
