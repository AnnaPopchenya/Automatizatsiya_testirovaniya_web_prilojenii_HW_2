package org.example.pom.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class StudentTableRow {
    private WebElement rowElement;

    public StudentTableRow(WebElement rowElement) {
        this.rowElement = rowElement;
    }

    public void clickTrashIcon() {
        rowElement.findElement(By.cssSelector(".trash-icon")).click();
    }

    public void clickRestoreFromTrashIcon() {
        rowElement.findElement(By.cssSelector(".restore-icon")).click();
    }

    public String getStatus() {
        return rowElement.findElement(By.cssSelector(".status")).getText();
    }
}