package job.service.exception.productExceptions;

import job.service.exception.global.EntityNotFound;

public class ProductNotFoundException extends EntityNotFound {
    public ProductNotFoundException(String id) {
        super(id);
    }
}
