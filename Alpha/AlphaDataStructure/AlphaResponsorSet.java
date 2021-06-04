package Alpha.AlphaDataStructure;

import Alpha.Server.HostMessage;
import Alpha.Server.Responsor;
import java.util.HashMap;
import java.util.Iterator;


/**
 * @author Askia
 * @since dev1.6
 */
public class AlphaResponsorSet extends HashMap<HostMessage, Responsor> {

    /**
     * 添加客户端主机信心与绑定的服务者{@link Responsor}对象到服务端服务集合{@link AlphaResponsorSet}
     * <p>
     *     服务集合{@code AlphaResponsorSet}中包含很多组KV对,
     *     每一组KV对的组成是:一个连接到服务器的客户机信息-分配给该客户机的的服务者对象
     *     调用服务集合{@link AlphaResponsorSet#size()}将返回连接到该服务器上的客户机的数量.
     *
     * @see Responsor
     * @since dev-1.6
     *
     * @param key 客户端主机信息
     * @param value 服务器给客户端分配的服务者对象
     * @return 如果添加成功的话,则返回{@code value}参数,
     *         如果服务集合中已包含该客户端信息,则返回{@code null},说明该客户端连接已存在服务集合中.
     */
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