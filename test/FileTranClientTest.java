package test;

import Alpha.Client.AlphaClient;
import Alpha.Client.AlphaClientConstants;
import Alpha.Client.AlphaClientEvent;
import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.IOStream.AlphaOutputStream;
import Alpha.RespInstance.FileTransmission.DirStructure;
import Alpha.RespInstance.FileTransmission.FileStructure;
import Alpha.Server.BroadcastServer.Message;

import java.io.IOException;
import java.util.Scanner;

public class FileTranClientTest {
    AlphaClient client = new AlphaClient("localhost", 13251, new event(), AlphaClientConstants.BROADCAST_INIT_ACCEPT);
    public static void main(String[] args) {
        FileTranClientTest broadcastClient = new FileTranClientTest();
        broadcastClient.client.broadcastAccepting(100);
        Scanner scanner = new Scanner(System.in);

        if (scanner.hasNextLine()) {

            /*
                DirStructure dirStructure = new DirStructure();
                dirStructure.setCurrentPath("根目录\\Askia");
                Message msg = new Message(broadcastClient.client.getHostMessage());
                msg.writeObject(dirStructure);
                broadcastClient.client.sendMessage(msg);
               */


            DirStructure dirStructure = new DirStructure();
            dirStructure.setCurrentPath("根目录\\Askia\\生肉", DirStructure.Operation.newDir);
            Message msg = new Message(broadcastClient.client.getHostMessage());
            msg.writeObject(dirStructure);
            broadcastClient.client.sendMessage(msg);
        }



        if (scanner.hasNextLine()) {
            DirStructure dirStructure1 = new DirStructure();
            dirStructure1.setCurrentPath("根目录\\Askia\\生肉", DirStructure.Operation.deleteDir);
            Message msg1 = new Message(broadcastClient.client.getHostMessage());
            msg1.writeObject(dirStructure1);
            broadcastClient.client.sendMessage(msg1);
        }


        /*
        if(scanner.hasNextLine()){
            FileStructure fileStructure = new FileStructure("根目录\\Askia","2.txt","1.txt");
            Message msg2 = new Message(broadcastClient.client.getHostMessage());
            msg2.writeObject(fileStructure);
            broadcastClient.client.sendMessage(msg2);
        }

        */
        while (true) {

        }


    }
}

class event implements AlphaClientEvent {


    @Override
    public void initializationAccept(AlphaInputStream acceptor) {

    }

    @Override
    public void initializationSend(AlphaOutputStream sender, byte[] data) {

    }

    @Override
    public void initializationBroadcastAccept(AlphaMessageInputStream acceptor) {
        Message msg = acceptor.readMessage();
        Object obj = msg.readObject();
        if (obj instanceof DirStructure) {
            DirStructure dirStructure = (DirStructure) obj;
            dirStructure.list();
        }
    }

    @Override
    public void initializationBroadcastSend(AlphaMessageOutputStream sender, Message msg) {

    }

    @Override
    public void doAccept(AlphaInputStream acceptor) {

    }

    @Override
    public void doBroadcastAccept(AlphaMessageInputStream acceptor) {
        System.out.println("Accept");
        Message msg = acceptor.readMessage();
        if (msg != null) {
            Object obj = msg.readObject();
            if (obj instanceof DirStructure) {
                DirStructure dirStructure = (DirStructure) obj;
                dirStructure.list();
            }
            if (obj instanceof FileStructure) {
                FileStructure fileStructure = (FileStructure) obj;
                try {
                    FileStructure.toDiskFile("D:", fileStructure);
                } catch (IOException e) {
                    System.out.println("创建文件失败!");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void connectionFailed(Exception cause) {

    }

    @Override
    public void error(Exception cause) {

    }
}

