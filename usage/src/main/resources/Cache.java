public class Cache {
    int value;
    long lastRefresh;
    static final int MAX_TIME = 10*60*1000;//10minutes
    public Cache(){
        value = readValue();
        lastRefresh = System.currentTimeMillis();
    }
    public int read(){
        long now = System.currentTimeMillis();
        if(now - lastRefresh > MAX_TIME){
            value = readValue();
            lastRefresh = System.currentTimeMillis();
        }
        return value;
    }

    private int readValue() {
        return (int)(Math.random()*100);
    }

    public static void main(String[] args) {
        Cache c = new Cache();
        c.read();
    }
}