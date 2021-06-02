package Alpha.Server.BroadcastServer;

import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaMessageOutputStream;
import Alpha.IOStream.AlphaOutputStream;
import Alpha.Server.AlphaEvent;
import Alpha.Server.ClientMessage;


public abstract class BroadcasetResponding implements AlphaEvent {
    @Override
    public boolean run(AlphaInputStream acceptor, AlphaOutputStream sender) {
        return false;
    }

    @Override
    public void initialization(ClientMessage newClient, AlphaInputStream acceptor, AlphaOutputStream sender) {

    }
}
