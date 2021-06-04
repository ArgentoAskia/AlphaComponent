package Alpha.Client;

import Alpha.Exceptions.ClientException.ClientAcceptingException;
import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.IOStream.AlphaOutputStream;
import Alpha.Server.BroadcastServer.Message;
import Alpha.Server.HostMessage;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

public class AlphaClient {
    private Socket connectionHost;
    private HostMessage clientMessage;

    private AlphaOutputStream sender;
    private AlphaInputStream acceptor;
    private AlphaMessageOutputStream msgSender;
    private AlphaMessageInputStream msgAcceptor;
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
        }else if(initFlag == AlphaClientConstants.BROADCAST_INIT_ACCEPT){
            eventInstance.initializationBroadcastAccept(msgAcceptor);
        }

    }
    public AlphaClient(String IP, int port, AlphaClientEvent eventInst){
        try {
            eventInstance = eventInst;
            connectionHost = new Socket(IP, port);
            acceptor = new AlphaInputStream(connectionHost.getInputStream());
            sender = new AlphaOutputStream(connectionHost.getOutputStream());
            // 注意顺序
            msgSender = new AlphaMessageOutputStream(sender);
            msgAcceptor = new AlphaMessageInputStream(acceptor);
            clientMessage = new HostMessage(connectionHost);
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
    public AlphaClient(String hostName, String IP, int port, AlphaClientEvent eventInst){
        try {
            connectionHost = new Socket(IP,port);
            acceptor = new AlphaInputStream(connectionHost.getInputStream());
            sender = new AlphaOutputStream(connectionHost.getOutputStream());
            // 注意顺序
            msgSender = new AlphaMessageOutputStream(sender);
            msgAcceptor = new AlphaMessageInputStream(acceptor);
            clientMessage = new HostMessage(hostName,connectionHost);
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

    public AlphaClient(String IP, int port, AlphaClientEvent eventInst, int acceptInitFlag){
        this(IP, port, eventInst);
        if(cause == null){
            if(acceptInitFlag == AlphaClientConstants.INIT_ACCEPT || acceptInitFlag == AlphaClientConstants.BROADCAST_INIT_ACCEPT)
            initialization(acceptInitFlag, null);
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
        if(readingTimer != null){
            return;
        }
        reading = true;
        // todo setName()
        readingTimer = new Timer();
        readingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(acceptor.available() > 0){
                    eventInstance.doAccept(acceptor);
                }
            }
        }, 100, mileSeconds);
    }
    public void broadcastAccepting(int mileSeconds){
        if(readingTimer != null ){
            return;
        }
        reading = true;
        readingTimer = new Timer();
        readingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                eventInstance.doBroadcastAccept(msgAcceptor);
            }
        }, 10, mileSeconds);
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
        eventInstance.doSend(sender, data);
    }
    public void sendLineData(String data){
        eventInstance.doSend(sender,data);
    }

    public void sendMessage(Message msgData){
        eventInstance.doBroadcastSend(msgSender,msgData);

    }
    public void closeReading(){
        if(readingTimer != null){
            readingTimer.cancel();
            readingTimer = null;
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

    public HostMessage getHostMessage(){
        return clientMessage;
    }

}
