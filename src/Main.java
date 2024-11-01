import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int n = 10; // Number of processors
        boolean tokenRingActive = true; // Set to true to activate the token ring
        boolean multipleTokens = true; // Set to true to allow multiple tokens in the ring
        Verbosity verbosity = Verbosity.HIGH; // Set to true to enable verbose output
        double messageDelay = 200;
        double csDelay = 20;

        BroadcastSystem broadcastSystem = new BroadcastSystem();

        TokenRing tokenRing = null;
        int[] agentIds = new int[n];

        if (tokenRingActive) {
            agentIds = new int[n];
            for (int i = 0; i < n; i++) {
                agentIds[i] = i;
            }
            tokenRing = new TokenRing(agentIds);
        }

        // Create processors
        for (int i = 0; i < n; i++) {
            LocalMemory localMemory = new LocalMemory();
            BroadcastAgent broadcastAgent = new BroadcastAgent(broadcastSystem);
            DSM dsm = new DSM(localMemory, broadcastAgent, false, i, messageDelay);
            broadcastAgent.setDSM(dsm);

            TokenRingAgent tokenRingAgent = null;
            if (tokenRingActive) {
                tokenRingAgent = new TokenRingAgent(i, tokenRing, n);
                tokenRing.addAgent(tokenRingAgent);
            }

            Processor processor = new Processor(i, dsm, n, tokenRingAgent, tokenRingActive, multipleTokens, csDelay);
            new Thread(processor).start();
        }

        // If token ring is active, start the ring
        if (tokenRingActive & !multipleTokens) {
            tokenRing.startRing(0, 0); // Start the token ring with the first processor
        }
        if (tokenRingActive & multipleTokens) {
            for (int i = 1; i < n; i++) {
                tokenRing.startRing(0, i); // Start the token ring with each processor
            }
        }
    }
}