package steps;

import com.codeborne.selenide.Selenide;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import io.cucumber.java.ru.Тогда;
import org.junit.jupiter.api.Assertions;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;
import ru.netology.page.DashboardPage;
import ru.netology.page.TransferPage;
import ru.netology.page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;

public class TemplateSteps {
    private static LoginPage loginPage;
    private static DashboardPage dashboardPage;
    private static VerificationPage verificationPage;

    @Пусть("пользователь залогинен с именем {string} и паролем {string}")
    public void setLoginPassword(String login, String password) {
        open("http://localhost:9999/");
        loginPage = new LoginPage();
        verificationPage = loginPage.validLogin(login, password);
    }

    @И("пользователь вводит проверочный код 'из смс' {string}")
    public void setValidCode(String verificationCode) {
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Когда("пользователь переводит {string} рублей с карты с номером {string} на свою {string} карту")
    public void transferOperation(String moneyAmount, String cardNumber2, String cardNumber1) {
        var transferPage = dashboardPage.transferToFirstCard();
        transferPage.transferOperation(moneyAmount, cardNumber2);
    }

    @Тогда("баланс его {string} карты из списка на главной странице должен стать {string} рублей")
    public void shouldShowIncreasedBalance(String cardNumber1, String moneyAmount) {
        var firstCardBalance = dashboardPage.getCardBalance(DataHelper.getCardNumber().getNumber());
        Assertions.assertEquals(moneyAmount.replace(" ", ""), String.valueOf(firstCardBalance));
    }
}
