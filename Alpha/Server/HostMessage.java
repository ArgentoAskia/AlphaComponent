package Alpha.Server;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

public class HostMessage {
    @Deprecated(since = "dev 1.8")
    private InetSocketAddress InternetSocketaddress;

    private String HostName;
    private String IPAddress;
    private int port;
    public HostMessage(Socket socket){
        if(socket.getRemoteSocketAddress() instanceof InetSocketAddress){
            InternetSocketaddress  = (InetSocketAddress)socket.getRemoteSocketAddress();
            HostName = InternetSocketaddress.getHostName();
            IPAddress = InternetSocketaddress.getAddress().getHostAddress();
            port = InternetSocketaddress.getPort();
        }else{
            HostName = socket.getInetAddress().getHostName();
            IPAddress = socket.getInetAddress().getHostAddress();
            port = socket.getPort();
        }
    }
    public HostMessage(String hostName, Socket socket){
        this(socket);
        HostName = hostName;
    }
    public HostMessage(String hostName, String IPAddress){
        HostName = hostName;
        this.IPAddress = IPAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HostMessage)) return false;
        HostMessage that = (HostMessage) o;
        if(InternetSocketaddress == null){
            return Objects.equals(IPAddress, that.IPAddress) &&
                    Objects.equals(HostName, that.HostName) &&
                    port == that.port;
        }else{
            return Objects.equals(InternetSocketaddress, that.InternetSocketaddress);
        }




    }

    @Override
    public int hashCode() {
        if(InternetSocketaddress != null){
            return Objects.hashCode(InternetSocketaddress);
        }else {
            return Objects.hash(HostName, IPAddress, port);
        }
    }

    @Override
    public String toString() {
        String clientmsg = "Client Message{ ";
        clientmsg = clientmsg + "客户机名: " + getHostName() + ", " +
                "客户机地址: " + getIPAddress() + ", " +
                "端口: " + getPort() + " }";
        return clientmsg;
    }

    public String getHostName(){
        return HostName;
    }
    public String getIPAddress(){
        return IPAddress;
    }
    public int getPort(){
        return port;
    }
}
