package com.makersacademy.acebook.feature;

import com.github.javafaker.Faker;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignUpTest {

    WebDriver driver;
    Faker faker;

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        driver = new ChromeDriver();
        faker = new Faker();
    }

    @After
    public void tearDown() {
        driver.close();
    }

    @Test
    public void successfulSignUpAlsoLogsInUser() {
        String email = faker.name().username() + "@email.com";

        driver.get("http://localhost:8080/");
        driver.findElement(By.linkText("Log in")).click();
        // creating a new user so need to click the Sign up link
        driver.findElement(By.linkText("Sign up")).click();
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys("P@55qw0rd");
        driver.findElement(By.name("action")).click();
        // the page can take a few seconds to load so added a wait timer for consistency
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        // there are two buttons on the screen so selecting the one with the text 'Accept' and clicking it
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Accept']")));
        driver.findElement(By.xpath("//button[text()='Accept']")).click();
        // the final page can take a few seconds to load so another wait timer is needed
        WebElement greetingTextLocator = new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.visibilityOfElementLocated(By.id("greeting")));
        String actualGreetingText = greetingTextLocator.getText();
        Assert.assertEquals("Hello " + email, actualGreetingText);
    }
}
