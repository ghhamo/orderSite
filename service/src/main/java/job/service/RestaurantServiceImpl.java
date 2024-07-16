package job.service;

import job.persistence.ProductRepository;
import job.persistence.RestaurantRepository;
import job.persistence.entity.Product;
import job.persistence.entity.Restaurant;
import job.service.dto.PaginationDto;
import job.service.dto.restaurantDto.*;
import job.service.dto.productDto.*;
import job.service.dto.restaurantDto.RestaurantDto;
import job.service.exception.productExceptions.ProductNotFoundException;
import job.service.exception.restaurantExceptions.RestaurantAlreadyExistsException;
import job.service.exception.restaurantExceptions.RestaurantNotFoundException;
import job.service.mapper.GetAllProducts;
import job.service.mapper.GetAllRestaurants;
import job.service.serviceInterface.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;

    private final ProductRepository productRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, ProductRepository productRepository) {
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
    }

    @Override
    public RestaurantDto create(CreateRestaurantDto createRestaurantDto) {
        Objects.requireNonNull(createRestaurantDto);
        Objects.requireNonNull(createRestaurantDto.name());
        Objects.requireNonNull(createRestaurantDto.address());
        Optional<Restaurant> optionalRestaurant = restaurantRepository.findByName(createRestaurantDto.name());
        if (optionalRestaurant.isPresent()) {
            throw new RestaurantAlreadyExistsException(createRestaurantDto.name());
        }
        optionalRestaurant = restaurantRepository.findByAddress(createRestaurantDto.address());
        if (optionalRestaurant.isPresent()) {
            throw new RestaurantAlreadyExistsException(createRestaurantDto.address());
        }
        Restaurant restaurant = CreateRestaurantDto.toRestaurant(createRestaurantDto);
        Restaurant restaurantFromDb = restaurantRepository.save(restaurant);
        return RestaurantDto.fromRestaurant(restaurantFromDb);
    }
@Override
    public RestaurantDto addProductToRestaurant(UUID restaurantId, UUID productId) {
        Objects.requireNonNull(restaurantId);
        Objects.requireNonNull(productId);
        Optional<Restaurant> opRestaurant = restaurantRepository.findById(restaurantId);
        if (opRestaurant.isEmpty()) throw new RestaurantNotFoundException(restaurantId.toString());
        Optional<Product> opProduct = productRepository.findById(productId);
        if (opProduct.isEmpty()) throw new ProductNotFoundException(productId.toString());
        Restaurant restaurant = opRestaurant.get();
        Product product = opProduct.get();
        restaurant.getProducts().add(product);
        product.getRestaurants().add(restaurant);
        return RestaurantDto.fromRestaurant(restaurantRepository.save(restaurant));
    }
@Override
    public RestaurantDto findRestaurantById(UUID id) {
        Objects.requireNonNull(id);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new RestaurantNotFoundException(id.toString()));
        return RestaurantDto.fromRestaurant(restaurant);
    }
@Override
    public RestaurantDto findRestaurantByName(String name) {
        Objects.requireNonNull(name);
        Restaurant restaurant = restaurantRepository.findByName(name).orElseThrow(() -> new RestaurantNotFoundException(name));
        return RestaurantDto.fromRestaurant(restaurant);
    }
@Override
    public RestaurantDto findRestaurantByAddress(String address) {
        Objects.requireNonNull(address);
        Restaurant restaurant = restaurantRepository.findByAddress(address).orElseThrow(() -> new RestaurantNotFoundException(address));
        return RestaurantDto.fromRestaurant(restaurant);
    }

    @Override
    public Iterable<RestaurantDto> findAllRestaurants(PaginationDto paginationDto) {
        PageRequest pageRequest = PageRequest.of(paginationDto.pageNumber(), paginationDto.pageSize());
        Page<Restaurant> restaurants = restaurantRepository.findAll(pageRequest);
        return GetAllRestaurants.mapRestaurantSetToRestaurantDtoSet(restaurants);
    }
@Override
    public Iterable<ProductDto> findProductsByRestaurantId(UUID id) {
        Objects.requireNonNull(id);
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isEmpty()) {
            throw new RestaurantNotFoundException(id.toString());
        }
        return GetAllProducts.mapProductSetToProductDtoSet(restaurant.get().getProducts());
    }
@Override
    public RestaurantDto update(UpdateRestaurantDto updateRestaurantDto) {
        Objects.requireNonNull(updateRestaurantDto.id());
        Restaurant restaurant = restaurantRepository.findById(updateRestaurantDto.id()).orElseThrow(() -> new RestaurantNotFoundException(updateRestaurantDto.id().toString()));
        restaurant.setTables(updateRestaurantDto.tables());
        return RestaurantDto.fromRestaurant(restaurantRepository.save(restaurant));
    }
@Override
    public boolean deleteById(UUID id) {
        Optional<Restaurant> restaurant = restaurantRepository.findById(id);
        if (restaurant.isPresent()) {
            restaurantRepository.deleteById(restaurant.get().getId());
            return true;
        }
        return false;
    }
}
