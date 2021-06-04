package test;

import Alpha.Server.BroadcastServer.Message;
import Alpha.Server.HostMessage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class AlphaBroadcastClient {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Socket client = new Socket("localhost", 13251);
        OutputStream cout = client.getOutputStream();
        InputStream cin = client.getInputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(cout);
        ObjectInputStream objectInputStream = new ObjectInputStream(cin);
        // 可以选择使用始终读取
        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Message ret = null;
                try {
                    ret = (Message) objectInputStream.readObject();
                    System.out.println(ret.getHostName() + "(" + ret.getIPAddress() + ")说: " + ret.readString());
                } catch (IOException ex) {
                    // ex.printStackTrace();
                    System.out.println("IOException:" + ex.getClass());
                    System.out.println("Message:" + ex.getMessage());
                    System.out.println("--------------------------------");
                } catch (ClassNotFoundException ex) {
                    // ex.printStackTrace();
                    System.out.println("ClassNotFoundException");
                }
            }
        });
        timer.start();

        while(true){
            Scanner scanner = new Scanner(System.in);
            Message msg = new Message(new HostMessage("客户机:" + UUID.randomUUID().toString().substring(0,4),client));
            if(scanner.hasNextLine()){
                String s = scanner.nextLine();
                if(s.equals("exit")){
                    break;
                }
                msg.writeString(s);
                System.out.println("msg信息:" + msg.readString());
                objectOutputStream.flush();
                objectOutputStream.writeObject(msg);
            }
        }
        timer.stop();
        objectInputStream.close();
        objectOutputStream.close();
        cout.close();
        cin.close();
        client.close();
    }
}
