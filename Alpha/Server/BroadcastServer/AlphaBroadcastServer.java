package Alpha.Server.BroadcastServer;

import Alpha.Exceptions.ServerException.AlphaServerException;
import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.Server.*;

import java.io.IOException;
import java.net.Socket;


public class AlphaBroadcastServer extends AlphaServer {
    private BroadcasetResponding responding = null;
    public AlphaBroadcastServer(BroadcasetResponding responding){
        super(null);
        this.responding = responding;
    }

    /**
     * 创建一个Alpha服务器并指定该服务器的监听端口
     * <p>使用该构造器创建Alpha服务器对象，在开启服务器前，请设置响应任务{@link AlphaServer#setResponsibility(Alpha.Server.AlphaEvent)},否则将抛出异常
     *
     * @param port 服务器端口
     * @throws AlphaServerException 若创建失败则抛出异常，可以使用{@link AlphaServerException#getMessage()}查看为何抛出此异常（通常为端口被占用）
     */
    public AlphaBroadcastServer(int port) throws AlphaServerException {
        super(port);
    }
    public AlphaBroadcastServer(int port, BroadcasetResponding responding) throws AlphaServerException {
        super(port, null);
        this.responding = responding;
    }

    @Override
    public boolean hasResponsibility() {
        return responding != null;
    }

    @Override
    public void setResponsibility(AlphaEvent responsibility) {
        if(this.responding == null){
            if(responsibility instanceof BroadcasetResponding){
                this.responding = (BroadcasetResponding)responsibility;
            }
        }

    }

    @Override
    public void waitForConnected() throws AlphaServerException {
        if(serverSocket == null){

            throw new AlphaServerException("服务端未注册端口，请先设置端口！");
        }
        if(responding == null){
            throw new AlphaServerException("未指定服务器任务，请先指定！");
        }

        Socket sock;
        try {
            while((sock = serverSocket.accept()) != null){
                // get client message and 检查连接已存在
                ClientMessage client = checkConnected(sock);

                // 分配Responsor对象处理用户请求
                BroadcastResponsor responsor = new BroadcastResponsor(sock, this);

                // 将连接和响应服务者放进服务器服务集合中
                connections.put(client,responsor);

                // 响应服务端连接事件
                connectionIn(client,responsor.getMessageAcceptor(), responsor.getMessageSender(), getBroadcastRespSenders(getOtherBroadcastResponsors(responsor)));

                // Responsor开始处理用户请求
                responsor.beginResponding();
            }
        } catch (IOException e) {
            connectionFailed(e);    // 传递高层异常给fail方法？
        }
    }
    public AlphaEvent getResponsibility(){
        if(hasResponsibility()){
            return responding;
        }else{
            return null;
        }
    }
    private void connectionIn(ClientMessage newClient, AlphaMessageInputStream acceptor, AlphaMessageOutputStream sender, AlphaMessageOutputStream[] othersSenders){
        responding.connectionIn(newClient, getAllClientMessage());
        responding.initialization(newClient, acceptor, sender, othersSenders);
    }
    private void connectionFailed(Exception cause){
        responding.connectionFailed(cause);
    }
    private AlphaMessageOutputStream[] getBroadcastRespSenders(BroadcastResponsor[] responsors){
        AlphaMessageOutputStream[] senders;
        senders = new AlphaMessageOutputStream[responsors.length];
        for (int i = 0; i < senders.length; i++) {
            senders[i] = responsors[i].getMessageSender();
        }
        return senders;
    }
}

