package Alpha.Server.BroadcastServer;

import Alpha.Server.HostMessage;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 460364234337780681L;
    private String HostName;
    private String IPAddress;

    private Object data;
    public Message(HostMessage localAddress){
        HostName = localAddress.getHostName();
        IPAddress = localAddress.getIPAddress();
    }
    public Message(){

    }

    private void writeAnything(Object anything){
        if(data == null){
            data = anything;
        }
    }
    private Object readAnything(){
        if(data != null){
            return data;
        }
        return null;
    }

    public void writeString(String s){
        writeAnything(s);
    }
    public String readString(){
        Object object = readAnything();
        if(object instanceof String)
            return (String)object;
        else
            return null;
    }

    public void writeObject(Object obj){
        writeAnything(obj);
    }
    public Object readObject(){
        return readAnything();
    }
    public String getHostName(){
        return HostName;
    }
    public String getIPAddress() {
        return IPAddress;
    }
}
