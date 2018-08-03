package example;

public class DeadlineExample {
    public void pool(int timeout) {
        try {
    //        deadline(timeout) {
                while(true){
                    collect_seeds();
                }
            //}
        } catch(Deadline ex) {
            download_data();
        }
    }

}