package Alpha.RespInstance;

import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaMessageInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.IOStream.AlphaOutputStream;
import Alpha.Server.ClientMessage;
import Alpha.Server.Responding;

public class FileTransmissionResp extends Responding {
    @Override
    public boolean run(AlphaInputStream acceptor, AlphaOutputStream sender) {
        return false;
    }


    @Override
    public void connectionIn(ClientMessage newClient, ClientMessage[] allClients) {

    }

    @Override
    public void connectionLost(ClientMessage clientMessage, ClientMessage[] allClients, Exception cause) {

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

    @Override
    public void initialization(ClientMessage newClient, AlphaMessageInputStream acceptor, AlphaMessageOutputStream rebacker, AlphaMessageOutputStream[] otherSenders) {

    }
}
