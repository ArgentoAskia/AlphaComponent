package Alpha.RespInstance.BroadCastChatting;

import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.Server.BroadcastServer.BroadcasetResponding;
import Alpha.Server.BroadcastServer.LostingMessage;
import Alpha.Server.BroadcastServer.Message;
import Alpha.Server.HostMessage;


import java.util.LinkedList;


public class ChattingResp extends BroadcasetResponding {

    @Override
    public void connectionIn(HostMessage newClient, HostMessage[] allClients) {
        System.out.println("新连接进来了:" + newClient.toString());
        System.out.println("当前连接总数:" + allClients.length);
    }

    @Override
    public void connectionLost(HostMessage hostMessage, HostMessage[] allClients, Exception cause) {
        System.out.println("连接退出了:" + hostMessage.toString());
        System.out.println("退出原因:" + cause.getMessage());
        System.out.println("当前连接数:" + allClients.length);
    }

    @Override
    public void connectionOut(HostMessage outClient) {

    }

    @Override
    public void connectionFailed(Exception cause) {

    }

    @Override
    public void initialization(HostMessage newClient, AlphaMessageInputStream acceptor, AlphaMessageOutputStream rebacker, AlphaMessageOutputStream[] otherSenders) {

    }

    @Override
    public Message msgInput(AlphaMessageInputStream acceptor) {
        Message msg = acceptor.readMessage();
        if(msg != null){
            System.out.println("接收到来自(" +
                    msg.getHostName() + "/" + msg.getIPAddress() +
                    ")的信息: " + msg.readString());
        }
        return msg;
    }

    @Override
    public boolean broadcastMsg(Message msg, AlphaMessageOutputStream[] otherSenders, LinkedList<LostingMessage> failSendIndex) {
        int i = 0;
        for (AlphaMessageOutputStream sender:otherSenders
        ) {
            sender.flush();
            if(sender.WriteMessage(msg)){
                System.out.println(sender.toString() + ": 发送成功!");
            }else{
                failSendIndex.add(new LostingMessage(i, sender.getCause()));
            }
            i++;
        }
        return true;
    }

    /*
        调试方法
    public boolean resultBack(AlphaMessageOutputStream rebacker, HostMessage[] lostingClientMsgs, Exception[] causes, boolean run) {

        Message rbmsg = new Message(new HostMessage("服务器", "服务器地址"));
        if(lostingClientMsgs == null){
            rbmsg.writeString("转发成功");
        }else{
            rbmsg.writeString("有" + lostingClientMsgs.length + "个客户转发失败");
        }
        rebacker.flush();
        return rebacker.WriteMessage(rbmsg);
        // 发送失败就进行本地化!
    }

     */
}
