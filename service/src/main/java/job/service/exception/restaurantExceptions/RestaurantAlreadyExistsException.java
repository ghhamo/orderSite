package job.service.exception.restaurantExceptions;

import job.service.exception.global.EntityAlreadyExists;

public class RestaurantAlreadyExistsException extends EntityAlreadyExists {
    public RestaurantAlreadyExistsException(String name) {
        super(name);
    }
}
