package rd.slcs.co.jp.showtabi.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import rd.slcs.co.jp.showtabi.R;

public class Const {

    /** Plans　データベース名 */
    public static final String DB_PLANTABLE = "plans";

    public static final String DB_PLANTABLE_PLANKEY = "planKey";
    public static final String DB_PLANTABLE_PLANNAME = "planName";
    public static final String DB_PLANTABLE_STARTYMD = "startYMD";
    public static final String DB_PLANTABLE_ENDYMD = "endYMD";
    public static final String DB_PLANTABLE_ICON = "icon";
    public static final String DB_PLANTABLE_MEMO = "memo";

    /** Events　データベース名 */
    public static final String DB_EVENTTABLE = "events";

    public static final String DB_EVENTTABLE_EVENTKEY = "eventKey";
    public static final String DB_EVENTTABLE_PLANKEY = "planKey";
    public static final String DB_EVENTTABLE_EVENTNAME = "eventName";
    public static final String DB_EVENTTABLE_STARTTIME = "startTime";
    public static final String DB_EVENTTABLE_ENDTIME = "endTime";
    public static final String DB_EVENTTABLE_MEMO = "memo";
    public static final String DB_EVENTTABLE_PHOTO = "photo";


    /** Photos　データベース名 */
    public static final String DB_PHOTOSTABLE = "photos";

    public static final String DB_PHOTOSTABLE_PHOTOKEY = "photoKey";
    public static final String DB_PHOTOSTABLE_EVENTKEY = "eventKey";
    public static final String DB_PHOTOSTABLE_PHOTO = "photo";
    public static final String DB_PHOTOSTABLE_SORTKEY = "sortKey";


    /** モデル名 */
    public static final String PLANDISP = "planDisp";
    public static final String EVENTDISP = "eventDisp";


    /** 日付フォーマット */
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYYMMDDHHMM = "yyyyMMddHHmm";

    /* カテゴリーアイコン */
    // category文字列とアイコン画像の対応Map
    public static final Map<String, Integer> categoryToIconMap = Collections.unmodifiableMap(new HashMap(){
        {put("移動", R.drawable.category_icon_run_24dp);
            put("食事", R.drawable.category_icon_eat_24dp);
            put("観光", R.drawable.category_icon_siteseeing_24dp);
            put("宿泊", R.drawable.category_icon_stay_24dp);
            put("その他", R.drawable.category_icon_other_24dp);
        }
    });

    /** 画面コード */
    public static final int SCREEN_EVENTLIST = 1;
    public static final int SCREEN_EVENTREFERENCE = 2;
    public static final int SCREEN_EVENTEDIT = 3;

}
