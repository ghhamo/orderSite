package job.service.dto.restaurantDto;

import job.persistence.entity.Restaurant;

public record CreateRestaurantDto(String name, String address, int tables) {

    public static Restaurant toRestaurant(CreateRestaurantDto createRestaurantDto) {
        Restaurant restaurant = new Restaurant();
        restaurant.setName(createRestaurantDto.name);
        restaurant.setAddress(createRestaurantDto.address);
        restaurant.setTables(createRestaurantDto.tables);
        return restaurant;
    }
}
