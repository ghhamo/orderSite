package job.service.util;

import jakarta.annotation.Resource;
import job.service.ExchangeRateServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class ScheduledTaskImpl {


    private final ExchangeRateServiceImpl exchangeRateServiceImpl;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    public ScheduledTaskImpl(ExchangeRateServiceImpl exchangeRateServiceImpl) {
        this.exchangeRateServiceImpl = exchangeRateServiceImpl;
    }

    @Transactional
    public void getExchangeRates() {
        threadPoolTaskExecutor.submit(exchangeRateServiceImpl::updateRecords);
    }
}
