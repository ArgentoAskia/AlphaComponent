package Alpha.Server.BroadcastServer;

import Alpha.IOStream.AlphaInputStream;
import Alpha.IOStream.AlphaOutputStream;
import Alpha.Server.AlphaEvent;
import Alpha.Server.HostMessage;

public abstract class BroadcasetResponding implements AlphaEvent {

    // todo readWrite connectionXXX() event
    @Override
    public boolean run(AlphaInputStream acceptor, AlphaOutputStream sender) {
        return false;
    }

    @Override
    public void initialization(HostMessage newClient, AlphaInputStream acceptor, AlphaOutputStream sender) {

    }
}
