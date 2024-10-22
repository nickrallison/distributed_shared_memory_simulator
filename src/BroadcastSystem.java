import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BroadcastSystem {
    private List<BroadcastAgent> agents;

    public BroadcastSystem() {
        agents = new CopyOnWriteArrayList<>();
    }

    public void registerAgent(BroadcastAgent agent) {
        agents.add(agent);
    }

    public void broadcast(Message message, BroadcastAgent sourceAgent) {
        for (BroadcastAgent agent : agents) {
            if (agent != sourceAgent) {
                agent.receive(message);
            }
        }
    }
}