import java.util.*;
public class Task implements Comparable<Task> {
    int totalTime;
    int task_num;
    int delay;
    int cycle = 0;
    int type;
    int wait;
    float waitAverage;
    boolean aborted;
    int [] claim;
    int own [];
    ArrayList<Activity> activities;
    Task(int task_num, int resourceNum){
        this.task_num = task_num;
        this.claim = new int [resourceNum];
        this.own = new int [ resourceNum];
        this.activities = new ArrayList<Activity>();
    }
    Task(Task t){
        this.task_num = t.task_num;
        this.delay = t.delay;
        this.cycle = t.cycle;
        this.totalTime = t.totalTime;
        this.type = t.type;
        this.wait = t.wait;
        this.waitAverage = t.waitAverage;
        this.aborted = t.aborted;
        this.claim = Arrays.copyOf(t.claim, t.claim.length);
        this.own = Arrays.copyOf(t.own, t.own.length);
        this.activities = new ArrayList<Activity>(t.activities);
    }
    public int compareTo(Task task){
        return this.task_num - task.task_num;
    }
    public String toString(){
        int properNumber = this.task_num + 1; //fix some zero indexing related weirdness;
        if(this.aborted){
            return "Task " + properNumber + " \taborted";
        }
        return "Task " + properNumber + "\t" + this.totalTime + "\t" + this.wait + "\t" + this.waitAverage + "%";
    }

    public void setAborted(){
        this.aborted = true;
    }

}
