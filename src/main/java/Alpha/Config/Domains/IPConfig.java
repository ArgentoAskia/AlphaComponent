package Alpha.Config.Domains;

import Alpha.Config.Config;

public class IPConfig implements Config<String> {
    String IP;
    @Override
    public String get() {
        return IP;
    }

    protected void set(String typeData) {
        IP = typeData;
    }

    @Override
    public String toString() {
        return "IPConfig: " + IP;
    }

    public IPConfig(String typeData){
        set(typeData);
    }

}
