import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Arrays;

public class BankerAlg {
    public  Bank run (Bank bank) {
      //  System.out.println("in here");
        ArrayList<Task> blocked = new ArrayList<Task>();
        ArrayList<Task> current_task = new ArrayList<Task>();
        int cycle = 0;

        int left = bank.tasks.length;
        //making sure we go through all the tasks
        while(left > 0){

           // System.out.println("unbeareably white");
            int[] released = new int[bank.resources.length];
            //initalize the current list and activities in for loop below.
            bank.initalize(blocked,current_task);

            for (int i = 0; i < current_task.size(); i++) {
                Activity activity = current_task.get(i).activities.get(current_task.get(i).type);
                // conditional checks on what to do based on the ids
                if (activity.id.equals("initiate")) {
                    if (current_task.get(i).claim[activity.resource] > bank.resources[activity.resource]) {
                        current_task.get(i).aborted = true;
                        left--;
                    }
                    activity.task.type++;
                    continue;
                }
                else if (activity.task.aborted) {
                    continue;
                }

                switch (activity.id) {


                    case ("request"): {

                        if (activity.delay == 0) {
                            if (!isSafe(bank, activity)) {
                                activity.task.wait++;

                                if (!blocked.contains(activity.task)) {
                                    blocked.add(current_task.get(i));
                                }
                            }
                            else if (activity.request > activity.task.own[activity.resource]) {
                                activity.task.aborted = true;
                                left--;
                                activity.id = "ended";

                                for (int j = 0; j < activity.task.own.length; j++) {
                                    bank.resources[j] += (activity.task.claim[j] - activity.task.own[j]);
                                    activity.task.own[j] = activity.task.claim[j];
                                }
                            }
                            else {
                                if (blocked.contains(activity.task)) {
                                    blocked.remove(activity.task);
                                }

                                bank.resources[activity.resource] -= activity.request;
                                activity.task.own[activity.resource] -= activity.request;
                                activity.task.type++;
                            }

                        }
                        else {
                            activity.delay--;
                        }
                        break;
                    }

                    case ("release"): {
                        if (activity.delay == 0) {
                            released[activity.resource] += activity.release;
                            activity.task.own[activity.resource] += activity.release;
                            activity.task.type++;
                        } else {
                            activity.delay--;
                        }
                        break;
                    }
                    case ("terminate"): {
                        if (activity.delay == 0) {

                            for (int j = 0; j < activity.task.own.length; j++) {
                                bank.resources[j] += (activity.task.claim[j] - activity.task.own[j]);
                                activity.task.own[j] = activity.task.claim[j];
                            }

                            activity.task.totalTime = cycle;
                            left--;
                            activity.id = "endgame";
                        } else {
                            activity.delay--;
                        }
                    break;
                    }
                    default:{
                        continue;
                    }

                }
            }

            //call the free resources method so we can add them up

            bank.free_resources(released);

            current_task.clear();
            cycle++;
        }
        System.out.println("we out");
        return bank;
    }

    // recursive method and overloaded method used to check whether banker is safe or not to allocate.
    public boolean isSafe(Bank bank, Activity activity) {
        Task[] tasks = new Task[bank.tasks.length];
        // loop though and initalize new tasks.
        for (int i = 0; i < bank.tasks.length; i++) {
            tasks[i] = new Task(bank.tasks[i]);
        }
        ArrayList<Task> listoftasks = new ArrayList<Task>(Arrays.asList(tasks));

        for (int i = 0; i < listoftasks.size(); i++) {
            if (listoftasks.get(i).aborted) {
                listoftasks.remove(i--);
            }
        }

        // make a copy of
        int[] resources = Arrays.copyOf(bank.resources, bank.resources.length);
        // keep decrementing
        resources[activity.resource] -= activity.request;
        // keep decrementing from the tasks list
        tasks[activity.task.task_num].own[activity.resource] -= activity.request;
        // now pass into the actual recurisve method
        return isSafe(listoftasks, resources, activity);
    }

    public boolean isSafe(ArrayList<Task> tasks, int[] resources, Activity activity){

       // if empty we know its safe
        if (tasks.isEmpty()) {
            return true;
        }
        boolean safe;
        // go through and see if the task is greater than resources we have if so then it is not safe
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            safe = true;
            for (int j = 0; j < resources.length; j++) {
                if (task.own[j] > resources[j]) {
                    safe = false;
                }
            }
            //if it is safe, iterate and recall method.
            if (safe) {
                for (int j = 0; j < task.own.length; j++) {
                    resources[j] += (task.claim[j] - task.own[j]);
                }
                tasks.remove(i);
                return isSafe(tasks, resources, activity);
            }
        }
        return false;
    }


}