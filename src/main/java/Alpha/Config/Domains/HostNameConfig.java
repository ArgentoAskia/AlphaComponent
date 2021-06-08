package Alpha.Config.Domains;

public class HostNameConfig extends NameConfig {

    public HostNameConfig(String hostName){
        super(hostName);
    }

    /**
     * hostname配置信息
     * <p>
     * 如果hostName是{@code localhost}或者是{@code 127.0.0.1},将会使用{@code defaultName}
     *
     * @param hostName 主要的hostName名称
     * @param defaultName 备选名称
     */
    public HostNameConfig(String hostName, String defaultName){
        super(hostName);
        if(hostName == null ||
                hostName.equals("localhost") ||
                hostName.equals("127.0.0.1")){
            set(defaultName);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
