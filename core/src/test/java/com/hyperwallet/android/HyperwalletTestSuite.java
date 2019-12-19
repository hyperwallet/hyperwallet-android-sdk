package com.hyperwallet.android;

import com.hyperwallet.android.model.graphql.RetrieveTransferMethodConfigurationFieldsTest;
import com.hyperwallet.android.transfer.CreateTransferTest;
import com.hyperwallet.android.transfermethod.CreateBankAccountTest;
import com.hyperwallet.android.transfermethod.CreateBankCardTest;
import com.hyperwallet.android.transfermethod.CreatePayPalAccountTest;
import com.hyperwallet.android.transfermethod.DeactivateBankAccountTest;
import com.hyperwallet.android.transfermethod.DeactivateBankCardTest;
import com.hyperwallet.android.transfermethod.DeactivatePayPalAccountTest;
import com.hyperwallet.android.transfermethod.GetBankAccountTest;
import com.hyperwallet.android.transfermethod.GetBankCardTest;
import com.hyperwallet.android.transfermethod.GetPayPalTest;
import com.hyperwallet.android.transfermethod.GetTransferTest;
import com.hyperwallet.android.transfermethod.GetUserTest;
import com.hyperwallet.android.transfermethod.ListBankAccountsTest;
import com.hyperwallet.android.transfermethod.ListBankCardsTest;
import com.hyperwallet.android.transfermethod.ListPayPalAccountsTest;
import com.hyperwallet.android.transfermethod.ListPrepaidCardReceiptsTest;
import com.hyperwallet.android.transfermethod.ListPrepaidCardsTest;
import com.hyperwallet.android.transfermethod.ListTransfersTest;
import com.hyperwallet.android.transfermethod.ListUserReceiptsTest;
import com.hyperwallet.android.transfermethod.RetrieveTransferMethodConfigurationKeysTest;
import com.hyperwallet.android.transfermethod.UpdateBankAccountTest;
import com.hyperwallet.android.transfermethod.UpdateBankCardTest;
import com.hyperwallet.android.transfermethod.UpdatePayPalAccountTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        CreateBankAccountTest.class,
        ListBankAccountsTest.class,
        CreateBankCardTest.class,
        CreatePayPalAccountTest.class,
        CreateTransferTest.class,
        GetBankAccountTest.class,
        GetBankCardTest.class,
        GetPayPalTest.class,
        GetUserTest.class,
        GetTransferTest.class,
        UpdateBankAccountTest.class,
        UpdateBankCardTest.class,
        UpdatePayPalAccountTest.class,
        DeactivateBankAccountTest.class,
        DeactivateBankCardTest.class,
        DeactivatePayPalAccountTest.class,
        ListBankCardsTest.class,
        RetrieveTransferMethodConfigurationKeysTest.class,
        RetrieveTransferMethodConfigurationFieldsTest.class,
        GetConfigurationTest.class,
        ListPayPalAccountsTest.class,
        ListUserReceiptsTest.class,
        ListPrepaidCardReceiptsTest.class,
        ListTransfersTest.class,
        ListPrepaidCardsTest.class
})
public class HyperwalletTestSuite {

}