
import java.util.*;
public class FIFO {
    public Bank fiforun(Bank bank) {
        ArrayList<Task> blocked = new ArrayList<Task>();
        ArrayList<Task> current_task = new ArrayList<Task>();
        int cycle = 0;

        int left = bank.tasks.length;
        while(left > 0){

            int[] free = new int[bank.resources.length];
            //System.out.println("I was standing on cannel! ");
            // initalize blocked and current list and initalize. iterate through the tasks
            bank.initalize(blocked,current_task);
            //now, go through the activities list and
            for(int i=0; i<current_task.size(); i++){
                Activity this_activity = current_task.get(i).activities.get(current_task.get(i).type);
                if(this_activity.id.equals("initiate") || this_activity.task.aborted){
                    this_activity.task.type++;
                    //System.out.println("increase index");
                    continue;
                }
                switch (this_activity.id){
                    case("request"):{
                        if(this_activity.request > bank.resources[this_activity.resource]){
                            this_activity.task.wait +=1;

                             if(!blocked.contains(this_activity.task)){
                                 blocked.add(current_task.get(i));
                               }
                        }
                        else{
                            if(this_activity.delay != 0){
                                this_activity.delay -=1;
                            }
                            else{

                                if(blocked.contains(this_activity.task)){
                                    blocked.remove(this_activity.task);
                                }

                                bank.resources[this_activity.resource] -= this_activity.request;
                                this_activity.task.own[this_activity.resource] -= this_activity.request;
                                this_activity.task.type++;

                            }
                           //
                        }
                     break;
                    }
                    case("release"):{
                        if(this_activity.delay!=0){
                            this_activity.delay--;
                        }
                        else{
                            this_activity.task.own[this_activity.resource] += this_activity.release;
                            free[this_activity.resource] += this_activity.release;
                            this_activity.task.type++;
                        }
                    break;
                    }
                    case("terminate"):{
                        if(this_activity.delay != 0){
                            this_activity.delay--;

                        }
                        else{
                            for(int k=0; k<this_activity.task.own.length; k++){
                                bank.resources[k] += (this_activity.task.claim[k] - this_activity.task.own[k]);
                                this_activity.task.own[k] = this_activity.task.claim[k];
                            }

                            this_activity.task.totalTime = cycle;
                            left -=1;
                            this_activity.id = "finished";
                        }
                    break;

                    }
                    default:{
                        continue;
                    }
                }
            }
            // after we go through the tasks call free method so we can add tem up.
           bank.free_resources(free);
            // deal with all the blocked now
            if(blocked.size() == left){
                Collections.sort(blocked);

                for(int i=0; i<blocked.size(); i++){
                    Activity this_activity = blocked.get(i).activities.get(blocked.get(i).type);

                    if(this_activity.resource >= 0 && this_activity.request > bank.resources[this_activity.resource]){


                        this_activity.task.aborted = true;
                        left-=1;
                        this_activity.id = "we_done!";

                        for(int j=0; j< this_activity.task.own.length; j++){
                            bank.resources[j] += (this_activity.task.claim[j] - this_activity.task.own[j]);
                            this_activity.task.own[j] = this_activity.task.claim[j];
                        }
                    }
                }
            }

            cycle++;
            current_task.clear();
        }
        return bank;
    }



}
