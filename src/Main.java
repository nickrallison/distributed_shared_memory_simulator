import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        int n = 10; // Number of processors
        int mode = 2; // 0 = no token ring, 1 = single token ring, 2 = multiple token rings

        int numberOfTokens; // 0 = no tokens, 1 = single token, n-1 = multiple tokens
        if (mode == 0) {
            numberOfTokens = 0;
        } else if (mode == 1) {
            numberOfTokens = 1;
        } else {
            numberOfTokens = n - 1;
        }

        boolean tokenRingActive = numberOfTokens > 0;

        BroadcastSystem broadcastSystem = new BroadcastSystem();

        Map<String, TokenRing> tokenRings = new HashMap<>();
        int[] agentIds = new int[n];

        for (int i = 0; i < n; i++) {
            agentIds[i] = i;
        }

        if (tokenRingActive) {
            if (numberOfTokens == 1) {
                // Single token ring
                TokenRing tokenRing = new TokenRing(agentIds, "singleToken");
                tokenRings.put("singleToken", tokenRing);
            } else {
                // Multiple token rings
                for (int k = 1; k < n; k++) {
                    String tokenId = "victim[" + k + "]";
                    TokenRing tokenRing = new TokenRing(agentIds, tokenId);
                    tokenRings.put(tokenId, tokenRing);
                }
            }
        }

        // Create processors
        for (int i = 0; i < n; i++) {
            LocalMemory localMemory = new LocalMemory();
            BroadcastAgent broadcastAgent = new BroadcastAgent(broadcastSystem);

            TokenRingAgent tokenRingAgent = new TokenRingAgent(i);
            if (tokenRingActive) {
                if (numberOfTokens == 1) {
                    TokenRing tokenRing = tokenRings.get("singleToken");
                    tokenRing.addAgent(tokenRingAgent);
                    tokenRingAgent.addTokenRing(tokenRing);
                } else {
                    for (String tokenId : tokenRings.keySet()) {
                        TokenRing tokenRing = tokenRings.get(tokenId);
                        tokenRing.addAgent(tokenRingAgent);
                        tokenRingAgent.addTokenRing(tokenRing);
                    }
                }
            }

            DSM dsm = new DSM(localMemory, broadcastAgent, tokenRingAgent, numberOfTokens);
            broadcastAgent.setDSM(dsm);

            Processor processor = new Processor(i, dsm, n);
            new Thread(processor).start();
        }

        // Start the token rings
        if (tokenRingActive) {
            if (numberOfTokens == 1) {
                TokenRing tokenRing = tokenRings.get("singleToken");
                tokenRing.startRing(0);
            } else {
                for (TokenRing tokenRing : tokenRings.values()) {
                    tokenRing.startRing(0);
                }
            }
        }
    }
}