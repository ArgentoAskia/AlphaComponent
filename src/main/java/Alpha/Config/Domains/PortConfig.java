package Alpha.Config.Domains;

import Alpha.Config.Config;

public class PortConfig implements Config<Integer> {
    private int port;

    protected void set(Integer typeData) {
        port = typeData;
    }

    @Override
    public Integer get() {
        return port;
    }

    @Override
    public String toString() {
        return "PortConfig: " + port;
    }

    public PortConfig(int port){
        this.port = port;
    }
}
