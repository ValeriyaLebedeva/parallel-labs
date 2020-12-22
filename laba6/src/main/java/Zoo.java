import java.time.Duration;

public class Zoo {
    public static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int TIMEOUT = (int)Duration.ofSeconds(5).getSeconds();
    public Zoo() {
        zooKeeper = new Zoo(ZOOKEEPER_ADDRESS, TIMEOUT, watcher);
    }
}
