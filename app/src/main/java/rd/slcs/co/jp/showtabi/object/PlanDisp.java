package rd.slcs.co.jp.showtabi.object;

public class PlanDisp extends Plan{

    private String key;

    public PlanDisp(Plan plan, String key) {
        this.key = key;
        this.setPlanName(plan.getPlanName());
        this.setStartYMD(plan.getStartYMD());
        this.setEndYMD(plan.getEndYMD());
        this.setIcon(plan.getIcon());
        this.setPerson(plan.getPerson());
        this.setMemo(plan.getMemo());
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
