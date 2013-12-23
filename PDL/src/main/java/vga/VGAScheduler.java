package vga;

import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author wesley
 */
public class VGAScheduler {

    public static void main(String[] args) {
        System.out.println("STARTED");
        try {
            // Grab the Scheduler instance from the Factory 
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
System.out.println("TEST");
            // and start it off
            scheduler.start();

            
               // define the job and tie it to our VGAß class
    JobDetail job = newJob(VGA.class)
        .withIdentity("job1", "group1")
        .build();

    // Trigger the job to run now, and then repeat every 40 seconds
    Trigger trigger = newTrigger()
        .withIdentity("trigger1", "group1")
        .startNow()
        .withSchedule(simpleSchedule()
                .withIntervalInSeconds(20)
                .repeatForever())            
        .build();

    // Tell quartz to schedule the job using our trigger
    scheduler.scheduleJob(job, trigger);
            
            
            
            //scheduler.shutdown();

        } 
        catch (SchedulerException se) {
            se.printStackTrace();
        }
        System.out.println("ENDED");
    }
}
