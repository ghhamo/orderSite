package job.service.dto.restaurantDto;

import job.persistence.entity.Restaurant;

import java.util.UUID;

public record RestaurantDto(UUID id, String name, String address, int tables) {
    public static RestaurantDto fromRestaurant(Restaurant restaurant) {
        return new RestaurantDto(restaurant.getId(), restaurant.getName(), restaurant.getAddress(), restaurant.getTables());
    }

    public static Restaurant toRestaurant(RestaurantDto restaurantDto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantDto.id);
        restaurant.setName(restaurantDto.name);
        restaurant.setAddress(restaurantDto.address);
        restaurant.setTables(restaurantDto.tables);
        return restaurant;
    }
}