package clientGui;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Listener for incoming broadcasted RPC messages.
 */
public interface RpcListener {
    void onBroadcast(ObjectNode msg);
}
