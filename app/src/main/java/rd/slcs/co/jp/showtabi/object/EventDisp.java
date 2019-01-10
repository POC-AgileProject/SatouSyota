package rd.slcs.co.jp.showtabi.object;

public class EventDisp extends Event{

    private String key;

    public EventDisp(Event event, String key) {
        this.key = key;
        this.setEventName(event.getEventName());
        this.setPlanKey(event.getPlanKey());
        this.setStartTime(event.getStartTime());
        this.setEndTime(event.getEndTime());
        this.setCategory(event.getCategory());
        this.setMemo(event.getMemo());

    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
