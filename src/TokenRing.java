import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenRing {
    private Map<Integer, TokenRingAgent> agents;
    private int[] agentIds;
    private AtomicInteger currentAgentIndex;

    public TokenRing(int[] agentIds) {
        this.agents = new HashMap<>();
        this.agentIds = agentIds;
        this.currentAgentIndex = new AtomicInteger(0);
    }

    public void addAgent(TokenRingAgent agent) {
        agents.put(agent.id, agent);
    }

    public TokenRingAgent getAgent(int agentId) {
        return agents.get(agentId);
    }

    public void startRing(int initialAgentId) {
        Token initialToken = new Token(0);
        TokenRingAgent initialAgent = agents.get(initialAgentId);
        if (initialAgent != null) {
            initialAgent.receiveToken(initialToken);
        }
    }

    public synchronized void passTokenToNext(int currentAgentId, Token token) {
        int nextIndex = (currentAgentIndex.incrementAndGet()) % agentIds.length;
        int nextAgentId = agentIds[nextIndex];
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