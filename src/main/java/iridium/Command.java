package iridium;

public class Command {
    private Runnable function;
    private int parameter;
    private String alias;

    public Command(Runnable function, int parameter, String alias){
        this.function = function;
        this.parameter = parameter;
        this.alias = alias;
    }

    public Runnable getFunction() {
        return function;
    }

    public int getParameter() {
        return parameter;
    }

    public String getAlias() {
        return alias;
    }

}
