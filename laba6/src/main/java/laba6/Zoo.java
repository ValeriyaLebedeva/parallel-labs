package laba6;

import akka.actor.ActorRef;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;

public class Zoo {
    public static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int TIMEOUT = (int)Duration.ofSeconds(5).getSeconds();
    public static ZooKeeper zooKeeper;
    private static ActorRef storageActor;

    public Zoo(ActorRef storage) throws IOException {
        zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, TIMEOUT, watcher);
        storageActor = storage;
    }

    public ActorRef getStorageActor() {
        return storageActor;
    }

    public static void init(String port) {
        zooKeeper.create("/servers/" + port, (port+"").getBytes(),
                
                )
    }

    public static Watcher watcher = watchedEvent -> {
        if (watchedEvent.getType() == Watcher.Event.EventType.NodeDataChanged ||
                watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted ||
                watchedEvent.getType() == Watcher.Event.EventType.NodeDeleted) {
            ArrayList<String> updatedServers = new ArrayList<>();
            try {
                for (String c: zooKeeper.getChildren("/servers", null)) {
                    String port = new String(zooKeeper.getData("/servers/" + c, false, null));
                    updatedServers.add(port);
                }
                storageActor.tell(new RefreshServersMsg(updatedServers), ActorRef.noSender());
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
}
