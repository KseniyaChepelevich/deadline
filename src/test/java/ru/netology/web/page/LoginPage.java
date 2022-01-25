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

    public SelenideElement invalidLogin(DataHelper.AuthInfo invalidInfo) {
        loginField.setValue(invalidInfo.getLogin());
        passwordField.setValue(invalidInfo.getPassword());
        loginButton.click();
        return notification.shouldBe(visible).shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    public SelenideElement reEnteringAnInvalidPassword(DataHelper.AuthInfo invalidInfo) {
        String deleteString = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;
        passwordField.sendKeys(deleteString);
        passwordField.setValue(invalidInfo.getPassword());
        loginButton.click();
        return notification.shouldBe(visible).shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    public SelenideElement invalidPasswordThreeTimes(DataHelper.AuthInfo invalidInfo) {
        String deleteString = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;
        invalidLogin(invalidInfo);
        reEnteringAnInvalidPassword(invalidInfo);
        reEnteringAnInvalidPassword(invalidInfo);
        return notification.shouldBe(visible).shouldHave(text("Ошибка! Превышено количество попыток ввода пароля"));
    }

    public VerificationPage validLogin(DataHelper.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }
}