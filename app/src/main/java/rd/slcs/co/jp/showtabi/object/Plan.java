package rd.slcs.co.jp.showtabi.object;

import java.io.Serializable;

public class Plan implements Serializable {

    private String planName;
    private String startYMD;
    private String endYMD;
    private String person;
    private String memo;
    private String icon;

    public Plan() {

    }


    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getStartYMD() {
        return startYMD;
    }

    public void setStartYMD(String startYMD) {
        this.startYMD = startYMD;
    }

    public String getEndYMD() {
        return endYMD;
    }

    public void setEndYMD(String endYMD) {
        this.endYMD = endYMD;
    }

    public String getPerson() { return person; }

    public void setPerson(String person) { this.person = person; }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
