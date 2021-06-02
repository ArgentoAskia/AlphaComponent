package test;

import Alpha.Client.AlphaClient;
import Alpha.Client.AlphaClientEvent;
import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaOutputStream;

public class AlphaClientTest {
    private AlphaClient client = new AlphaClient("localhost", 13251, new Event());
    public static void main(String[] args) {
        AlphaClientTest inst = new AlphaClientTest();
        inst.beginReading();
        while(true){
            // dowrite
        }
    }
    public void beginReading(){
        client.accepting(100);
    }

}
class Event implements AlphaClientEvent{

    @Override
    public void initializationAccept(AlphaInputStream acceptor) {

    }

    @Override
    public void initializationSend(AlphaOutputStream sender, byte[] data) {

    }

    @Override
    public void doAccept(AlphaInputStream acceptor) {
        String str = new String(acceptor.readNBytes(acceptor.available()));
        acceptor.readAllBytes();
        System.out.println(str);
    }

    @Override
    public void doSend(AlphaOutputStream sender, byte[] data) {

    }

    @Override
    public void connectionFailed(Exception cause) {

    }

    @Override
    public void error(Exception cause) {

    }
}
