import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenRing {
    private Map<Integer, TokenRingAgent> agents;
    private int[] agentIds;
    private AtomicInteger currentAgentIndex;
    private HashSet<Integer> freeTokens;
    private HashMap<Integer, Integer> tokenMap;
    private Verbosity verbosity = Verbosity.NONE;
    private Object lock = new Object();

    public TokenRing(int[] agentIds) {
        this.agents = new HashMap<>();
        this.agentIds = agentIds;
        this.currentAgentIndex = new AtomicInteger(0);
        this.freeTokens = new HashSet<>();
    }

    public void addAgent(TokenRingAgent agent) {
        agents.put(agent.id, agent);
    }

    public TokenRingAgent getAgent(int agentId) {
        return agents.get(agentId);
    }

    public void startRing(int initialAgentId, int initialTokenId) {
        Token initialToken = new Token(initialTokenId);
        TokenRingAgent initialAgent = agents.get(initialAgentId);
        if (initialAgent != null) {
            initialAgent.receiveToken(initialToken);
        }
    }

    public void passTokenToNext(int currentAgentId, Token token) {
        synchronized (lock) {
            int callingAgentIndex = this.currentAgentIndex.get();
            int nextIndex = (callingAgentIndex + 1) % agentIds.length;

            while (true) {
                int nextAgentId = agentIds[nextIndex];
                TokenRingAgent nextAgent = agents.get(nextAgentId);
                if (nextAgent.getBlockedToken() == token.getTokenId()) {
                    if (verbosity.isLow()) System.out.println("Token " + token.getTokenId() + " is blocked by agent " + nextAgentId);
                    nextAgent.receiveToken(token);
                    return;
                }
                nextIndex = (nextIndex + 1) % agentIds.length;
                if (nextIndex == callingAgentIndex) {
                    if (verbosity.isLow()) System.out.println("Token " + token.getTokenId() + " has completed the ring.");
                    freeTokens.add(token.getTokenId());
                    return;
                }
            }
        }
    }

    public Optional<Token> grabFreeToken(int tokenId) {
        synchronized (lock) {
            if (freeTokens.contains(tokenId)) {
                freeTokens.remove(tokenId);
                return Optional.of(new Token(tokenId));
            }
            return Optional.empty();
        }
    }
}