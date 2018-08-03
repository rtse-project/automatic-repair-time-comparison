package daikon.instrumentation.preprocess;

/**
 * Created by giovanni on 28/04/2017.
 */
public class Variable {
    String type;
    String name;

    public Variable(String csv_value){
        String[] exp = csv_value.split("-");
        this.type = exp[0];
        this.name = exp[1];
    }
    public Variable(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
