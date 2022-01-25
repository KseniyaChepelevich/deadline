package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.mode.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");

    public VerificationPage() {
        codeField.shouldBe(visible, Duration.ofSeconds(60));
    }

    public DashboardPage validVerify(String code) {
        val authInfo = DataHelper.getAuthInfo();
        codeField.shouldBe(visible, Duration.ofSeconds(60));
        codeField.setValue(DataHelper.VerificationCode.getAuthCode(authInfo));
        verifyButton.click();
        return new DashboardPage();
    }
}
