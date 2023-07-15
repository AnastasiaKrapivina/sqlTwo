package ru.netology.sql.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.netology.sql.data.ApiHelper;
import ru.netology.sql.data.DataHelper;
import ru.netology.sql.data.SQLHelper;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestForm {


    @Test
    @DisplayName("Should transfer from the first card to the second")
    public void validTransferFromFirstToSecond(){
        var authInfo = DataHelper.getAuthInfoWithTestData();
        ApiHelper.makeQueryToLogin(authInfo, 200);
        var verificationCode = SQLHelper.getVerificationCode();
        var verificationInfo = new DataHelper.VerificationInfo(authInfo.getLogin(), verificationCode.getCode());
        var tokenInfo = ApiHelper.sendQueryToVerify(verificationInfo, 200);
        var cardsBalances = ApiHelper.sentQueryToGetCardBalances(tokenInfo.getToken(), 200);
        var firstCardBalance = cardsBalances.get(DataHelper.getFirstCardInfo().getTestId());
        var secondCardBalance = cardsBalances.get(DataHelper.getSecondCardInfo().getTestId());
        var amount = DataHelper.validAmount(firstCardBalance);
        var transferInfo = new ApiHelper.APITransferInfo(DataHelper.getFirstCardInfo().getNumber(),
                DataHelper.getSecondCardInfo().getNumber(), amount);
        ApiHelper.generateQueryToTransfer(tokenInfo.getToken(), transferInfo, 200);
        cardsBalances = ApiHelper.sentQueryToGetCardBalances(tokenInfo.getToken(), 200);
        var actualFirstCardBalance = cardsBalances.get(DataHelper.getFirstCardInfo().getTestId());
        var actualSecondCardBalance = cardsBalances.get(DataHelper.getSecondCardInfo().getTestId());
        assertAll(() -> assertEquals(firstCardBalance - amount, actualFirstCardBalance),
                () -> assertEquals(secondCardBalance + amount, actualSecondCardBalance));
    }

}
