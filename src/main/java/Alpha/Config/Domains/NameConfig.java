package Alpha.Config.Domains;

import Alpha.Config.Config;

public class NameConfig implements Config<String> {
    private String name;

    protected void set(String typeData) {
        name = typeData;
    }

    @Override
    public String get() {
        return name;
    }

    @Override
    public String toString() {
        return "NameConfig: " + name;
    }

    public NameConfig(String name){
        this.name =  name;
    }
}
