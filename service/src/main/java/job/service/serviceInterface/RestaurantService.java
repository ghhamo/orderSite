package job.service.serviceInterface;
import job.service.dto.PaginationDto;
import job.service.dto.productDto.ProductDto;
import job.service.dto.restaurantDto.CreateRestaurantDto;
import job.service.dto.restaurantDto.RestaurantDto;
import job.service.dto.restaurantDto.UpdateRestaurantDto;

import java.util.UUID;

public interface RestaurantService {
    RestaurantDto create(CreateRestaurantDto createRestaurantDto);
    RestaurantDto addProductToRestaurant(UUID restaurantId, UUID productId);
    RestaurantDto findRestaurantById(UUID id);
    RestaurantDto findRestaurantByName(String name);
    RestaurantDto findRestaurantByAddress(String address);
    Iterable<RestaurantDto> findAllRestaurants(PaginationDto paginationDto);
    Iterable<ProductDto> findProductsByRestaurantId(UUID id);
    RestaurantDto update(UpdateRestaurantDto updateRestaurantDto);
    boolean deleteById(UUID id);
}
