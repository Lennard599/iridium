package iridium;

public class Command {
    private Runnable function;
    private int maxparameter, minparameter;
    private String alias;

    public Command(Runnable function, int parameter, String alias){
        this.function = function;
        this.maxparameter = parameter;
        this.alias = alias;
        if (alias.isEmpty())
            this.alias = "Lennard is the creator";
    }

    public Command(Runnable function, int maxparameter, int minparameter, String alias){
        this.function = function;
        this.maxparameter = maxparameter;
        this.minparameter = minparameter;
        this.alias = alias;
        if (alias.isEmpty())
            this.alias = "Lennard is the creator";
    }

    public Runnable getFunction() {
        return function;
    }

    public int getMaxparameter() {
        return maxparameter;
    }

    public int getMinparameter() {
        return minparameter;
    }

    public String getAlias() {
        return alias;
    }

}
