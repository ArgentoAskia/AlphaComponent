package Alpha.Server;

import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.Server.BroadcastServer.LostingMessage;
import Alpha.Server.BroadcastServer.Message;

import java.util.LinkedList;


public abstract class Responding implements AlphaEvent{

    // 任务处理
    // 注意 accept（）方法获取到的Socket与客户端的不是同一个，accept(）方法获取到的socket与客户区的不相通！


    @Override
    public void initialization(HostMessage newClient, AlphaMessageInputStream acceptor, AlphaMessageOutputStream rebacker, AlphaMessageOutputStream[] otherSenders) {

    }

    @Override
    public boolean broadcastMsg(Message msg, AlphaMessageOutputStream[] otherSenders, LinkedList<LostingMessage> failSendIndex) {
        return false;
    }

    @Override
    public boolean resultBack(AlphaMessageOutputStream rebacker, HostMessage[] lostingClientMsgs, Exception[] causes, boolean run) {
        return false;
    }

    @Override
    public Message msgInput(AlphaMessageInputStream acceptor) {
        return null;
    }
}
