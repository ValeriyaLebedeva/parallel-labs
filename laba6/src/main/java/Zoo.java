import java.time.Duration;

public class Zoo {
    public static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    public Zoo() {
        zooKeeper = new Zoo(ZOOKEEPER_ADDRESS, (int));
    }
}
