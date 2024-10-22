import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class TokenRingAgent {
    int id;
    private Map<String, TokenRing> tokenRings; // Map of tokenId to TokenRing
    private Map<String, BlockingQueue<Token>> tokenQueues; // Map of tokenId to token queues

    public TokenRingAgent(int id) {
        this.id = id;
        this.tokenRings = new ConcurrentHashMap<>();
        this.tokenQueues = new ConcurrentHashMap<>();
    }

    public void addTokenRing(TokenRing tokenRing) {
        tokenRings.put(tokenRing.tokenId, tokenRing);
    }

    public void receiveToken(Token token) {
        String tokenId = token.getTokenId();
        tokenQueues.computeIfAbsent(tokenId, k -> new LinkedBlockingQueue<>()).add(token);
    }

    public void sendToken(String tokenId) {
        TokenRing tokenRing = tokenRings.get(tokenId);
        if (tokenRing != null) {
            Token token = new Token(tokenId);
            tokenRing.passTokenToNext(this.id, token);
        } else {
            System.err.println("TokenRing not found for tokenId: " + tokenId);
        }
    }

    public void acquireToken(String tokenId) {
        try {
            BlockingQueue<Token> queue = tokenQueues.computeIfAbsent(tokenId, k -> new LinkedBlockingQueue<>());
            queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void releaseToken(String tokenId) {
        sendToken(tokenId);
    }
}