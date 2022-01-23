package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.netology.mode.DataHelper;
import ru.netology.web.page.LoginPage;

import java.sql.DriverManager;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

import static com.codeborne.selenide.Selenide.open;

public class AuthenticationTest {

    @BeforeEach
    @SneakyThrows
    void setUp() {
        var faker = new Faker();
        var dataSQL = "INSERT INTO users(id, login, password) VALUES (?, ?, ?);";

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app-db", "app", "mypass"
                );
                var dataStmt = conn.prepareStatement(dataSQL);
        ) {
            dataStmt.setString(1, faker.idNumber().valid());
            dataStmt.setString(2, faker.name().username());
            dataStmt.setString(3, faker.internet().password());
            dataStmt.executeUpdate();


        }
    }

    @AfterAll
    @SneakyThrows
    static void deletingDataFromTheDb() {
        var deleteFromAuthCodes = "DELETE FROM auth_codes;";
        var deleteFromCards = "DELETE FROM cards;";
        var deleteFromUsers = "DELETE FROM users;";

        try (
                var conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/app-db", "app", "mypass"
                );
                var deleteStmt = conn.createStatement();
        ) {

            var authCodes = deleteStmt.executeUpdate(deleteFromAuthCodes);
            var cards = deleteStmt.executeUpdate(deleteFromCards);
            var users = deleteStmt.executeUpdate(deleteFromUsers);
            System.out.println("delete from auth_codes" + authCodes + "\n" + "delete from cards" + cards + "\n" + "delete from users" + users);
        }

    }


    @Test
    public void shouldAuthorizationIsSuccessful() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999"); //Открыть приложение
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.VerificationCode.getAuthCode();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldGiveErrorIfPasswordInvalid() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999"); //Открыть приложение
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidAuthInfo();
        val pageWithNotification = loginPage.invalidLogin(authInfo);
        pageWithNotification.shouldBe(visible).shouldHave(text("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    public void shouldGiveLockIfAnInvalidPasswordIsEnteredThreeTimes() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999"); //Открыть приложение
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidAuthInfo();
        val pageWithNotification = loginPage.invalidPasswordThreeTimes(authInfo);
        pageWithNotification.shouldBe(visible).shouldHave(text("Ошибка! Превышено количество попыток ввода пароля"));

    }


}