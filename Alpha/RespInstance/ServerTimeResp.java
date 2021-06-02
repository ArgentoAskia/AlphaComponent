package Alpha.RespInstance;

import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaOutputStream;
import Alpha.Server.AlphaEvent;
import Alpha.Server.ClientMessage;
import Alpha.Server.Responding;

import java.time.LocalTime;

public class ServerTimeResp extends Responding {
    @Override
    public boolean run(AlphaInputStream acceptor, AlphaOutputStream sender) {
        LocalTime time = LocalTime.now();
        String s = time.getHour() +" : "+ time.getMinute() + " : " + time.getSecond() + "\n";
        boolean ret =  sender.writeLine(s);
        if(ret){
            AlphaEvent.delay(1000);
            return true;
        }else{
            // 结束服务器,在此之前可以使用solution
            return false;
        }
    }


    @Override
    public void connectionIn(ClientMessage newClient, ClientMessage[] allClients) {
        System.out.println("新连接进来了:" + newClient.toString());
        System.out.println("当前连接总数:" + allClients.length);
    }

    @Override
    public void connectionLost(ClientMessage clientMessage, ClientMessage[] allClients, Exception cause) {
        System.out.println("连接退出了:" + clientMessage.toString());
        System.out.println("退出原因:" + cause.getMessage());
        System.out.println(allClients.length);
    }

    @Override
    public void connectionOut(ClientMessage outClient) {

    }

    @Override
    public void connectionFailed(Exception cause) {

    }

    @Override
    public void initialization(ClientMessage newClient, AlphaInputStream acceptor, AlphaOutputStream sender) {

    }
}
