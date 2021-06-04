package Alpha.Client;

import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.IOStream.AlphaOutputStream;
import Alpha.Server.BroadcastServer.Message;

public interface AlphaClientEvent {
    void initializationAccept(AlphaInputStream acceptor);   // 客户端初始化
    void initializationSend(AlphaOutputStream sender, byte[] data);
    void initializationBroadcastAccept(AlphaMessageInputStream acceptor);
    void initializationBroadcastSend(AlphaMessageOutputStream sender, Message msg);
    void doAccept(AlphaInputStream acceptor);
    void doBroadcastAccept(AlphaMessageInputStream acceptor);
    void connectionFailed(Exception cause);
    void error(Exception cause);

    default boolean doSend(AlphaOutputStream sender, byte[] data){
        return sender.write(data);
    }
    default boolean doSend(AlphaOutputStream sender, String data){
        return sender.writeLine(data);
    }
    default boolean doBroadcastSend(AlphaMessageOutputStream sender, Message msg){
        return sender.WriteMessage(msg);
    }
}
