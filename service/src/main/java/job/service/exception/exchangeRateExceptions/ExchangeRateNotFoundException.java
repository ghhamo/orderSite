package job.service.exception.exchangeRateExceptions;

import job.service.exception.global.EntityNotFound;

public class ExchangeRateNotFoundException extends EntityNotFound {
    public ExchangeRateNotFoundException(String name) {
        super(name);
    }
}
