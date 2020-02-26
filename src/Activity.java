import java.util.*;

public class Activity {
   public int request;
   public int release;
   public int resource;
   public int claim;
   public int delay;
   String id;
   public Task task;
    Activity(int resource, int delay, String id, Task task){
        this.resource = resource;
        this.delay = delay;
        this.task = task;
        this.id = id;
    }
}
