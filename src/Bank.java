import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class Bank {
    int total = 0;
    int taken = 0;
    int left = this.total - this.taken;
    public Task[] tasks;
    public int [] resources;

  //used to generate our "Bank"
    Bank(String filename)throws FileNotFoundException {
        File inputFile = new File(filename);
        Scanner input = new Scanner(inputFile);
        int taskcount = input.nextInt();
        this.tasks = new Task[taskcount];
        int resourcecount = input.nextInt();
        this.resources = new int[resourcecount];
        //two for loops used to initalize the tasks and resources
        for (int i = 0; i < taskcount; i++) {
            tasks[i] = new Task(i, resourcecount);
        }
        for (int i = 0; i < resourcecount; i++) {
            resources[i] = input.nextInt();
        }

        while (input.hasNextLine() && input.hasNext()) {
            String id = input.next();
            int task_num = input.nextInt() - 1; //
            int delay = input.nextInt();
            int resourceType = input.nextInt() - 1;
            // adding all the inidividual actitivities per line
            Activity activity = new Activity(resourceType, delay, id, tasks[task_num]);
            if (activity.id.equals("initiate")) {
                int initialClaim = input.nextInt();
                activity.claim = initialClaim;
                tasks[task_num].own[resourceType] = initialClaim;
                tasks[task_num].claim[resourceType] = initialClaim;
            } else if (activity.id.equals("request")) {
                int numberRequested = input.nextInt();
                activity.request = numberRequested;

            } else if (activity.id.equals("release")) {
                int numberReleased = input.nextInt();
                activity.release = numberReleased;

            } else if (activity.id.equals("terminate")) {
                input.nextInt();
            }
            tasks[task_num].activities.add(activity);

        }

        input.close();
    }





    public void printBank(){
        int total = 0;
        int wait = 0;
        for(int i = 0; i<this.tasks.length; i++) {
            this.tasks[i].waitAverage = ((float) this.tasks[i].wait / this.tasks[i].totalTime * 100);

            if(this.tasks[i].aborted == false){
                total += this.tasks[i].totalTime;
                wait = wait + this.tasks[i].wait;
            }

            System.out.println(this.tasks[i]);
        }
       // System.out.println("made this here with all the ice on in the booth");
        System.out.println("\ntotal: " + total + ", wait: " + wait + ", waitAverage: " + ((double) wait / total) * 100);
    }

    public void free_resources(int[] free){
        for(int i=0; i<free.length; i++){
            this.resources[i] +=free[i];
        }
    }
    public  void initalize( ArrayList<Task> blocked,  ArrayList<Task> current_task){
        for(int i=0; i<blocked.size(); i++){
            current_task.add(blocked.get(i));
        }

        for(int i=0; i<this.tasks.length; i++){
            if(!current_task.contains(this.tasks[i])){
                current_task.add(this.tasks[i]);
            }
        }
    }
}
