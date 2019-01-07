package rd.slcs.co.jp.showtabi.object;

public class Event {

    // 時間を切り取る処理において基準となるインデックス値
    private final static int SUBTIME_INDEX = 8;

    private String planKey;
    private String eventName;
    private String startTime;
    private String endTime;
    private String category;
    private String memo;
    private String photos;

    public Event (){

    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getPlanKey() {
        return planKey;
    }

    public void setPlanKey(String planKey) {
        this.planKey = planKey;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }


    // 開始時間に関して、時刻を切り出す。
    public String subStartTime() {
        return getStartTime().substring(SUBTIME_INDEX);
    }

    // 終了時間に関して、時刻を切り出す。
    public String subEndTime(){
        return getEndTime().substring(SUBTIME_INDEX);
    }








}
