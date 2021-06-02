package Alpha.Server;

import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.IOStream.AlphaOutputStream;
import Alpha.Server.BroadcastServer.LostingMessage;
import Alpha.Server.BroadcastServer.Message;

import java.util.LinkedList;
import java.util.Vector;

public interface AlphaEvent {

    void connectionIn(ClientMessage newClient, ClientMessage[] allClients);
    void connectionLost(ClientMessage clientMessage, ClientMessage[] allClients, Exception cause);
    void connectionOut(ClientMessage outClient);
    void connectionFailed(Exception cause);
    void initialization(ClientMessage newClient, AlphaInputStream acceptor, AlphaOutputStream sender);
    void initialization(ClientMessage newClient, AlphaMessageInputStream acceptor, AlphaMessageOutputStream rebacker, AlphaMessageOutputStream[] otherSenders);
    boolean run(AlphaInputStream acceptor, AlphaOutputStream sender);
    Message msgInput(AlphaMessageInputStream acceptor);
    boolean broadcastMsg(Message msg, AlphaMessageOutputStream[] otherSenders, LinkedList<LostingMessage> failSendIndex);

    static void delay(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // 重新处理
            // todo solution for intereter
            e.printStackTrace();
        }
    }
    default boolean resultBack(AlphaMessageOutputStream rebacker, ClientMessage[] lostingClientMsgs, Exception[] causes, boolean run) {
        return run;
    }
}
