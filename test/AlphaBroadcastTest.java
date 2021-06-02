package test;

import Alpha.Exceptions.ServerException.AlphaServerException;
import Alpha.RespInstance.ChattingResp;
import Alpha.Server.AlphaServer;
import Alpha.Server.BroadcastServer.AlphaBroadcastServer;

public class AlphaBroadcastTest {
    public static void main(String[] args) throws AlphaServerException {
        AlphaBroadcastServer alphaServer = new AlphaBroadcastServer(13251);
        alphaServer.setResponsibility(new ChattingResp());
        alphaServer.waitForConnected();
    }
}
