package Alpha.Server;

import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.IOStream.AlphaOutputStream;
import Alpha.Server.BroadcastServer.LostingMessage;
import Alpha.Server.BroadcastServer.Message;

import java.util.LinkedList;

public interface AlphaEvent {

    void connectionIn(HostMessage newClient, HostMessage[] allClients);
    void connectionLost(HostMessage hostMessage, HostMessage[] allClients, Exception cause);
    void connectionOut(HostMessage outClient);
    void connectionFailed(Exception cause);
    void initialization(HostMessage newClient, AlphaInputStream acceptor, AlphaOutputStream sender);
    void initialization(HostMessage newClient, AlphaMessageInputStream acceptor, AlphaMessageOutputStream rebacker, AlphaMessageOutputStream[] otherSenders);
    boolean run(AlphaInputStream acceptor, AlphaOutputStream sender);
    Message msgInput(AlphaMessageInputStream acceptor);
    boolean broadcastMsg(Message msg, AlphaMessageOutputStream[] otherSenders, LinkedList<LostingMessage> failSendIndex);

    static void delay(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // 重新处理
            // todo solution for intereter
            e.printStackTrace();
        }
    }
    default boolean resultBack(AlphaMessageOutputStream rebacker, HostMessage[] lostingClientMsgs, Exception[] causes, boolean run) {
        return run;
    }
    default void connectionIn(HostMessage newClient, HostMessage[] allClients, AlphaMessageOutputStream[] senders){
        connectionIn(newClient, allClients);
    }
    default void connectionLost(HostMessage hostMessage, HostMessage[] allClients, Exception cause, AlphaMessageOutputStream[] senders){
        connectionLost(hostMessage, allClients, cause);
    }
    default void connectionOut(HostMessage outClient, AlphaMessageOutputStream[] senders){
        connectionOut(outClient);
    }
}
