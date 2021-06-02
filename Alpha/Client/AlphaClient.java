package Alpha.Client;

import Alpha.Exceptions.ClientException.ClientAcceptingException;
import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaOutputStream;

import javax.swing.Timer;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class AlphaClient {
    private Socket connectionHost;
    private AlphaOutputStream sender;
    private AlphaInputStream acceptor;
    private Exception cause;
    private Timer readingTimer = null;

    private AlphaClientEvent eventInstance;
    private boolean reading = false;

    private void initialization(int initFlag, byte[] data){
        if(initFlag == AlphaClientConstants.INIT_ACCEPT){       // read
            while(acceptor.available() == 0){}  // block
            eventInstance.initializationAccept(acceptor);
        }else if(initFlag == 2){
            eventInstance.initializationSend(sender, data);
        }else if(initFlag == AlphaClientConstants.INIT_ACCEPTSEND){

            eventInstance.initializationAccept(acceptor);
            eventInstance.initializationSend(sender, data);
        }else if(initFlag == AlphaClientConstants.INIT_SENDACCEPT){
            eventInstance.initializationSend(sender, data);
            while(acceptor.available() == 0){}  // block
            eventInstance.initializationAccept(acceptor);
        }

    }
    public AlphaClient(String IP, int port, AlphaClientEvent eventInst){
        try {
            connectionHost = new Socket(IP,port);
            acceptor = new AlphaInputStream(connectionHost.getInputStream());
            sender = new AlphaOutputStream(connectionHost.getOutputStream());
            eventInstance = eventInst;
        } catch (UnknownHostException e) {
            // todo solution for UnknownHostException
            e.printStackTrace();
            cause = e;
            eventInstance.connectionFailed(e);
        } catch (IOException e){
            // exceptions
            eventInstance.error(e);
            System.exit(-1);
        }
    }
    public AlphaClient(String IP, int port, AlphaClientEvent eventInst, byte[] data){
        this(IP, port, eventInst);
        if(cause == null) {
            if (data == null) {
                initialization(0, data);
            } else {
                initialization(2, data);
            }
        }
    }

    public AlphaClient(String IP, int port, AlphaClientEvent eventInst, int initFlag){
        this(IP, port, eventInst);
        if(cause == null){
            initialization(AlphaClientConstants.INIT_ACCEPT, null);
        }

    }

    public AlphaClient(String IP, int port, AlphaClientEvent eventInst, int initFlag, byte[] data){
        this(IP, port, eventInst);
        if(cause == null){
            if(initFlag == AlphaClientConstants.INIT_ACCEPT){
                initialization(AlphaClientConstants.INIT_ACCEPT, null);
            }else{
                initialization(initFlag, data);
            }
        }
    }
    public void accepting(int mileSeconds) {
        if(readingTimer != null && readingTimer.isRunning()){
            return;
        }
        reading = true;
        readingTimer = new Timer(mileSeconds, e -> {
            if(acceptor.available() > 0){
                eventInstance.doAccept(acceptor);
            }
        });
        readingTimer.start();
    }
    public synchronized byte[] acceptData() throws ClientAcceptingException {
        if(reading){
            throw new ClientAcceptingException();
        }else{
            while(acceptor.available() == 0){}  // block
            return acceptor.readAllBytes();
        }
    }
    public void sendData(byte[] data){
        sender.write(data);
    }
    public void sendData(String data){
        sender.writeLine(data);
    }
    public void closeReading(){
        if(readingTimer != null && readingTimer.isRunning()){
            readingTimer.stop();
            reading = false;
        }
    }

    public boolean closeClient(){
        if(reading){
            closeReading();
        }
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
    public Exception getCause(){
        Exception exc = cause;
        cause = null;
        return exc;
    }

}
