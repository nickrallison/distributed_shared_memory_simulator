import java.util.HashMap;
import java.util.Map;

public class TokenRing {
    private Map<Integer, TokenRingAgent> agents;
    private int[] agentIds;
    String tokenId;

    public TokenRing(int[] agentIds, String tokenId) {
        this.agents = new HashMap<>();
        this.agentIds = agentIds;
        this.tokenId = tokenId;
    }

    public void addAgent(TokenRingAgent agent) {
        agents.put(agent.id, agent);
    }

    public void startRing(int initialAgentId) {
        Token token = new Token(tokenId);
        TokenRingAgent initialAgent = agents.get(initialAgentId);
        if (initialAgent != null) {
            initialAgent.receiveToken(token);
        }
    }

    public synchronized void passTokenToNext(int currentAgentId, Token token) {
        int idx = -1;
        for (int i = 0; i < agentIds.length; i++) {
            if (agentIds[i] == currentAgentId) {
                idx = i;
                break;
            }
        }
        if (idx == -1) {
            System.err.println("Current agent id not found in agentIds");
            return;
        }
        int nextIdx = (idx + 1) % agentIds.length;
        int nextAgentId = agentIds[nextIdx];
        TokenRingAgent nextAgent = agents.get(nextAgentId);

        // Simulate delay
        try {
            Thread.sleep((long) (Math.random() * 50));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nextAgent.receiveToken(token);
    }
}