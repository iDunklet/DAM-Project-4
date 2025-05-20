package model;

public class Wood extends Putter {
    private boolean isHybrid;

    public Wood(boolean isHybrid) {
        super("Wood", 80.0, 0.3, 20.0);
        this.isHybrid = isHybrid;
    }

    @Override
    public String getUseType() {
        if (isHybrid) {
            return "FOR ROUGH";
        } else {
            return "FOR FAIRWAY";
        }
    }

    public boolean isHybrid() {
        return isHybrid;
    }

    public void setHybrid(boolean hybrid) {
        isHybrid = hybrid;
    }
}
