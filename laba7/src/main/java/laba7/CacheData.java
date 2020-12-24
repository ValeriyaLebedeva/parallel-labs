package laba7;

import org.zeromq.ZContext;

import java.util.HashMap;

public class CacheData {
    private String id;
    private int left;
    private int right;
    private ZContext zContext;
    private HashMap<Integer, String> cache;

}
