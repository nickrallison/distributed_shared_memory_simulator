import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenRingAgent {
    int id;
    private TokenRing tokenRing;
    private ArrayList<BlockingQueue<Token>> tokenQueues;

    public TokenRingAgent(int id, TokenRing tokenRing, int numAgents) {
        this.id = id;
        this.tokenRing = tokenRing;
        this.tokenQueues = new ArrayList<>();
        for (int i = 0; i < numAgents; i++) {
            tokenQueues.add(new LinkedBlockingQueue<>());
        }
    }

    public void receiveToken(Token token) {
        int tokenId = token.getTokenId();
        tokenQueues.get(tokenId).add(token);
    }

    public void sendToken(Token token) {
        tokenRing.passTokenToNext(this.id, token);
    }

    public void acquireToken(int tokenId) {
        try {
            Token token = tokenQueues.get(tokenId).take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void releaseToken(int tokenId) {
        Token token = new Token(tokenId);
        sendToken(token);
    }
}