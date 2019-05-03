package com.hyperwallet.android;

import com.hyperwallet.android.model.meta.HyperwalletRetrieveTransferMethodConfigurationFieldsTest;
import com.hyperwallet.android.transfermethod.HyperwalletCreateBankAccountTest;
import com.hyperwallet.android.transfermethod.HyperwalletCreateBankCardTest;
import com.hyperwallet.android.transfermethod.HyperwalletDeactivateBankAccountTest;
import com.hyperwallet.android.transfermethod.HyperwalletDeactivateBankCardTest;
import com.hyperwallet.android.transfermethod.HyperwalletGetBankAccountTest;
import com.hyperwallet.android.transfermethod.HyperwalletGetBankCardTest;
import com.hyperwallet.android.transfermethod.HyperwalletGetUserTest;
import com.hyperwallet.android.transfermethod.HyperwalletListBankAccountsTest;
import com.hyperwallet.android.transfermethod.HyperwalletListBankCardsTest;
import com.hyperwallet.android.transfermethod.HyperwalletListTransferMethodsTest;
import com.hyperwallet.android.transfermethod.HyperwalletRetrieveTransferMethodConfigurationKeysTest;
import com.hyperwallet.android.transfermethod.HyperwalletUpdateBankAccountTest;
import com.hyperwallet.android.transfermethod.HyperwalletUpdateBankCardTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        HyperwalletCreateBankAccountTest.class,
        HyperwalletListBankAccountsTest.class,
        HyperwalletCreateBankCardTest.class,
        HyperwalletGetBankAccountTest.class,
        HyperwalletGetBankCardTest.class,
        HyperwalletGetUserTest.class,
        HyperwalletUpdateBankAccountTest.class,
        HyperwalletUpdateBankCardTest.class,
        HyperwalletDeactivateBankAccountTest.class,
        HyperwalletDeactivateBankCardTest.class,
        HyperwalletListTransferMethodsTest.class,
        HyperwalletListBankCardsTest.class,
        HyperwalletRetrieveTransferMethodConfigurationKeysTest.class,
        HyperwalletRetrieveTransferMethodConfigurationFieldsTest.class
})
public class HyperwalletTestSuite {

}