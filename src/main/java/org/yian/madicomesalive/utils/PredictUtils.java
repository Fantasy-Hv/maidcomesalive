package org.yian.madicomesalive.utils;

public class PredictUtils {
    public static boolean assertAll(boolean ... conditions ){
        if (conditions==null)return false;
        boolean resu = true;
        for (boolean con:conditions){
            resu &= con;
        }
        return resu;
    }
}
