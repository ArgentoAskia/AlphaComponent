package Alpha.Server.BroadcastServer;

import Alpha.Server.ClientMessage;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Message implements Serializable {
    private static final long serialVersionUID = 460364234337780681L;
    private String HostName;
    private String IPAddress;

    private byte[] data;
    public Message(Socket client){
            HostName = client.getLocalAddress().getHostName();
            IPAddress = client.getLocalAddress().getHostAddress();
    }
    public Message(ClientMessage localAddress){
        HostName = localAddress.getHostName();
        IPAddress = localAddress.getIPAddress();
    }
    public Message(){

    }
    public void writeString(String s){
        if(data == null){
            data = s.getBytes();
        }
    }
    public String readString(){
        if(data != null){
            return new String(data);
        }
        return null;
    }
    public static Message createStringMessage(Socket client, String s){
        Message msg = new Message(client);
        msg.writeString(s);
        return msg;
    }
}
