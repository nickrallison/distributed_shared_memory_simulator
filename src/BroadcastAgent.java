import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BroadcastAgent implements Runnable {
    private BroadcastSystem broadcastSystem;
    private BlockingQueue<Message> messageQueue;
    private DSM dsm;

    public BroadcastAgent(BroadcastSystem broadcastSystem) {
        this.broadcastSystem = broadcastSystem;
        this.messageQueue = new LinkedBlockingQueue<>();
        broadcastSystem.registerAgent(this);
        new Thread(this).start();
    }

    public void setDSM(DSM dsm) {
        this.dsm = dsm;
    }

    public void broadcast(Message message, double messageDelay) {
        try {
            // Simulate delay
            Thread.sleep((long) (Math.random() * messageDelay));
            broadcastSystem.broadcast(message, this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void receive(Message message) {
        messageQueue.add(message);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Message message = messageQueue.take();
                dsm.applyRemoteStore(message.getVariable(), message.getValue());
                // Simulate delay
//                Thread.sleep((long) (Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
