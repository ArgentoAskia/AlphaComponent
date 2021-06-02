package Alpha.Server.BroadcastServer;

import Alpha.Exceptions.ServerException.AlphaServerException;
import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.Server.*;

import java.io.IOException;
import java.net.Socket;

/**
 * 转发服务器组件.
 * <p>
 * <strong>使用方法:</strong>参考下面代码<br>
 * <pre>
 * <code>
 *      // 设置端口,创建文件传输服务器
 *      AlphaBroadcastServer  fileTransmissionServer = new AlphaBroadcastServer(13251);
 *
 *      //指定服务器响应任务
 *      alphaServer.setResponsibility(new FileTransmissionResp("D:\\AlphaFileTranslate", "根目录"));
 *
 *      //监听用户连接
 *      alphaServer.waitForConnected();
 * </code>
 * </pre>
 *
 *
 */
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

    /**
     * 创建一个Alpha服务器并指定该服务器的监听端口和响应服务
     * @param port  端口
     * @param responding    响应服务
     * @throws AlphaServerException 若创建失败则抛出异常，可以使用{@link AlphaServerException#getMessage()}查看为何抛出此异常（通常为端口被占用）
     */
    public AlphaBroadcastServer(int port, BroadcasetResponding responding) throws AlphaServerException {
        super(port, null);
        this.responding = responding;
    }

    /**
     * 判断当前服务器时候有响应服务对象.
     * @return 如果服务器有指定服务的话返回:true,没有指定服务返回:false
     */
    @Override
    public boolean hasResponsibility() {
        return responding != null;
    }

    /**
     * 设置当前服务器响应服务对象
     * @param responsibility 响应任务对象
     */
    @Override
    public void setResponsibility(AlphaEvent responsibility) {
        if(this.responding == null){
            if(responsibility instanceof BroadcasetResponding){
                this.responding = (BroadcasetResponding)responsibility;
            }
        }

    }

    /**
     * 开启服务器，等待用户连接。
     * <p>该方法将开启服务器，并等待客户连接。
     * <p>客户一旦连接服务器，服务器将会给连接的客户分配一个{@link BroadcastResponsor}对象来专门处理该用户的请求
     * <p>在{@link BroadcastResponsor}定义了两种转发方式
     * @throws AlphaServerException 服务器未注册端口、未指明响应任务都会抛出该异常
     */
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
                HostMessage client = checkConnected(sock);

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

    private void connectionIn(HostMessage newClient, AlphaMessageInputStream acceptor, AlphaMessageOutputStream sender, AlphaMessageOutputStream[] othersSenders){
        responding.connectionIn(newClient, getAllClientMessage(), othersSenders);
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

