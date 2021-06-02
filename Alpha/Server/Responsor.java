package Alpha.Server;

import Alpha.Exceptions.ResponsorException.AlphaResponsorException;
import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaOutputStream;

import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;

public class Responsor {
    protected Socket clientSocket;              // 对方的socket
    protected AlphaServer belongServer;  // 响应所属服务器
    protected Thread respondingThread = null;   // 响应任务线程

    private AlphaInputStream inputStream;    // 接收字节流
    private AlphaOutputStream outputStream;  // 发送字节流

    private boolean aynFlag;            // 异步繁忙字段
    private Thread connectedThread = null;  // 连接线程(保留不用)

    private class PPP implements Runnable {

        @Override
        public void run() {
            // 一旦keepRunning == false, 代表响应结束了,就要进行释放操作.
            boolean keepRunning;
            do {
                keepRunning = belongServer.getResponsibility().run(inputStream, outputStream);
            } while (keepRunning);

            // 关闭响应者,并将响应者从服务器中移除
            if (!closeResponsor()) {
                // todo solutions for close exception
                respondingThread.interrupt();
            }

            // 响应连接丢失事件
            IOException cause;
            if ((cause = inputStream.getCause()) != null) ;
            else
                cause = outputStream.getCause();
            belongServer.getResponsibility().connectionLost(getClientMessage(), belongServer.getAllClientMessage(), cause);
        }
    }

    protected void initThread(Runnable mode){
        respondingThread = new Thread(mode);
    }
    protected void initConnectionConfig(Socket conectionSocket, AlphaServer belongTo){
        clientSocket = conectionSocket;
        belongServer = belongTo;
    }
    protected void initConnectionIO() throws AlphaResponsorException {
        try {
            inputStream = new AlphaInputStream(clientSocket.getInputStream());
            outputStream = new AlphaOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            throw new AlphaResponsorException(e.getMessage(), e);
        }
    }

    protected Responsor(){
        // 守护线程代码
    }
    public Responsor(Socket conectionSocket, AlphaServer belongTo) throws AlphaResponsorException {
        this();
        initThread(new PPP());
        initConnectionConfig(conectionSocket, belongTo);
        initConnectionIO();
    }
    public void beginResponding() {
        respondingThread.start();
    }

    protected ClientMessage getClientMessage() {
        return new ClientMessage(clientSocket);
    }

    public boolean isClosed() {
        return clientSocket.isClosed();
    }

    private boolean closeIO(boolean ret){
        if (!inputStream.close()) {
            inputStream.getCause().printStackTrace();
            ret = false;
        }
        if (!outputStream.close()) {
            outputStream.getCause().printStackTrace();
            ret = false;
        }
        return ret;
    }
    protected boolean closeConnection(boolean ret){
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            ret = false;
        }
        belongServer.removeResponsor(getClientMessage());
        return ret;
    }
    public boolean closeResponsor() {
        boolean ret = true;
        ret = closeIO(ret);
        ret = closeConnection(ret);
        return ret;
    }
    public void closeThread(){
        respondingThread.interrupt();
    }

    // 测试心跳连接,用于守护连接
    public boolean testUrgent() {
        try {
            clientSocket.sendUrgentData(0xff);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public AlphaInputStream getInputStream() {
        return inputStream;
    }

    public AlphaOutputStream getOutputStream() {
        return outputStream;
    }
}
