package Alpha.Server;

import Alpha.AlphaDataStructure.AlphaResponsorSet;
import Alpha.Exceptions.ServerException.AlphaServerException;
import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaOutputStream;
import Alpha.Server.BroadcastServer.AlphaBroadcastServer;
import Alpha.Server.BroadcastServer.BroadcastResponsor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;


/**
 * Alpha服务器用于处理局域网内文件传输、小组交流
 * 可以创建多个Alpha服务器对象来负责不同的任务处理。
 * @author Askia
 * @version 1.0
 */
public class AlphaServer {
    protected ServerSocket serverSocket = null;           // 服务器监听端口
    private Responding responsibility = null;           // 服务器主要任务
    protected AlphaResponsorSet connections = new AlphaResponsorSet();    // 服务器当前所有的连接（连接组）

    // 打包高层异常再抛出
    private void packUpAndThrowHighException(IOException e) throws AlphaServerException {
        throw new AlphaServerException(e.getMessage(), e);
    }
    // 释放响应线程,当用户断开连接时，将匹配的响应线程释放！
    void removeResponsor(ClientMessage clientMessage){
        connections.remove(clientMessage);
    }
    public AlphaEvent getResponsibility(){
        if(hasResponsibility()){
            return responsibility;
        }else{
            return null;
        }
    }


    // pass
    /**
     * 创建一个Alpha服务器并指定该服务器的响应任务
     * <p>
     *     使用该构造器创建的Alpha服务器对象，那么在调用{@link AlphaServer#waitForConnected()}方法启动服务器前，
     *     请先设置端口{@link AlphaServer#setPort(int)},否则将抛出异常
     *
     * @param responsibility 响应任务对象，关于什么是响应任务，参考{@link Responding}
     */
    public AlphaServer(Responding responsibility){
        this.responsibility = responsibility;
    }

    // pass
    /**
     * 创建一个Alpha服务器并指定该服务器的监听端口
     * <p>使用该构造器创建Alpha服务器对象，在开启服务器前，请设置响应任务{@link AlphaServer#setResponsibility(AlphaEvent)},否则将抛出异常
     * @param port  服务器端口
     * @throws AlphaServerException 若创建失败则抛出异常，可以使用{@link AlphaServerException#getMessage()}查看为何抛出此异常（通常为端口被占用）
     */
    public AlphaServer(int port) throws AlphaServerException {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            packUpAndThrowHighException(e);
        }
    }

    // pass
    /**
     * 方便的构造方法，指定Alpha服务器端口和响应任务。
     * @param port  端口
     * @param responsibility    响应任务对象，关于什么是响应任务，参考{@link Responding}
     * @throws AlphaServerException  若创建失败则抛出异常，可以使用{@link AlphaServerException#getMessage()}查看为何抛出此异常（通常为端口被占用）
     */
    public AlphaServer(int port, Responding responsibility) throws AlphaServerException {
        this(port);
        this.responsibility = responsibility;
    }

    // pass
    /**
     * 判断该服务器对象是否已有响应任务
     * @return  如果有，则返回true，否则返回false
     */
    public boolean hasResponsibility(){
        return responsibility != null;
    }

    public void setServerConnectionTime(int timeout) throws AlphaServerException {
        try {
            serverSocket.setSoTimeout(timeout);
        } catch (SocketException e) {
            packUpAndThrowHighException(e);
        }
    }
    public String getServerLocalHostName(){
        return serverSocket.getInetAddress().getHostName();
    }
    public String getServerLocalAddress(){
        return serverSocket.getInetAddress().getHostAddress();
    }
    public int getServerBindingPort(){
        return serverSocket.getLocalPort();

    }
    public boolean closedServer(){
        try{
            AlphaResponsorSet.AlphaHashKeySetIterator iterator = connections.keySetIterator();
            for (iterator.first(); !iterator.isDone(); iterator.next()){
                connections.get(iterator.currrentInstance()).closeResponsor();
            }
            connections.clear();
            serverSocket.close();
            return true;
        }catch (IOException e){
            // todo solution for close
            e.printStackTrace();
            return false;
        }
    }
    public boolean isClosed(){
        return serverSocket.isClosed();
    }

    // pass
    /**
     * 指定服务器的监听端口
     * @param port 端口号
     * @throws AlphaServerException  若设置失败则抛出异常，可以使用{@link AlphaServerException#getMessage()}查看为何抛出此异常（通常为端口被占用）
     */
    public void setPort(int port) throws AlphaServerException {
        try {
            serverSocket.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            packUpAndThrowHighException(e);
        }
    }
    // pass
    /**
     * 设置服务器响应任务，关于什么是响应任务，参考{@link Responding}
     * @param responsibility 响应任务对象
     */
    public void setResponsibility(AlphaEvent responsibility){
        if(this.responsibility == null){
            if(responsibility instanceof Responding){
                this.responsibility = (Responding)responsibility;
            }
        }
    }

    protected ClientMessage checkConnected(Socket connection) throws AlphaServerException {
        ClientMessage client = new ClientMessage(connection);
        if(connections.containsKey(client)){
            throw new AlphaServerException("该IP用户已连接服务器！");
        }
        return client;
    }

    /**
     * 开启服务器，等待用户连接。
     * <p>该方法将开启服务器，并等待客户连接。
     * <p>客户一旦连接服务器，服务器将会给连接的客户分配一个Responsor对象来专门处理该用户的请求
     * <p>关于何为Responsor对象，参考{@link Responsor}
     * @throws AlphaServerException 服务器未注册端口、未指明响应任务都会抛出该异常
     */
    public void waitForConnected() throws AlphaServerException {

        if(serverSocket == null){

            throw new AlphaServerException("服务端未注册端口，请先设置端口！");
        }
        if(responsibility == null){
            throw new AlphaServerException("未指定服务器任务，请先指定！");
        }

        Socket sock;
        try {
            while((sock = serverSocket.accept()) != null){
                // get client message and 检查连接已存在
                ClientMessage client = checkConnected(sock);

                // 分配Responsor对象处理用户请求
                Responsor responsor = new Responsor(sock, this);

                // 将连接和响应服务者放进服务器服务集合中
                connections.put(client,responsor);

                // 响应服务端连接事件
                connectionIn(client,responsor.getInputStream(), responsor.getOutputStream());

                // Responsor开始处理用户请求
                responsor.beginResponding();
            }
        } catch (IOException e) {
            connectionFailed(e);    // 传递高层异常给fail方法？
        }
    }

    // todo 当前服务端的所有连接组!
    // connections操作!
    public ClientMessage[] getAllClientMessage(){
        ClientMessage[] clientMessages = new ClientMessage[connections.size()];
        AlphaResponsorSet.AlphaHashKeySetIterator iterator = connections.keySetIterator();
        int i = 0;
        for(iterator.first(); !iterator.isDone(); iterator.next(), i++){
            clientMessages[i] = iterator.currrentInstance();
        }
        return clientMessages;
    }
    public AlphaResponsorSet.AlphaHashKeySetIterator alphaHashKeySetIterator(){
        return connections.keySetIterator();
    }

    private Responsor[] getAllResponsors(){
        Responsor[] responsors = new Responsor[connections.size()];
        AlphaResponsorSet.AlphaHashKeySetIterator iterator = connections.keySetIterator();
        int i = 0;
        for(iterator.first(); !iterator.isDone(); iterator.next(), i++){
            responsors[i] = connections.get(iterator.currrentInstance());
        }
        return responsors;
    }

    public Responsor getResponsor(ClientMessage client){
        return connections.get(client);
    }

    public BroadcastResponsor[] getOtherBroadcastResponsors(BroadcastResponsor resps) throws AlphaServerException {
        if(this instanceof AlphaBroadcastServer){
            BroadcastResponsor[] responsors = new BroadcastResponsor[connections.size() - 1];
            AlphaResponsorSet.AlphaHashKeySetIterator iterator = connections.keySetIterator();
            int i = 0;
            for(iterator.first(); !iterator.isDone(); iterator.next()){
                BroadcastResponsor tmp = (BroadcastResponsor)connections.get(iterator.currrentInstance());
                if(tmp == resps){
                    continue;
                }else{
                    responsors[i] = tmp;
                    i++;
                }
            }
            return responsors;
        }
        throw new AlphaServerException("你所创建的服务器并不是转发服务器!无法使用该方法!");
    }
    // event
    private void connectionIn(ClientMessage newClient, AlphaInputStream acceptor, AlphaOutputStream sender){
        responsibility.connectionIn(newClient, getAllClientMessage());
        responsibility.initialization(newClient, acceptor, sender);
    }
    private void connectionFailed(Exception cause){
        responsibility.connectionFailed(cause);
    }
}
