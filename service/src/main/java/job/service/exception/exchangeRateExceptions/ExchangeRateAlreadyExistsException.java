package job.service.exception.exchangeRateExceptions;

import job.service.exception.global.EntityAlreadyExists;

public class ExchangeRateAlreadyExistsException extends EntityAlreadyExists {
    public ExchangeRateAlreadyExistsException(String name) {
        super(name);
    }
}
