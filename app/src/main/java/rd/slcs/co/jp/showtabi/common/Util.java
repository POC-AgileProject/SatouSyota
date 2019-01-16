package rd.slcs.co.jp.showtabi.common;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Util {

    private final static int YMD_DATE_LENGTH = 8;
    private final static int YMDHM_DATE_LENGTH = 12;

    /* String → Date型 */
    public static Date convertToDate(String dateStr){

        //ToDO:バリデーションチェック前提で、PlanやEventのオブジェクトを渡すよう変更する？？

        SimpleDateFormat fmt;
        switch(dateStr.length()) {
            case YMD_DATE_LENGTH:  fmt = new SimpleDateFormat(Const.YYYYMMDD);break;
            case YMDHM_DATE_LENGTH: fmt = new SimpleDateFormat(Const.YYYYMMDDHHMM);break;
            default: fmt = new SimpleDateFormat();
        }

        try {
            return fmt.parse(dateStr);
        } catch (Exception e) {
            Log.d("convertToDate", "Date型への変換が解決できませんでした。");
            return null;
        }
    }


}
