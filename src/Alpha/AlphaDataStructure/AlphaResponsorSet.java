package Alpha.AlphaDataStructure;

import Alpha.Server.HostMessage;
import Alpha.Server.Responsor;
import java.util.HashMap;
import java.util.Iterator;

public class AlphaResponsorSet extends HashMap<HostMessage, Responsor> {
    @Override
    public Responsor put(HostMessage key, Responsor value) {
        return containsKey(key) ? null : super.put(key, value);
    }

    public AlphaHashKeySetIterator keySetIterator() {
        return new AlphaHashKeySetIterator(keySet().iterator());
    }

    public static class AlphaHashKeySetIterator {
        private Iterator<HostMessage> messageIterator;
        private HostMessage currrentInstance;

        public AlphaHashKeySetIterator(Iterator<HostMessage> iterator) {
            messageIterator = iterator;
        }

        public HostMessage first() {
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

        public HostMessage next() {
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

        public HostMessage currrentInstance() {
            return currrentInstance;
        }
    }
}