package model;

public abstract class Putter {
    protected String name;
    protected double speed;
    protected double precision;
    protected double angle;

    public Putter(String name, double speed, double precision, double angle) {
        this.name = name;
        this.speed = speed;
        this.precision = precision;
        this.angle = angle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public abstract String getUseType();
}