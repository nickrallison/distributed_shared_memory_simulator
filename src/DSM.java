public class DSM {
    private LocalMemory localMemory;
    private BroadcastAgent broadcastAgent;
    private TokenRingAgent tokenRingAgent;
    private int numberOfTokens;

    public DSM(LocalMemory localMemory, BroadcastAgent broadcastAgent, TokenRingAgent tokenRingAgent, int numberOfTokens) {
        this.localMemory = localMemory;
        this.broadcastAgent = broadcastAgent;
        this.tokenRingAgent = tokenRingAgent;
        this.numberOfTokens = numberOfTokens;
    }

    public int load(String var) {
        return localMemory.load(var);
    }

    public void store(String var, int val) {
        // Determine if this variable needs to be synchronized using a token
        boolean needsToken = false;
        String tokenId = null;

        if (numberOfTokens > 0) {
            if (numberOfTokens == 1) {
                needsToken = true;
                tokenId = "singleToken";
            } else { // Multiple tokens
                if (var.startsWith("victim[")) {
                    needsToken = true;
                    tokenId = var; // Use variable name as tokenId
                }
            }
        }

        if (needsToken) {
            // Acquire the token
            tokenRingAgent.acquireToken(tokenId);
        }

        localMemory.store(var, val);
        broadcastAgent.broadcast(new Message(var, val));

        if (needsToken) {
            // Release the token
            tokenRingAgent.releaseToken(tokenId);
        }
    }

    public void applyRemoteStore(String var, int val) {
        localMemory.store(var, val);
    }
}