import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenRingAgent {
    int id;
    private TokenRing tokenRing;
    private BlockingQueue<Token> tokenQueue;

    public TokenRingAgent(int id, TokenRing tokenRing) {
        this.id = id;
        this.tokenRing = tokenRing;
        this.tokenQueue = new LinkedBlockingQueue<>();
    }

    public void receiveToken(Token token) {
        tokenQueue.add(token);
    }

    public void sendToken(Token token) {
        tokenRing.passTokenToNext(this.id, token);
    }

    public void acquireToken() {
        try {
            Token token = tokenQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void releaseToken() {
        Token token = new Token(0);
        sendToken(token);
    }
}