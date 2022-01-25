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

    @AfterAll
    @SneakyThrows
    static void deletingDataFromTheDb() {
        DataHelper.DeleteInfo.deletingData();
    }

    @Test
    public void shouldAuthorizationIsSuccessful() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999"); //Открыть приложение
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.VerificationCode.getAuthCode(authInfo);
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