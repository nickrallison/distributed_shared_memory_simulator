public class Processor implements Runnable {
    private DSM dsm;
    private int id;
    private int n; // Number of processors
    private TokenRingAgent tokenRingAgent;
    private boolean tokenRingActive;

    public Processor(int id, DSM dsm, int n, TokenRingAgent tokenRingAgent, boolean tokenRingActive) {
        this.id = id;
        this.dsm = dsm;
        this.n = n;
        this.tokenRingAgent = tokenRingAgent;
        this.tokenRingActive = tokenRingActive;
    }

    @Override
    public void run() {
        for (int count = 0; count < 100; count++) {
            // Entry section for Peterson's algorithm
            dsm.store("flag[" + id + "]", 1);
            dsm.store("turn", id);

            boolean canEnter = false;
            while (true) {
                canEnter = true;
                for (int j = 0; j < n; j++) {
                    if (j != id) {
                        int otherFlag = dsm.load("flag[" + j + "]");
                        int turn = dsm.load("turn");
                        if (otherFlag == 1 && turn == id) {
                            canEnter = false;
                            break;
                        }
                    }
                }
                if (canEnter) {
                    break;
                }
                // Simulate delay
                try {
                    Thread.sleep((long) (Math.random() * 5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Critical section
            if (tokenRingActive) {
                // Acquire the token before entering critical section
                tokenRingAgent.acquireToken();
            }

            System.out.println("Processor " + id + " is entering the critical section.");

            // Simulate work in critical section
            try {
                Thread.sleep((long) (Math.random() * 10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Processor " + id + " is leaving the critical section.");

            if (tokenRingActive) {
                // Release the token after exiting critical section
                tokenRingAgent.releaseToken();
            }

            // Exit section
            dsm.store("flag[" + id + "]", 0);

            try {
                Thread.sleep((long) (Math.random() * 20));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}