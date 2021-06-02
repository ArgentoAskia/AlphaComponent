package test;

import Alpha.Client.AlphaClient;

public class AlphaClientTest {
    public static void main(String[] args) {
        AlphaClient client = new AlphaClient("localhost", 13251);
        if(client.getCause() == null){
            String time;
            while((time = client.getAcceptor().readLine()) != null){
                System.out.println(time);
            }
        }
    }
}
