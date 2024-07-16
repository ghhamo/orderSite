package job.service.exception.productExceptions;

import job.service.exception.global.EntityAlreadyExists;

public class ProductAlreadyExistsException extends EntityAlreadyExists {
    public ProductAlreadyExistsException(String result) {
        super(result);
    }
}
