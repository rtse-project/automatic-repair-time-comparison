

import org.junit.Test;

public class AgentTest {

    @Test
    public void RunTheAgent() {
        WorkerCoordinator workerCoordinator = new WorkerCoordinator();
        workerCoordinator.poll((long) (Math.random()*1000L));
    }


}