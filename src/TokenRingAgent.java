import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TokenRingAgent {
    int id;
    private TokenRing tokenRing;
    private ArrayList<BlockingQueue<Token>> tokenQueues;
    private int blockedTokenId = -1;
    private Verbosity verbosity = Verbosity.LOW;

    public TokenRingAgent(int id, TokenRing tokenRing, int numAgents) {
        this.id = id;
        this.tokenRing = tokenRing;
        this.tokenQueues = new ArrayList<>();
        for (int i = 0; i < numAgents; i++) {
            tokenQueues.add(new LinkedBlockingQueue<>());
        }
    }

    public void receiveToken(Token token) {
        if (verbosity.isMedium()) System.out.println("Agent " + id + " received token " + token.getTokenId());
        int tokenId = token.getTokenId();
        tokenQueues.get(tokenId).add(token);
    }

    public void sendToken(Token token) {
        if (verbosity.isMedium()) System.out.println("Agent " + id + " sent token " + token.getTokenId());
        tokenRing.passTokenToNext(this.id, token);
    }

    public void acquireToken(int tokenId) {
        if (verbosity.isMedium()) System.out.println("Agent " + id + " is acquiring token " + tokenId);
        try {
            blockedTokenId = tokenId;
            Token token;
            Optional<Token> token_opt = tokenRing.grabFreeToken(tokenId);
            if (token_opt.isPresent()) {
                if (verbosity.isMedium()) System.out.println("Agent " + id + " grabbed free token " + tokenId);
                token = token_opt.get();
            } else {
                if (verbosity.isMedium()) System.out.println("Agent " + id + " is waiting for token " + tokenId);
                token = tokenQueues.get(tokenId).take();
                if (verbosity.isMedium()) System.out.println("Agent " + id + " is no longer waiting for token " + tokenId);
            }
            blockedTokenId = -1;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void releaseToken(int tokenId) {
        if (verbosity.isMedium()) System.out.println("Agent " + id + " is releasing token " + tokenId);
        Token token = new Token(tokenId);
        sendToken(token);
    }

    public int getBlockedToken() {
        return blockedTokenId;
    }
}