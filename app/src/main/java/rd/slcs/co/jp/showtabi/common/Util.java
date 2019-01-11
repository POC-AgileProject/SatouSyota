package rd.slcs.co.jp.showtabi.common;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Util {

    /* String → Date型 */
    public static Date convertToDate(String dateStr){

        //ToDO:バリデーションチェック前提。

        SimpleDateFormat fmt;
        switch(dateStr.length()) {
            case 8:  fmt = new SimpleDateFormat("yyyyMMdd");break;
            case 12: fmt = new SimpleDateFormat("yyyyMMddHHmm");break;
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
