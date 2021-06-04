package Alpha.RespInstance.ServerTime;

import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaOutputStream;
import Alpha.Server.AlphaEvent;
import Alpha.Server.HostMessage;
import Alpha.Server.Responding;

import java.time.LocalTime;

public class ServerTimeResp extends Responding {
    @Override
    public boolean run(AlphaInputStream acceptor, AlphaOutputStream sender) {
        LocalTime time = LocalTime.now();
        String s = time.getHour() + " : " + time.getMinute() + " : " + time.getSecond() + "\n";
        if(sender.write(s.getBytes())){
            AlphaEvent.delay(1000);
            return true;
        }else{
            return false;
        }
    }


    @Override
    public void connectionIn(HostMessage newClient, HostMessage[] allClients) {
        System.out.println("新连接进来了:" + newClient.toString());
        System.out.println("当前连接总数:" + allClients.length);
    }

    @Override
    public void connectionLost(HostMessage hostMessage, HostMessage[] allClients, Exception cause) {
        System.out.println("连接退出了:" + hostMessage.toString());
        System.out.println("退出原因:" + cause.getMessage());
        System.out.println("当前连接数:" + allClients.length);
    }

    @Override
    public void connectionOut(HostMessage outClient) {

    }

    @Override
    public void connectionFailed(Exception cause) {

    }

    @Override
    public void initialization(HostMessage newClient, AlphaInputStream acceptor, AlphaOutputStream sender) {

    }
}
