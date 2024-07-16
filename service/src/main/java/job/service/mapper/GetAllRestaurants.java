package job.service.mapper;

import job.persistence.entity.Restaurant;
import job.service.dto.restaurantDto.RestaurantDto;

import java.util.HashSet;
import java.util.Set;

public record GetAllRestaurants(Iterable<Restaurant> restaurants) {
    public static Set<RestaurantDto> mapRestaurantSetToRestaurantDtoSet(Iterable<Restaurant> restaurants) {
        Set<RestaurantDto> restaurantDtoSet = new HashSet<>();
        for (Restaurant restaurant : restaurants) {
            restaurantDtoSet.add(RestaurantDto.fromRestaurant(restaurant));
        }
        return restaurantDtoSet;
    }
}
