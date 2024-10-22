public class DSM {
    private LocalMemory localMemory;
    private BroadcastAgent broadcastAgent;

    public DSM(LocalMemory localMemory, BroadcastAgent broadcastAgent) {
        this.localMemory = localMemory;
        this.broadcastAgent = broadcastAgent;
    }

    public int load(String var) {
        return localMemory.load(var);
    }

    public void store(String var, int val) {
        localMemory.store(var, val);
        broadcastAgent.broadcast(new Message(var, val));
    }

    public void applyRemoteStore(String var, int val) {
        localMemory.store(var, val);
    }
}