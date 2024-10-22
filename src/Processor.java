public class Processor implements Runnable {
    private DSM dsm;
    private int id;
    private int n; // Number of processors

    public Processor(int id, DSM dsm, int n) {
        this.id = id;
        this.dsm = dsm;
        this.n = n;
    }

    @Override
    public void run() {
        int count = 0;
        while (count < 100) {
            // Entry section for Peterson's n-process algorithm
            for (int k = 1; k < n; k++) {
                dsm.store("level[" + id + "]", k);
                dsm.store("victim[" + k + "]", id);

                boolean waiting = true;
                while (waiting) {
                    waiting = false;
                    for (int j = 0; j < n; j++) {
                        if (j != id) {
                            int otherLevel = dsm.load("level[" + j + "]");
                            if (otherLevel >= k && dsm.load("victim[" + k + "]") == id) {
                                waiting = true;
                                break;
                            }
                        }
                    }
                    // Sleep to prevent tight spinning
                    try { Thread.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
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
            dsm.store("level[" + id + "]", 0);

            count++;
            // Random sleep to simulate work
            try {
                Thread.sleep((long) (Math.random() * 20));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}