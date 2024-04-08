package org.example.tests;


import org.example.pom.LoginPage;
import org.example.pom.MainPage;
import org.example.pom.elements.StudentTableRow;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeekBrainStandTests {


    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private MainPage mainPage;
    private static String USERNAME;
    private static String PASSWORD;


    @BeforeAll
    public static void setupClass() {
        //Помещаем в переменные окружения путь до драйвера
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        USERNAME = "GB202310790786";
        PASSWORD = "559bf6f5d1";


    }

    @BeforeEach
    public void setupTest() {
        //Создаем экземпляр драйвера
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        //Расстягиваем окно браузеоа на весь экран
        driver.manage().window().maximize();
        // навигация на https://test-stand.gb.ru/login
        driver.get("https://test-stand.gb.ru/login");
        //Объект созданного Page Object
        loginPage = new LoginPage(driver, wait);

    }

    @Test
    public void testAddingGroupOnMainPage() {
        //Логин в систему с помощью метода из класса Page Object
        loginPage.login(USERNAME, PASSWORD);
        //Инициализация объекта класса MainPage
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        //Создание группы. Даем ей имя, чтобы в каждом запуске была проверка нового имени
        String groupTestName = "New Test Group" + System.currentTimeMillis();
        mainPage.createGroup(groupTestName);
        // Проверка, что группа создана и находится в таблице
        assertTrue(mainPage.waitAndGetGroupTitleByText(groupTestName).isDisplayed());
    }

    @Test
    public void testLoginWithoutUsernameAndPassword() {
        // Выполняем вход без ввода логина и пароля
        loginPage.login("", "");

        String expectedErrorMessage = "401\nInvalid credentials."; // Ожидаемое сообщение об ошибке
        String actualErrorMessage = loginPage.getErrorMessageText(); // Получаем фактическое сообщение об ошибке

        // Проверяем, совпадает ли сообщение об ошибке с ожидаемым сообщением об ошибке
        Assertions.assertEquals(expectedErrorMessage, actualErrorMessage, "Сообщение об ошибке не соответствует ожидаемому.");
    }

    @Test
    public void changeStudentCountInGroupTest() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);

        String groupName = "Test Group";
        mainPage.createGroup(groupName);

        mainPage.addStudentsToGroup(groupName, 2); // Добавляем 2 студента в группу

        int initialStudentCount = mainPage.getStudentsInGroupTableCount(groupName);

        mainPage.clickExpandIconInGroupRow(groupName); // Открываем таблицу студентов в группе

        int studentsInGroupTableCount = mainPage.getStudentsInGroupTableCount(groupName);

        // Проверяем, что количество студентов в таблице изменилось
        Assertions.assertEquals(initialStudentCount + 2, studentsInGroupTableCount, "Количество студентов не изменилось");

        // Выполняем дополнительные действия с кастомными элементами строки студента
        StudentTableRow studentRow = mainPage.getRowByTitle(groupName);
        studentRow.clickTrashIcon(); // Клик на иконку корзины
        String initialStatus = studentRow.getStatus();

        studentRow.clickRestoreFromTrashIcon(); // Клик на иконку восстановления
        String updatedStatus = studentRow.getStatus();

        // Проверяем, что статус студента изменился
        Assertions.assertNotEquals(initialStatus, updatedStatus, "Статус студента не изменился");


        driver.quit();


    }
}


