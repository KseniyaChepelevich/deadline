package ru.netology;

import com.codeborne.selenide.Configuration;

import lombok.val;

import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.Test;

import ru.netology.mode.DataHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

public class AuthenticationTest {

    @AfterAll
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
        loginPage.invalidLogin(authInfo);
        loginPage.getNotificationAuth();
    }

    @Test
    public void shouldGiveLockIfAnInvalidPasswordIsEnteredThreeTimes() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999"); //Открыть приложение
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidAuthInfo();
        loginPage.invalidPasswordThreeTimes(authInfo);
        loginPage.getNotification();
    }
}