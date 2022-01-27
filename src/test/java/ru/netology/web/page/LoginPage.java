package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.mode.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement notification = $("[data-test-id=error-notification]");

    public void getNotificationAuth() {
        notification.shouldBe(visible).shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    public void getNotification() {
        notification.shouldBe(visible).shouldHave(text("Ошибка! Превышено количество попыток ввода пароля"));
    }

    public void fillingOutTheForm(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
    }

    public void invalidLogin(DataHelper.AuthInfo invalidInfo) {
        fillingOutTheForm(invalidInfo);
    }

    public void reEnteringAnInvalidPassword(DataHelper.AuthInfo invalidInfo) {
        String deleteString = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;
        passwordField.sendKeys(deleteString);
        passwordField.setValue(invalidInfo.getPassword());
        loginButton.click();
    }

    public void invalidPasswordThreeTimes(DataHelper.AuthInfo invalidInfo) {

        invalidLogin(invalidInfo);
        reEnteringAnInvalidPassword(invalidInfo);
        reEnteringAnInvalidPassword(invalidInfo);
    }

    public VerificationPage validLogin(DataHelper.AuthInfo validInfo) {
        fillingOutTheForm(validInfo);
        return new VerificationPage();
    }
}