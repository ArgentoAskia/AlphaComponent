package Alpha.Server.BroadcastServer;

import Alpha.Exceptions.ResponsorException.AlphaResponsorException;
import Alpha.Exceptions.ServerException.AlphaServerException;

import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;

import Alpha.Server.AlphaEvent;
import Alpha.Server.HostMessage;
import Alpha.Server.Responsor;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

public class BroadcastResponsor extends Responsor {
    private AlphaMessageInputStream messageAcceptor;
    private AlphaMessageOutputStream messageSender;

    private class BroadcaseRunnable implements Runnable {
        private LinkedList<LostingMessage> lostingConnection = new LinkedList<>();

        private BroadcastResponsor[] getBroadcaseTargets() throws AlphaResponsorException {
            BroadcastResponsor[] sndresp;
            try {
                sndresp = belongServer.getOtherBroadcastResponsors((BroadcastResponsor) belongServer.getResponsor(getClientMessage()));
                /*
                while (sndresp.length < 1) {
                    Thread.sleep((long) (1000 + Math.random() * 9000));
                    sndresp = belongServer.getOtherBroadcastResponsors((BroadcastResponsor) belongServer.getResponsor(getClientMessage()));
                }

                 */
                return sndresp;
            } catch (AlphaServerException e) {
                throw new AlphaResponsorException("无法获取服务者上的其他服务者句柄!", e);
            }
        }
        private AlphaMessageOutputStream[] getMessageSender(BroadcastResponsor[] Responsor) {
            AlphaMessageOutputStream[] senders;
            senders = new AlphaMessageOutputStream[Responsor.length];
            for (int i = 0; i < senders.length; i++) {
                senders[i] = Responsor[i].getMessageSender();
            }
            return senders;
        }

        private void closeLosting(BroadcastResponsor[] responsors, LinkedList<LostingMessage> LostingConnection) {
            for (LostingMessage lmsg : LostingConnection
            ) {
                System.out.println("要关闭的"+lmsg.getResponsorIndex()+ "客户:" + responsors[lmsg.getResponsorIndex()].getClientMessage().toString());
                responsors[lmsg.getResponsorIndex()].closeThread();
                if (! responsors[lmsg.getResponsorIndex()].isClosed()) {
                    responsors[lmsg.getResponsorIndex()].closeResponsor();
                }
                System.out.println("移出" + lmsg.getResponsorIndex());
            }
            LostingConnection.clear();
        }

        private void getLostingMsg(BroadcastResponsor[] responsors, LinkedList<LostingMessage> LostingConnection,
                                   HostMessage[] lostingClients, Exception[] lostingExcs){
            int i = 0;
            for (LostingMessage lostingClientMsg: LostingConnection
                 ) {
                lostingClients[i] = responsors[lostingClientMsg.getResponsorIndex()].getClientMessage();
                lostingExcs[i] = lostingClientMsg.getCause();
                i++;
            }
        }
        @Override
        public void run() {
            // 服务端Responsor线程只会运行一次的地方

            boolean run = true;
            do {
                System.out.println("--------------------------- 分割线 --------------------------");
                Message acceptMsg = belongServer.getResponsibility().msgInput(messageAcceptor);
                if(acceptMsg == null){
                    System.out.println("出错了!\n位置在msgInput()事件\n原因是:无法接收到信息数据!即将关闭源发送端");
                    break;
                }
                if(respondingThread.isInterrupted()){
                    break;
                }
                try {

                    System.out.println("当前进程:" + this.toString());
                    System.out.println("即将进行转发服务");
                    System.out.println("正在获取转发对象...");

                    BroadcastResponsor[] responsors = getBroadcaseTargets();
                    if (responsors.length < 1){
                        System.out.println("单回传结果");
                        Exception[] exceptions = new Exception[]{new IOException("当前只有一个连接")};
                        run = belongServer.getResponsibility().resultBack(messageSender,null,exceptions, run);
                        if(!run){
                            break;
                        }
                        continue;
                    }
                    AlphaMessageOutputStream[] senders = getMessageSender(responsors);

                    System.out.println("转发目标数:" + senders.length);
                    for (int i = 0; i < responsors.length; i++) {
                        System.out.println("转发对应表:" + responsors[i].getClientMessage() + " <->" + senders[i].toString());
                    }

                    System.out.println("正在转发...");
                    run = belongServer.getResponsibility().broadcastMsg(acceptMsg, senders,lostingConnection);

                    System.out.println("正在回传转发结果...");
                    if(lostingConnection.size() != 0){
                        HostMessage[] lostingClients = new HostMessage[lostingConnection.size()];
                        Exception[] causes = new Exception[lostingConnection.size()];
                        getLostingMsg(responsors, lostingConnection, lostingClients, causes);
                        closeLosting(responsors, lostingConnection);
                        run = belongServer.getResponsibility().resultBack(messageSender,lostingClients, causes, run);
                    }else{
                        run = belongServer.getResponsibility().resultBack(messageSender,null, null, run);
                    }
                    System.out.println("--------------------------- 一轮转发成功 --------------------------");
                } catch (AlphaResponsorException e) {
                    // error
                    break;
                }
            } while (run);
            System.out.println("正在关闭响应者:..." + this.toString());
            closeResponsor();
        }
    }
    private class PPM implements Runnable{

        @Override
        public void run() {

        }
    }

    @Override
    protected void initConnectionIO() throws AlphaResponsorException {
        try {
            messageAcceptor = new AlphaMessageInputStream(clientSocket.getInputStream());
            messageSender = new AlphaMessageOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            throw new AlphaResponsorException(e.getMessage(), e);
        }
    }

    public BroadcastResponsor(Socket conectionSocket, AlphaBroadcastServer belongTo) throws AlphaResponsorException {
        initThread(new BroadcaseRunnable());
        initConnectionConfig(conectionSocket, belongTo);
        initConnectionIO();
    }

    private boolean closeIO(boolean ret) {
        if (!messageSender.close()) {
            messageSender.getCause().printStackTrace();
            ret = false;
        }
        if (!messageAcceptor.close()) {
            messageAcceptor.getCause().printStackTrace();
            ret = false;
        }
        return ret;
    }

    @Override
    public boolean closeResponsor() {
        boolean ret = true;
        ret = closeIO(ret);
        ret = closeConnection(ret);
        return ret;
    }

    public AlphaMessageInputStream getMessageAcceptor() {
        return messageAcceptor;
    }

    public AlphaMessageOutputStream getMessageSender() {
        return messageSender;
    }
}
