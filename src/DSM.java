public class DSM {
    private LocalMemory localMemory;
    private BroadcastAgent broadcastAgent;
    public Verbosity verbosity = Verbosity.LOW;
    private int procId;
    private double messageDelay;

    public DSM(LocalMemory localMemory, BroadcastAgent broadcastAgent, boolean verbosity, int procId, double messageDelay) {
        this.localMemory = localMemory;
        this.broadcastAgent = broadcastAgent;
        this.procId = procId;
        this.messageDelay = messageDelay;
    }

    public int load(String var) {
        if (verbosity.isMedium()) System.out.println("Processor " + procId + " is loading variable " + var);
        return localMemory.load(var);
    }

    public void store(String var, int val) {
        if (verbosity.isMedium()) System.out.println("Processor " + procId + " is storing variable " + var);
        localMemory.store(var, val);
        broadcastAgent.broadcast(new Message(var, val), messageDelay);
    }

    public void applyRemoteStore(String var, int val) {
        if (verbosity.isHigh()) System.out.println("Processor " + procId + " is applying remote store to variable " + var);
        localMemory.store(var, val);
    }
}