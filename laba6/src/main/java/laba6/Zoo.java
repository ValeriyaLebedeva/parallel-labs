package laba6;

import akka.actor.ActorRef;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;

public class Zoo {
    private static final int TIMEOUT = (int)Duration.ofSeconds(5).getSeconds();
    public static ZooKeeper zooKeeper;
    private static ActorRef storageActor;

    public Zoo(ActorRef storage, String address) throws IOException {
        zooKeeper = new ZooKeeper(address, TIMEOUT, watcher);
        storageActor = storage;
    }

    public void init(String port) throws KeeperException, InterruptedException {
        zooKeeper.create("/servers/" + port, (port+"").getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        WatchedEvent e = new WatchedEvent(Watcher.Event.EventType.NodeCreated,
                Watcher.Event.KeeperState.SyncConnected, "");
        watcher.process(e);
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
