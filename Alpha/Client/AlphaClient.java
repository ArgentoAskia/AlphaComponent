package Alpha.Client;

import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaOutputStream;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class AlphaClient {
    private Socket connectionHost;
    private AlphaOutputStream sender;
    private AlphaInputStream acceptor;
    private IOException cause;

    public AlphaClient(String IP, int port){
        try {
            connectionHost = new Socket(IP,port);
            acceptor = new AlphaInputStream(connectionHost.getInputStream());
            sender = new AlphaOutputStream(connectionHost.getOutputStream());
        } catch (UnknownHostException e) {
            // todo solution for UnknownHostException
            e.printStackTrace();
        } catch (IOException e){
            // exceptions
        }

    }

    // 方便方法
    public boolean sendData(int data){
        if(sender.write(data))
            return true;
        else{
            cause = sender.getCause();
            return false;
        }
    }
    public boolean sendByteData(byte[] data){
        if(sender.write(data))
            return true;
        else{
            cause = sender.getCause();
            return false;
        }
    }
    public boolean sendByteData(byte[] b, int off, int len){
        if(sender.write(b, off, len))
            return true;
        else{
            cause = sender.getCause();
            return false;
        }
    }
    public int readData(){
        return acceptor.read();
    }
    public byte[] ReadByteData(){
        return acceptor.readAllBytes();
    }



    public AlphaOutputStream getSender(){
        return sender;
    }
    public AlphaInputStream getAcceptor(){
        return acceptor;
    }

    public boolean closeClient(){
        boolean ret = true;
        if(!sender.close()){
            sender.getCause().printStackTrace();
            ret = false;
        }
        if(!acceptor.close()){
            acceptor.getCause().printStackTrace();
            ret = false;
        }
        try {
            connectionHost.close();
        } catch (IOException e) {
            e.printStackTrace();
            ret = false;
        }
        return ret;
    }
    // 实际上该方法调用一次之后就应该将cause字段置null
    public IOException getCause(){
        return cause;
    }


}
