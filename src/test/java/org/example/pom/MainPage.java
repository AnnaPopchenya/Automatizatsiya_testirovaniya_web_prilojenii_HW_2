package org.example.pom;


import org.example.pom.elements.StudentTableRow;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


public class MainPage {

    private final WebDriver driver;

    private final WebDriverWait wait;

    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private WebElement usernameLinkInNavBar;
    @FindBy(id = "create-btn")
    private WebElement createGroupButton;
    @FindBy(xpath = "//form//span[contains(text(), 'Group name')]/following-sibling::input")
    private WebElement groupNameField;
    @FindBy(css = "form div.submit button")
    private WebElement submitButtonOnModalWindow;

    public MainPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public WebElement waitAndGetGroupTitleByText(String title) {
        String xpath = String.format("//table[@aria-label='Tutors list']/tbody//td[text()='%s']", title);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public void createGroup(String groupName) {
        wait.until(ExpectedConditions.visibilityOf(createGroupButton)).click();
        wait.until(ExpectedConditions.visibilityOf(groupNameField)).sendKeys(groupName);
        submitButtonOnModalWindow.click();
        waitAndGetGroupTitleByText(groupName);
    }

    public String getUsernameLabelText() {
        return wait.until(ExpectedConditions.visibilityOf(usernameLinkInNavBar))
                .getText().replace("\n", " ");
    }

    public void addStudentsToGroup(String groupName, int count) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".mdc-icon-button")));
        addButton.click();

        // Ожидаем появления модального окна для добавления студентов
        WebDriverWait modalWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        modalWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("")));

        // Находим поле для ввода имени студента и вводим имя
        WebElement nameInput = driver.findElement(By.cssSelector(""));
        nameInput.sendKeys("Имя студента");

        // Нажимаем кнопку "Добавить студента"
        WebElement saveButton = driver.findElement(By.cssSelector(".mdc-button__label"));
        saveButton.click();

        // Повторяем процесс count раз (если нужно добавить несколько студентов)
        for (int i = 1; i < count; i++) {
            // Добавляем еще одного студента, повторяя те же шаги
            // Например, можно очистить поля ввода и ввести другие данные для каждого нового студента
            nameInput.clear();
            nameInput.sendKeys("Имя студента " + i);

            addButton.click();
        }

        // Закрываем модальное окно после добавления всех студентов
        WebElement closeButton = driver.findElement(By.cssSelector(".mdc-dialog .mdc-dialog__close "));
        closeButton.click();
    }

    public void clickExpandIconInGroupRow(String groupName) {
        // Находим иконку увеличения в строке группы по имени
        WebElement expandIcon = driver.findElement(By.cssSelector("tr.group-row[data-group-name='" + groupName + "'] .expand-icon"));
        // Кликаем на иконку увеличения
        expandIcon.click();
    }

    public StudentTableRow getRowByTitle(String title) {
        // Находим элемент с заданным заголовком и находим его родительскую строку
        WebElement rowElement = waitAndGetGroupTitleByText(title).findElement(By.xpath("./ancestor::tr"));
        // Создаем объект строки таблицы студентов и возвращаем его
        return new StudentTableRow(rowElement);
    }

    public int getStudentsInGroupTableCount(String groupName) {
        // Находим все строки студентов в таблице группы по имени
        List<WebElement> studentRows = driver.findElements(By.cssSelector("tr.student-row[data-group-name='" + groupName + "']"));
        // Возвращаем количество найденных строк
        return studentRows.size();
    }

}




