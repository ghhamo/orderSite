package job.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class ScheduledTask {

    private final ScheduledTaskImpl scheduledTaskImpl;

    @Autowired
    public ScheduledTask(ScheduledTaskImpl scheduledTaskImpl) {
        this.scheduledTaskImpl = scheduledTaskImpl;
    }

    @Scheduled(cron = "0 0 6,18 * * ?")
    public void downloadingExchangeScheduledTask() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        scheduledTaskImpl.getExchangeRates();
        String strDate = sdf.format(now);
        System.err.println("The downloads are done:: " + strDate);
    }
}
