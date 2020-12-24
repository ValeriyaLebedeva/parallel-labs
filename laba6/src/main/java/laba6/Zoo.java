package laba6;

import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.apache.zookeeper.*;

import java.io.IOException;
import java.nio.file.WatchEvent;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;

public class Zoo implements Watcher {
    public ZooKeeper zooKeeper;
    private final ActorRef storageActor;

    public Zoo(ZooKeeper zoo, ActorRef storage) throws IOException, KeeperException, InterruptedException {
        this.zooKeeper = zoo;
        this.storageActor = storage;
//        ArrayList<String> updatedServers = new ArrayList<>();
//        for (String c: zooKeeper.getChildren("/servers", null)) {
//            String port = new String(zooKeeper.getData("/servers/" + c, false, null));
//            updatedServers.add(port);
//        }
//        storageActor.tell(new RefreshServersMsg(updatedServers), ActorRef.noSender());
    }

    public void init(String port) throws KeeperException, InterruptedException {
        zooKeeper.create("/servers/localohost:" + port, port.getBytes(StandardCharsets.UTF_8),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    }


    @Override
    public void process(WatchedEvent watchedEvent) {
        try {
            ArrayList<String> updatedServers = new ArrayList<>();
            for (String c: zooKeeper.getChildren("/servers", null)) {
                String port = new String(zooKeeper.getData("/servers/" + c, false, null));
                updatedServers.add(port);
            }
            storageActor.tell(new RefreshServersMsg(updatedServers), ActorRef.noSender());
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
