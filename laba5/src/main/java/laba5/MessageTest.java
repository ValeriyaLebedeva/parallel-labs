package laba5;

public class MessageTest {
    private final float time;
    private final String url;

    public MessageTest(float time, String url) {
        this.time = time;
        this.url = url;
    }

    public float getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }
}
