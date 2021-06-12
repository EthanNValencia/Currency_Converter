package CC_Directory;

public class Currency {

    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }

    private String name;
    private double rate;

    public Currency(String nm, double rt){
        this.name = nm;
        this.rate = rt;
    }

}
