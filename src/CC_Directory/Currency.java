package CC_Directory;

public class Currency {

    public String getName() {
        return name;
    }

    public String getRate() {
        return rate;
    }

    private String name;
    private String rate;

    public Currency(String nm, String rt){
        this.name = nm;
        this.rate = rt;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "name='" + name + '\'' +
                ", rate=" + rate +
                '}';
    }
}
