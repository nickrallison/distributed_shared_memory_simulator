public class Processor implements Runnable {
    private DSM dsm;
    private int id;
    private int n; // Number of processors
    private TokenRingAgent tokenRingAgent;
    private boolean tokenRingActive;
    private boolean multipleTokens;

    public Processor(int id, DSM dsm, int n, TokenRingAgent tokenRingAgent, boolean tokenRingActive, boolean multipleTokens) {
        this.id = id;
        this.dsm = dsm;
        this.n = n;
        this.tokenRingAgent = tokenRingAgent;
        this.tokenRingActive = tokenRingActive;
        this.multipleTokens = multipleTokens;
    }

    @Override
    public void run() {
        int run_count = 5;
        if (tokenRingActive) {
            if (multipleTokens) {
                run_single_token(run_count);
            } else {
                run_no_tokens(run_count);
            }
        } else {
            run_no_tokens(run_count);
        }
    }

    public void run_no_tokens(int run_count) {
        for (int count = 0; count < run_count; count++) {
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

            System.out.println("Processor " + id + " is entering the critical section.");

            // Simulate work in critical section
            try {
                Thread.sleep((long) (Math.random() * 10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Processor " + id + " is leaving the critical section.");

            // Exit section
            dsm.store("flag[" + id + "]", 0);

            try {
                Thread.sleep((long) (Math.random() * 20));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run_single_token(int run_count) {
        for (int count = 0; count < run_count; count++) {
            // Entry section for Peterson's algorithm
            tokenRingAgent.acquireToken(0);
            dsm.store("flag[" + id + "]", 1);
            tokenRingAgent.releaseToken(0);
            tokenRingAgent.acquireToken(0);
            dsm.store("turn", id);
            tokenRingAgent.releaseToken(0);

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
            System.out.println("Processor " + id + " is entering the critical section.");

            // Simulate work in critical section
            try {
                Thread.sleep((long) (Math.random() * 10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Processor " + id + " is leaving the critical section.");

            // Exit section
            tokenRingAgent.acquireToken(0);
            dsm.store("flag[" + id + "]", 0);
            tokenRingAgent.releaseToken(0);

            try {
                Thread.sleep((long) (Math.random() * 20));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void run_multiple_tokens() {

    }
}