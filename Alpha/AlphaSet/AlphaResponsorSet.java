package Alpha.AlphaSet;

import Alpha.Server.ClientMessage;
import Alpha.Server.Responsor;
import java.util.HashMap;
import java.util.Iterator;

public class AlphaResponsorSet extends HashMap<ClientMessage, Responsor> {
    @Override
    public Responsor put(ClientMessage key, Responsor value) {
        return containsKey(key) ? null : super.put(key, value);
    }

    public AlphaHashKeySetIterator keySetIterator() {
        return new AlphaHashKeySetIterator(keySet().iterator());
    }

    public static class AlphaHashKeySetIterator {
        private Iterator<ClientMessage> messageIterator;
        private ClientMessage currrentInstance;

        public AlphaHashKeySetIterator(Iterator<ClientMessage> iterator) {
            messageIterator = iterator;
        }

        public ClientMessage first() {
            if (messageIterator == null) {
                return null;
            }
            if (messageIterator.hasNext() && currrentInstance == null) {
                currrentInstance = messageIterator.next();
                return currrentInstance;
            }
            return null;
        }

        public boolean isDone() {
            if (messageIterator == null) {
                return true;
            }
            return !messageIterator.hasNext() && currrentInstance == null;
        }

        public ClientMessage next() {
            if (messageIterator == null) {
                return null;
            }
            if (currrentInstance == null && messageIterator.hasNext()) {
                return first();
            }
            if (messageIterator.hasNext()) {
                currrentInstance = messageIterator.next();
            } else {
                currrentInstance = null;
            }
            return currrentInstance;

        }

        public ClientMessage currrentInstance() {
            return currrentInstance;
        }
    }
}