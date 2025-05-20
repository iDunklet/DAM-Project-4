package model;

public class Driver extends Putter {
    private double aerodynamicCoefficient;

    public Driver() {
        super("Driver", 95.0, 0.4, 15.0);
        this.aerodynamicCoefficient = 0.3;
    }

    @Override
    public String getUseType() {
        return "FOR LONG SHOTS";
    }

    public double getAerodynamicCoefficient() {
        return aerodynamicCoefficient;
    }

    public void setAerodynamicCoefficient(double aerodynamicCoefficient) {
        this.aerodynamicCoefficient = aerodynamicCoefficient;
    }
}