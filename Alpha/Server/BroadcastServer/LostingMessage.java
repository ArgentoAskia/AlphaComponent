package Alpha.Server.BroadcastServer;

public class LostingMessage {
    private int responsorIndex;
    private Exception cause;

    public LostingMessage(int indexPlace, Exception cause){
        responsorIndex = indexPlace;
        this.cause = cause;
    }

    public Exception getCause() {
        return cause;
    }

    public int getResponsorIndex() {
        return responsorIndex;
    }
}
