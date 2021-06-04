package test;

import Alpha.RespInstance.FileTransmission.FileTransmissionResp;
import Alpha.Server.BroadcastServer.AlphaBroadcastServer;

import java.io.*;

public class FileTranTest {
    public static void main(String[] args) throws IOException {
        AlphaBroadcastServer alphaServer = new AlphaBroadcastServer(13251);
        alphaServer.setResponsibility(new FileTransmissionResp("D:\\AlphaFileTranslate", "根目录"));
        alphaServer.waitForConnected();
    }
}
