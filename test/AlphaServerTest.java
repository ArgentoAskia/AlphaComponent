package test;

import Alpha.Exceptions.ServerException.AlphaServerException;
import Alpha.RespInstance.ServerTime.ServerTimeResp;
import Alpha.Server.AlphaServer;


public class AlphaServerTest {
        public static void main(String[] args) throws AlphaServerException {
        AlphaServer alphaServer = new AlphaServer(13251);
        alphaServer.setResponsibility(new ServerTimeResp());
        alphaServer.waitForConnected();
    }
}