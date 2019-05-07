package com.hyperwallet.android.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.StringContains.containsString;

import static com.hyperwallet.android.model.HyperwalletUser.BusinessContactRoles.OWNER;
import static com.hyperwallet.android.model.HyperwalletUser.BusinessTypes.CORPORATION;
import static com.hyperwallet.android.model.HyperwalletUser.Genders.MALE;
import static com.hyperwallet.android.model.HyperwalletUser.GovernmentIdTypes.PASSPORT;
import static com.hyperwallet.android.model.HyperwalletUser.ProfileTypes.INDIVIDUAL;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.ADDRESS_LINE_1;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.ADDRESS_LINE_2;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.BUSINESS_CONTACT_ADDRESS_LINE_1;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.BUSINESS_CONTACT_ADDRESS_LINE_2;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.BUSINESS_CONTACT_CITY;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.BUSINESS_CONTACT_COUNTRY;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.BUSINESS_CONTACT_POSTAL_CODE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.BUSINESS_CONTACT_ROLE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.BUSINESS_CONTACT_STATE_PROVINCE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.BUSINESS_NAME;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.BUSINESS_OPERATING_NAME;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.BUSINESS_REGISTRATION_COUNTRY;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.BUSINESS_REGISTRATION_ID;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.BUSINESS_REGISTRATION_STATE_PROVINCE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.BUSINESS_TYPE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.CITY;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.CLIENT_USER_ID;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.COUNTRY;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.COUNTRY_OF_BIRTH;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.COUNTRY_OF_NATIONALITY;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.CREATED_ON;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.DATE_OF_BIRTH;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.DRIVERS_LICENSE_ID;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.EMAIL;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.EMPLOYER_ID;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.FIRST_NAME;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.GENDER;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.GOVERNMENT_ID;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.GOVERNMENT_ID_TYPE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.LANGUAGE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.LAST_NAME;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.MIDDLE_NAME;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.MOBILE_NUMBER;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.PASSPORT_ID;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.PHONE_NUMBER;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.POSTAL_CODE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.PROFILE_TYPE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.PROGRAM_TOKEN;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.STATE_PROVINCE;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.STATUS;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.TOKEN;
import static com.hyperwallet.android.model.HyperwalletUser.UserFields.VERIFICATION_STATUS;
import static com.hyperwallet.android.model.HyperwalletUser.UserStatuses.LOCKED;
import static com.hyperwallet.android.model.HyperwalletUser.VerificationStatuses.UNDER_REVIEW;
import static com.hyperwallet.android.util.JsonUtils.fromJsonString;

import android.os.Parcel;

import com.hyperwallet.android.rule.HyperwalletExternalResourceManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class HyperwalletUserTest {
    @Rule
    public HyperwalletExternalResourceManager mExternalResourceManager = new HyperwalletExternalResourceManager();

    @Test
    public void testFromJsonString_userResponse() throws Exception {
        HyperwalletUser actualUser = fromJsonString(
                mExternalResourceManager.getResourceContent("user_response.json"),
                new TypeReference<HyperwalletUser>() {
                });

        assertThat(actualUser, is(notNullValue()));
        assertThat(actualUser.getToken(), is("usr-f9154016-94e8-4686-a840-075688ac07b5"));
        assertThat(actualUser.getStatus(), is("PRE_ACTIVATED"));
        assertThat(actualUser.getVerificationStatus(), is("NOT_REQUIRED"));
        assertThat(actualUser.getCreatedOn(), is("2017-10-30T22:15:45"));
        assertThat(actualUser.getClientUserId(), is("123345789"));
        assertThat(actualUser.getProfileType(), is("INDIVIDUAL"));
        assertThat(actualUser.getFirstName(), is("Some"));
        assertThat(actualUser.getLastName(), is("Guy"));
        assertThat(actualUser.getDateOfBirth(), is("1991-01-01"));
        assertThat(actualUser.getEmail(), is("someguy@hyperwallet.com"));
        assertThat(actualUser.getAddressLine1(), is("575 Market Street"));
        assertThat(actualUser.getCity(), is("San Francisco"));
        assertThat(actualUser.getStateProvince(), is("CA"));
        assertThat(actualUser.getCountry(), is("US"));
        assertThat(actualUser.getPostalCode(), is("94105"));
        assertThat(actualUser.getLanguage(), is("en"));
        assertThat(actualUser.getProgramToken(), is("prg-83836cdf-2ce2-4696-8bc5-f1b86077238c"));

    }

    @Test
    public void testToJsonObject_user() throws Exception {

        final HyperwalletUser expectedUser = new HyperwalletUser.Builder()
                .token("usr-f9154016-94e8-4686-a840-075688ac07b5")
                .status(LOCKED)
                .verificationStatus(UNDER_REVIEW)
                .createdOn("2017-10-30T22:15:45")
                .clientUserId("123345789")
                .addressLine1("575 Market Street")
                .addressLine2("247 Tottenham Court Rd")
                .businessContactRole(OWNER)
                .businessName("My Company")
                .businessRegistrationCountry("CA")
                .businessRegistrationId("werqq")
                .businessRegistrationStateProvince("BC")
                .businessContactAddressLine1("34 Zoo pl")
                .businessContactAddressLine2("102 Park ave")
                .businessContactCity("Zippy")
                .businessContactStateProvince("Quebec")
                .businessContactCountry("CA")
                .businessContactPostalCode("QC H2Y 2E2")
                .businessOperatingName("OP name")
                .businessType(CORPORATION)
                .city("Montréal")
                .country("CA")
                .countryOfBirth("US")
                .countryOfNationality("CA")
                .dateOfBirth("1991-03-09")
                .driversLicenseId("ID-45dff")
                .email("someguy@hyperwallet.com")
                .employerId("34333")
                .firstName("Jany")
                .gender(MALE)
                .governmentId("CA-MO-12")
                .governmentIdType(PASSPORT)
                .language("fr")
                .lastName("Smith")
                .middleName("de")
                .mobileNumber("+1514-496-7678")
                .passportId("AV54467")
                .phoneNumber("496-7678")
                .postalCode("94105")
                .profileType(INDIVIDUAL)
                .programToken("prg-83836cdf-2ce2-4696-8bc5-f1b86077238c")
                .stateProvince("BC")
                .build();

        JSONObject jsonObject = expectedUser.toJsonObject();

        assertThat(jsonObject.getString(TOKEN), is("usr-f9154016-94e8-4686-a840-075688ac07b5"));
        assertThat(jsonObject.getString(STATUS), is(LOCKED));
        assertThat(jsonObject.getString(VERIFICATION_STATUS), is(UNDER_REVIEW));
        assertThat(jsonObject.getString(CREATED_ON), is("2017-10-30T22:15:45"));
        assertThat(jsonObject.getString(CLIENT_USER_ID), is("123345789"));
        assertThat(jsonObject.getString(ADDRESS_LINE_1), is("575 Market Street"));
        assertThat(jsonObject.getString(ADDRESS_LINE_2), is("247 Tottenham Court Rd"));
        assertThat(jsonObject.getString(BUSINESS_CONTACT_ROLE), is(OWNER));
        assertThat(jsonObject.getString(BUSINESS_NAME), is("My Company"));
        assertThat(jsonObject.getString(BUSINESS_REGISTRATION_COUNTRY), is("CA"));
        assertThat(jsonObject.getString(BUSINESS_REGISTRATION_ID), is("werqq"));
        assertThat(jsonObject.getString(BUSINESS_REGISTRATION_STATE_PROVINCE), is("BC"));
        assertThat(jsonObject.getString(BUSINESS_CONTACT_ADDRESS_LINE_1), is("34 Zoo pl"));
        assertThat(jsonObject.getString(BUSINESS_CONTACT_ADDRESS_LINE_2), is("102 Park ave"));
        assertThat(jsonObject.getString(BUSINESS_CONTACT_CITY), is("Zippy"));
        assertThat(jsonObject.getString(BUSINESS_CONTACT_STATE_PROVINCE), is("Quebec"));
        assertThat(jsonObject.getString(BUSINESS_CONTACT_COUNTRY), is("CA"));
        assertThat(jsonObject.getString(BUSINESS_CONTACT_POSTAL_CODE), is("QC H2Y 2E2"));
        assertThat(jsonObject.getString(BUSINESS_OPERATING_NAME), is("OP name"));
        assertThat(jsonObject.getString(BUSINESS_TYPE), is(CORPORATION));
        assertThat(jsonObject.getString(CITY), is("Montréal"));
        assertThat(jsonObject.getString(COUNTRY), is("CA"));
        assertThat(jsonObject.getString(COUNTRY_OF_BIRTH), is("US"));
        assertThat(jsonObject.getString(COUNTRY_OF_NATIONALITY), is("CA"));
        assertThat(jsonObject.getString(DATE_OF_BIRTH), is("1991-03-09"));
        assertThat(jsonObject.getString(DRIVERS_LICENSE_ID), is("ID-45dff"));
        assertThat(jsonObject.getString(EMAIL), is("someguy@hyperwallet.com"));
        assertThat(jsonObject.getString(EMPLOYER_ID), is("34333"));
        assertThat(jsonObject.getString(FIRST_NAME), is("Jany"));
        assertThat(jsonObject.getString(GENDER), is(MALE));
        assertThat(jsonObject.getString(GOVERNMENT_ID), is("CA-MO-12"));
        assertThat(jsonObject.getString(GOVERNMENT_ID_TYPE), is(PASSPORT));
        assertThat(jsonObject.getString(LANGUAGE), is("fr"));
        assertThat(jsonObject.getString(LAST_NAME), is("Smith"));
        assertThat(jsonObject.getString(MIDDLE_NAME), is("de"));
        assertThat(jsonObject.getString(MOBILE_NUMBER), is("+1514-496-7678"));
        assertThat(jsonObject.getString(PASSPORT_ID), is("AV54467"));
        assertThat(jsonObject.getString(PHONE_NUMBER), is("496-7678"));
        assertThat(jsonObject.getString(POSTAL_CODE), is("94105"));
        assertThat(jsonObject.getString(PROFILE_TYPE), is(INDIVIDUAL));
        assertThat(jsonObject.getString(PROGRAM_TOKEN), is("prg-83836cdf-2ce2-4696-8bc5-f1b86077238c"));
        assertThat(jsonObject.getString(STATE_PROVINCE), is("BC"));
    }

    @Test
    public void testHyperwalletUser_getNullField() {
        final HyperwalletUser actualUser = new HyperwalletUser.Builder()
                .token("usr-f9154016-94e8-4686-a840-075688ac07b5").build();
        assertThat(actualUser.getField("my field"), is(nullValue()));
    }

    @Test
    public void testHyperwalletUser_getField() {
        final HyperwalletUser actualUser = new HyperwalletUser.Builder()
                .token("usr-f9154016-94e8-4686-a840-075688ac07b5").build();
        assertThat(actualUser.getField(TOKEN), is("usr-f9154016-94e8-4686-a840-075688ac07b5"));
    }

    @Test
    public void testToJsonString_user() throws JSONException {
        final HyperwalletUser actualUser = new HyperwalletUser.Builder()
                .token("usr-f9154016-94e8-4686-a840-075688ac07b5")
                .status(LOCKED)
                .build();

        String actualUserJsonString = actualUser.toJsonString();
        assertThat(actualUserJsonString, containsString("usr-f9154016-94e8-4686-a840-075688ac07b5"));
        assertThat(actualUserJsonString, containsString(LOCKED));
    }

    @Test
    public void testHyperwalletUser_isParcelable() throws Exception {

        String json = mExternalResourceManager.getResourceContent("user_response.json");

        HyperwalletUser user = fromJsonString(json, new TypeReference<HyperwalletUser>() {
        });

        Parcel parcel = Parcel.obtain();
        user.writeToParcel(parcel, user.describeContents());
        parcel.setDataPosition(0);
        HyperwalletUser bundledUser = HyperwalletUser.CREATOR.createFromParcel(parcel);

        assertThat(bundledUser, is(notNullValue()));
        assertThat(bundledUser.getToken(), is("usr-f9154016-94e8-4686-a840-075688ac07b5"));
        assertThat(bundledUser.getStatus(), is("PRE_ACTIVATED"));
        assertThat(bundledUser.getVerificationStatus(), is("NOT_REQUIRED"));
        assertThat(bundledUser.getCreatedOn(), is("2017-10-30T22:15:45"));
        assertThat(bundledUser.getClientUserId(), is("123345789"));
        assertThat(bundledUser.getProfileType(), is("INDIVIDUAL"));
        assertThat(bundledUser.getFirstName(), is("Some"));
        assertThat(bundledUser.getLastName(), is("Guy"));
        assertThat(bundledUser.getDateOfBirth(), is("1991-01-01"));
        assertThat(bundledUser.getEmail(), is("someguy@hyperwallet.com"));
        assertThat(bundledUser.getAddressLine1(), is("575 Market Street"));
        assertThat(bundledUser.getCity(), is("San Francisco"));
        assertThat(bundledUser.getStateProvince(), is("CA"));
        assertThat(bundledUser.getCountry(), is("US"));
        assertThat(bundledUser.getPostalCode(), is("94105"));
        assertThat(bundledUser.getLanguage(), is("en"));
        assertThat(bundledUser.getProgramToken(), is("prg-83836cdf-2ce2-4696-8bc5-f1b86077238c"));
    }

    @Test
    public void testHyperwalletUser_fieldsAccessors() {
        final HyperwalletUser expectedUser = new HyperwalletUser.Builder()
                .token("usr-f9154016-94e8-4686-a840-075688ac07b5")
                .status(LOCKED)
                .verificationStatus(UNDER_REVIEW)
                .createdOn("2017-10-30T22:15:45")
                .clientUserId("123345789")
                .addressLine1("575 Market Street")
                .addressLine2("247 Tottenham Court Rd")
                .businessContactRole(OWNER)
                .businessName("My Company")
                .businessRegistrationCountry("CA")
                .businessRegistrationId("werqq")
                .businessRegistrationStateProvince("BC")
                .businessContactAddressLine1("34 Zoo pl")
                .businessContactAddressLine2("102 Park ave")
                .businessContactCity("Zippy")
                .businessContactStateProvince("Quebec")
                .businessContactCountry("CA")
                .businessContactPostalCode("QC H2Y 2E2")
                .businessOperatingName("OP name")
                .businessType(CORPORATION)
                .city("Montréal")
                .country("CA")
                .countryOfBirth("US")
                .countryOfNationality("CA")
                .dateOfBirth("1991-03-09")
                .driversLicenseId("ID-45dff")
                .email("someguy@hyperwallet.com")
                .employerId("34333")
                .firstName("Jany")
                .gender(MALE)
                .governmentId("CA-MO-12")
                .governmentIdType(PASSPORT)
                .language("fr")
                .lastName("Smith")
                .middleName("de")
                .mobileNumber("+1514-496-7678")
                .passportId("AV54467")
                .phoneNumber("496-7678")
                .postalCode("94105")
                .profileType(INDIVIDUAL)
                .programToken("prg-83836cdf-2ce2-4696-8bc5-f1b86077238c")
                .stateProvince("BC")
                .build();

        assertThat(expectedUser.getToken(), is("usr-f9154016-94e8-4686-a840-075688ac07b5"));
        assertThat(expectedUser.getStatus(), is(LOCKED));
        assertThat(expectedUser.getVerificationStatus(), is(UNDER_REVIEW));
        assertThat(expectedUser.getCreatedOn(), is("2017-10-30T22:15:45"));
        assertThat(expectedUser.getClientUserId(), is("123345789"));
        assertThat(expectedUser.getAddressLine1(), is("575 Market Street"));
        assertThat(expectedUser.getAddressLine2(), is("247 Tottenham Court Rd"));
        assertThat(expectedUser.getBusinessContactRole(), is(OWNER));
        assertThat(expectedUser.getBusinessName(), is("My Company"));
        assertThat(expectedUser.getBusinessRegistrationCountry(), is("CA"));
        assertThat(expectedUser.getBusinessRegistrationId(), is("werqq"));
        assertThat(expectedUser.getBusinessRegistrationStateProvince(), is("BC"));
        assertThat(expectedUser.getBusinessContactAddressLine1(), is("34 Zoo pl"));
        assertThat(expectedUser.getBusinessContactAddressLine2(), is("102 Park ave"));
        assertThat(expectedUser.getBusinessContactCity(), is("Zippy"));
        assertThat(expectedUser.getBusinessContactStateProvince(), is("Quebec"));
        assertThat(expectedUser.getBusinessContactCountry(), is("CA"));
        assertThat(expectedUser.getBusinessContactPostalCode(), is("QC H2Y 2E2"));
        assertThat(expectedUser.getBusinessOperatingName(), is("OP name"));
        assertThat(expectedUser.getBusinessType(), is(CORPORATION));
        assertThat(expectedUser.getCity(), is("Montréal"));
        assertThat(expectedUser.getCountry(), is("CA"));
        assertThat(expectedUser.getCountryOfBirth(), is("US"));
        assertThat(expectedUser.getCountryOfNationality(), is("CA"));
        assertThat(expectedUser.getDateOfBirth(), is("1991-03-09"));
        assertThat(expectedUser.getDriversLicenseId(), is("ID-45dff"));
        assertThat(expectedUser.getEmail(), is("someguy@hyperwallet.com"));
        assertThat(expectedUser.getEmployerId(), is("34333"));
        assertThat(expectedUser.getFirstName(), is("Jany"));
        assertThat(expectedUser.getGender(), is(MALE));
        assertThat(expectedUser.getGovernmentId(), is("CA-MO-12"));
        assertThat(expectedUser.getGovernmentIdType(), is(PASSPORT));
        assertThat(expectedUser.getLanguage(), is("fr"));
        assertThat(expectedUser.getLastName(), is("Smith"));
        assertThat(expectedUser.getMiddleName(), is("de"));
        assertThat(expectedUser.getMobileNumber(), is("+1514-496-7678"));
        assertThat(expectedUser.getPassportId(), is("AV54467"));
        assertThat(expectedUser.getPhoneNumber(), is("496-7678"));
        assertThat(expectedUser.getPostalCode(), is("94105"));
        assertThat(expectedUser.getProfileType(), is(INDIVIDUAL));
        assertThat(expectedUser.getProgramToken(), is("prg-83836cdf-2ce2-4696-8bc5-f1b86077238c"));
        assertThat(expectedUser.getStateProvince(), is("BC"));
    }
}
