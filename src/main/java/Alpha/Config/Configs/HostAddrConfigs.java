package Alpha.Config.Configs;

import Alpha.Config.Domains.HostNameConfig;
import Alpha.Config.Domains.IPConfig;
import Alpha.Config.Domains.PortConfig;

public class HostAddrConfigs {
    private IPConfig ip;
    private HostNameConfig host;
    private PortConfig port;

    public IPConfig getIp() {
        return ip;
    }

    public void setIp(IPConfig ip) {
        this.ip = ip;
    }

    public HostNameConfig getHost() {
        return host;
    }

    public void setHost(HostNameConfig host) {
        this.host = host;
    }

    public PortConfig getPort() {
        return port;
    }

    public void setPort(PortConfig port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "HostAddrConfigs{" +
                "ip=" + ip +
                ", host=" + host +
                ", port=" + port +
                '}';
    }
}
