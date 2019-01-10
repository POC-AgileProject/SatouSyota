package rd.slcs.co.jp.showtabi.common;

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


}
