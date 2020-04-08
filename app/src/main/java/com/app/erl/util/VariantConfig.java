package com.app.erl.util;

/**
 *
 */
public class VariantConfig {

    public static String getServerBaseUrl() {
        //live ip
        return "https://owlmanagementsystems.com/api/v1/";
//        return "http://time.owlmanagementsystems.com/api/v1/";
//        return "http://192.168.1.115/TimeManagementWeb/api/v1/";


//        return "http://newtime.owlmanagementsystems.com/api/v1/";

    }

    public static String getApkfilePath() {
        return getServerBaseUrl() + "mobile/" + AppConstant.APP_APK_NAME;
    }

}
