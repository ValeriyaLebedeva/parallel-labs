package laba6;

import java.util.ArrayList;

public class RefreshServersMsg {
    private final ArrayList<String> servers;

    public RefreshServersMsg(ArrayList<String> servers) {
        this.servers = servers;
    }

    public ArrayList<String> getServers() {
        return servers;
    }
}
