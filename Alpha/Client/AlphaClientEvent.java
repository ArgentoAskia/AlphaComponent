package Alpha.Client;

import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaOutputStream;

public interface AlphaClientEvent {
    void initializationAccept(AlphaInputStream acceptor);   // 客户端初始化
    void initializationSend(AlphaOutputStream sender, byte[] data);
    void doAccept(AlphaInputStream acceptor);
    void doSend(AlphaOutputStream sender, byte[] data);
    void connectionFailed(Exception cause);
    void error(Exception cause);
}
