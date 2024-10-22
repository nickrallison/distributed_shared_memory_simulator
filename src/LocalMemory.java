import java.util.concurrent.ConcurrentHashMap;

public class LocalMemory {
    private ConcurrentHashMap<String, Integer> memory;

    public LocalMemory() {
        memory = new ConcurrentHashMap<>();
    }

    public int load(String var) {
        return memory.getOrDefault(var, 0);
    }

    public void store(String var, int val) {
        memory.put(var, val);
    }
}