package job.service.exception.restaurantExceptions;

import job.service.exception.global.EntityNotFound;

public class RestaurantNotFoundException extends EntityNotFound {
    public RestaurantNotFoundException(String name) {
        super(name);
    }
}
