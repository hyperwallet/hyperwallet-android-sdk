package com.hyperwallet.android;

import com.hyperwallet.android.model.graphql.HyperwalletRetrieveTransferMethodConfigurationFieldsTest;
import com.hyperwallet.android.transfer.HyperwalletCreateTransferTest;
import com.hyperwallet.android.transfermethod.HyperwalletCreateBankAccountTest;
import com.hyperwallet.android.transfermethod.HyperwalletCreateBankCardTest;
import com.hyperwallet.android.transfermethod.HyperwalletCreatePayPalAccountTest;
import com.hyperwallet.android.transfermethod.HyperwalletDeactivateBankAccountTest;
import com.hyperwallet.android.transfermethod.HyperwalletDeactivateBankCardTest;
import com.hyperwallet.android.transfermethod.HyperwalletDeactivatePayPalAccountTest;
import com.hyperwallet.android.transfermethod.HyperwalletGetBankAccountTest;
import com.hyperwallet.android.transfermethod.HyperwalletGetBankCardTest;
import com.hyperwallet.android.transfermethod.HyperwalletGetPayPalTest;
import com.hyperwallet.android.transfermethod.HyperwalletGetTransferTest;
import com.hyperwallet.android.transfermethod.HyperwalletGetUserTest;
import com.hyperwallet.android.transfermethod.HyperwalletListBankAccountsTest;
import com.hyperwallet.android.transfermethod.HyperwalletListBankCardsTest;
import com.hyperwallet.android.transfermethod.HyperwalletListPayPalAccountsTest;
import com.hyperwallet.android.transfermethod.HyperwalletListPrepaidCardReceiptsTest;
import com.hyperwallet.android.transfermethod.HyperwalletListTransfersTest;
import com.hyperwallet.android.transfermethod.HyperwalletListUserReceiptsTest;
import com.hyperwallet.android.transfermethod.HyperwalletRetrieveTransferMethodConfigurationKeysTest;
import com.hyperwallet.android.transfermethod.HyperwalletUpdateBankAccountTest;
import com.hyperwallet.android.transfermethod.HyperwalletUpdateBankCardTest;
import com.hyperwallet.android.transfermethod.HyperwalletUpdatePayPalAccountTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({
        HyperwalletCreateBankAccountTest.class,
        HyperwalletListBankAccountsTest.class,
        HyperwalletCreateBankCardTest.class,
        HyperwalletCreatePayPalAccountTest.class,
        HyperwalletCreateTransferTest.class,
        HyperwalletGetBankAccountTest.class,
        HyperwalletGetBankCardTest.class,
        HyperwalletGetPayPalTest.class,
        HyperwalletGetUserTest.class,
        HyperwalletGetTransferTest.class,
        HyperwalletUpdateBankAccountTest.class,
        HyperwalletUpdateBankCardTest.class,
        HyperwalletUpdatePayPalAccountTest.class,
        HyperwalletDeactivateBankAccountTest.class,
        HyperwalletDeactivateBankCardTest.class,
        HyperwalletDeactivatePayPalAccountTest.class,
        HyperwalletListBankCardsTest.class,
        HyperwalletRetrieveTransferMethodConfigurationKeysTest.class,
        HyperwalletRetrieveTransferMethodConfigurationFieldsTest.class,
        HyperwalletGetConfigurationTest.class,
        HyperwalletListPayPalAccountsTest.class,
        HyperwalletListUserReceiptsTest.class,
        HyperwalletListPrepaidCardReceiptsTest.class,
        HyperwalletListTransfersTest.class
})
public class HyperwalletTestSuite {

}