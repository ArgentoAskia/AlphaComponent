package Alpha.IOStream;

import Alpha.Server.BroadcastServer.Message;

import java.io.*;

public class AlphaMessageInputStream {
    private ObjectInputStream messageAcceptor;
    private Exception cause;

    public AlphaMessageInputStream(InputStream in){
        try {
            messageAcceptor = new ObjectInputStream(in);
        } catch (IOException e) {
            e.printStackTrace();
            cause = e;
        }
    }
    public AlphaMessageInputStream(AlphaInputStream in){
        this(in.getInputStream());
    }
    public Exception getCause(){
        return cause;
    }
    public Message readMessage(){
        Object obj = readObject();
        if(obj instanceof Message){
            Message msg = (Message) obj;
            return msg;
        }
        return null;
    }

    private Object readObject(){
        Object obj;
        boolean block = false;
        do{
            try {
                obj = messageAcceptor.readObject();
                return obj;
            } catch (EOFException e) {
                block = true;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                block = false;
                cause = e;
            }
        }while(block);
        return null;
    }
    public boolean close(){
        try {
            messageAcceptor.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
