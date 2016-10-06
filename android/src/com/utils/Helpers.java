package com.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Helpers {

    public static boolean isEmptyNull(List list) {
        return !isNotEmptyNull(list);
    }

    public static boolean isEmptyNull(String string) {
        return !isNotEmptyNull(string);
    }

    public static boolean isNotEmptyNull(String[] list) {
        if (list == null)
            return false;
        if (list.length == 0)
            return false;
        return true;
    }

    public static boolean isNotEmptyNull(List list) {
        if (list == null)
            return false;
        if (list.isEmpty())
            return false;
        return true;
    }

    public static boolean isNotEmptyNull(String string) {
        if (string == null)
            return false;
        if (string.isEmpty())
            return false;
        return true;
    }


    public static boolean isTextMessage(String string) {
        if (isNotEmptyNull(string)) {
            string = string.trim();
            return isNotEmptyNull(string);
        } else
            return false;
    }

    private static String[] getCertificateFingerprint(Context ctx, String packageName) {
        try {
            if (ctx == null || ctx.getPackageManager() == null)
                return null;
            PackageInfo info = ctx.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            assert info.signatures != null;
            String[] result = new String[info.signatures.length];
            int i = 0;
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                result[i++] = toHex(md.digest());
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    public static void showFingerprint(Context context) {
        String[] fingerprint = Helpers.getCertificateFingerprint(context, context.getPackageName());
        Log.d("Fingerprint", fingerprint[0]);
    }


}
