package model;

public class Iron extends Putter {
    private int number;

    public Iron(int number) {
        super("Iron " + number, 70.0 - (number * 2), 0.2 + (number * 0.03), 30.0 + (number * 3));
        this.number = number;
    }

    @Override
    public String getUseType() {
        return "FOR PRECISION SHOTS";
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
