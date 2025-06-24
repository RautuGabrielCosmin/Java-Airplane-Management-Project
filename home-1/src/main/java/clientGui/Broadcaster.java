package clientGui;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Holds a thread‐safe list of RpcListeners and fires broadcast messages to them.
 */
public class Broadcaster {
    private static final CopyOnWriteArrayList<RpcListener> listeners = new CopyOnWriteArrayList<>();

    /** Register to receive broadcasts */
    public static void addListener(RpcListener l) {
        listeners.add(l);
    }

    /** Unregister from broadcasts */
    public static void removeListener(RpcListener l) {
        listeners.remove(l);
    }

    /** Fire one broadcast to all registered listeners */
    public static void fire(ObjectNode msg) {
        for (RpcListener l : listeners) {
            l.onBroadcast(msg);
        }
    }
}
