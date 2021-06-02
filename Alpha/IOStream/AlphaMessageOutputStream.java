package Alpha.IOStream;

import Alpha.Server.BroadcastServer.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class AlphaMessageOutputStream {
    private ObjectOutputStream messageSender;
    private Exception cause;

    public AlphaMessageOutputStream(OutputStream out){
        try {
            messageSender = new ObjectOutputStream(out);
        } catch (IOException e) {
            cause = e;
            e.printStackTrace();
        }
    }
    public AlphaMessageOutputStream(AlphaOutputStream out){
        this(out.getOutputStream());
    }
    public Exception getCause(){
        return cause;
    }
    public boolean WriteMessage(Message msg){
        try {
            messageSender.writeObject(msg);
            return true;
        }catch (IOException e) {
            cause = e;
            e.printStackTrace();
            return false;
        }
    }
    private boolean writeObject(Object obj){
        try {
            messageSender.writeObject(obj);
            return true;
        } catch (IOException e) {
            cause = e;
            e.printStackTrace();
            return false;
        }

    }
    public boolean close(){
        try {
            messageSender.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void flush(){
        try {
            messageSender.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
