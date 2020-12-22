package laba6;

import akka.actor.ActorRef;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;

public class Zoo {
    public static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int TIMEOUT = (int)Duration.ofSeconds(5).getSeconds();
    public static ZooKeeper zooKeeper;
    private ActorRef storageActor;
    public Zoo(ActorRef storageActor) throws IOException {
        zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, TIMEOUT, watcher);
        this.storageActor = storageActor;
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
                
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
