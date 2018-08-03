package example;

class TestNeighbors extends Thread {

    int NUM_PING = 10;

    List neighbors = List();

    boolean is_alive(IpAddress address, long timeout) {
        try{
            //deadline(timeout) {
                this.send_icmp_request(address);
                this.receive_icmp_reply(address);
                return true;
            //}
        } catch (DeadlineException e) {
            return false;
        }
    }

    void run() {

        for (IpAddress curr : this.neighbors) {

            bool isAlive = this.is_alive(100);
            curr.setAlive(is_alive);

            sleep(1000);
        }
    }
}