package test;

import Alpha.Client.AlphaClient;
import Alpha.Client.AlphaClientEvent;
import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.IOStream.AlphaOutputStream;
import Alpha.Server.BroadcastServer.Message;

import java.util.Scanner;

public class BroadcastClient {
    AlphaClient client = new AlphaClient("localhost","localhost", 13251, new clientEvent());
    public static void main(String[] args) {
        BroadcastClient broadcastClient = new BroadcastClient();
        broadcastClient.client.broadcastAccepting(100);
        while(true){
            Scanner scanner = new Scanner(System.in);
            Message msg = new Message(broadcastClient.client.getHostMessage());
            if(scanner.hasNextLine()){
                String s = scanner.nextLine();
                if(s.equals("exit")){
                    break;
                }
                msg.writeString(s);
                System.out.println("msg信息:" + msg.readString());
                broadcastClient.client.sendMessage(msg);
            }
        }
    }
}
class clientEvent implements AlphaClientEvent{

    @Override
    public void initializationAccept(AlphaInputStream acceptor) {

    }

    @Override
    public void initializationSend(AlphaOutputStream sender, byte[] data) {

    }

    @Override
    public void initializationBroadcastAccept(AlphaMessageInputStream acceptor) {

    }

    @Override
    public void initializationBroadcastSend(AlphaMessageOutputStream sender, Message msg) {

    }

    @Override
    public void doAccept(AlphaInputStream acceptor) {

    }

    @Override
    public void doBroadcastAccept(AlphaMessageInputStream acceptor) {
        Message ret = null;
        ret = acceptor.readMessage();
        System.out.println(ret.getHostName() + "(" + ret.getIPAddress() + ")说: " + ret.readString());
    }

    @Override
    public void connectionFailed(Exception cause) {

    }

    @Override
    public void error(Exception cause) {

    }
}
