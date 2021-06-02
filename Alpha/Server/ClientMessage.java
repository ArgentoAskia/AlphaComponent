package Alpha.Server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

public class ClientMessage {
    private InetSocketAddress InternetSocketaddress;
    private String HostName;
    private String IPAddress;
    private int port;
    public ClientMessage(Socket socket){
        if(socket.getRemoteSocketAddress() instanceof InetSocketAddress){
            InternetSocketaddress  = (InetSocketAddress)socket.getRemoteSocketAddress();
        }else{
            HostName = socket.getInetAddress().getHostName();
            IPAddress = socket.getInetAddress().getHostAddress();
            port = socket.getPort();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientMessage)) return false;
        ClientMessage that = (ClientMessage) o;
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
        return "ClientMessage{" +
                "InternetSocketaddress=" + InternetSocketaddress +
                ", HostName='" + HostName + '\'' +
                ", IPAddress='" + IPAddress + '\'' +
                ", port=" + port +
                '}';
    }

    public String getHostName(){
        if(InternetSocketaddress != null){
            return InternetSocketaddress.getHostName();
        }else{
            return HostName;
        }

    }
    public String getIPAddress(){
        if(InternetSocketaddress != null){
            return InternetSocketaddress.getAddress().getHostAddress();
        }else{
            return IPAddress;
        }
    }
    public int getPort(){
        if(InternetSocketaddress != null){
            return InternetSocketaddress.getPort();
        }else{
            return port;
        }
    }
}
