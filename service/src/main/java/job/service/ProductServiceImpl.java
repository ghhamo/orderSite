package job.service;

import job.persistence.ExchangeRateRepository;
import job.persistence.ProductRepository;
import job.persistence.RestaurantRepository;
import job.persistence.entity.ExchangeRate;
import job.persistence.entity.Product;
import job.persistence.entity.Restaurant;
import job.service.dto.PaginationDto;
import job.service.dto.productDto.CreateProductDto;
import job.service.dto.productDto.ProductDto;
import job.service.dto.productDto.UpdateProductDto;
import job.service.mapper.GetAllRestaurants;
import job.service.dto.restaurantDto.RestaurantDto;
import job.service.exception.exchangeRateExceptions.ExchangeRateNotFoundException;
import job.service.exception.productExceptions.ProductAlreadyExistsException;
import job.service.exception.productExceptions.ProductNotFoundException;
import job.service.exception.restaurantExceptions.RestaurantNotFoundException;
import job.service.mapper.GetAllProducts;
import job.service.serviceInterface.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {


    private final RestaurantRepository restaurantRepository;

    private final ProductRepository productRepository;

    private final ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, RestaurantRepository restaurantRepository, ExchangeRateRepository exchangeRateRepository) {
        this.productRepository = productRepository;
        this.restaurantRepository = restaurantRepository;
        this.exchangeRateRepository = exchangeRateRepository;
    }

    @Override
    public ProductDto create(CreateProductDto createProductDto) {
        Objects.requireNonNull(createProductDto.name());
        Objects.requireNonNull(createProductDto.brand());
        Objects.requireNonNull(createProductDto.description());
        if (createProductDto.price() < 0) {
            throw new NoSuchElementException();
        }
        Optional<Product> optionalProduct = productRepository.findProductByNameBrandAndDescription(createProductDto.name(), createProductDto.brand(), createProductDto.description());
        if (optionalProduct.isPresent()) {
            throw new ProductAlreadyExistsException(optionalProduct.toString());
        }
        Product product = CreateProductDto.toProduct(createProductDto);
        Product productFromDb = productRepository.save(product);
        return ProductDto.fromProduct(productFromDb);
    }

    @Override
    public ProductDto addRestaurantToProduct(UUID productId, UUID restaurantId) {
        Objects.requireNonNull(restaurantId);
        Objects.requireNonNull(productId);
        Optional<Restaurant> opRestaurant = restaurantRepository.findById(restaurantId);
        if (opRestaurant.isEmpty()) throw new RestaurantNotFoundException(restaurantId.toString());
        Optional<Product> opProduct = productRepository.findById(productId);
        if (opProduct.isEmpty()) throw new ProductNotFoundException(productId.toString());
        Restaurant restaurant = opRestaurant.get();
        Product product = opProduct.get();
        product.getRestaurants().add(restaurant);
        restaurant.getProducts().add(product);
        return ProductDto.fromProduct(productRepository.save(product));
    }

    @Override
    public Iterable<ProductDto> findAllProducts(PaginationDto paginationDto) {
        PageRequest pageRequest = PageRequest.of(paginationDto.pageNumber(), paginationDto.pageSize());
        Page<Product> products = productRepository.findAll(pageRequest);
        return GetAllProducts.mapProductSetToProductDtoSet(products);
    }

    @Override
    public ProductDto findById(UUID id) {
        Objects.requireNonNull(id);
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id.toString()));
        return ProductDto.fromProduct(product);
    }

    @Override
    public ProductDto findProductByNameBrandAndDescription(String name, String brand, String description) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(brand);
        Objects.requireNonNull(description);
        Product product = productRepository.findProductByNameBrandAndDescription(name, brand, description).orElseThrow(() -> new ProductNotFoundException(name + ", " + brand + ", " + description));
        return ProductDto.fromProduct(product);
    }

    @Override
    public Iterable<RestaurantDto> findRestaurantsByProductId(UUID id) {
        Objects.requireNonNull(id);
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new ProductNotFoundException(id.toString());
        }
        return GetAllRestaurants.mapRestaurantSetToRestaurantDtoSet(product.get().getRestaurants());
    }

    @Override
    public double getExchangeRateOfPriceOfProduct(UUID productId, String currencyCode) {
        Objects.requireNonNull(productId);
        Objects.requireNonNull(currencyCode);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId.toString()));
        ExchangeRate exchangeRate = exchangeRateRepository.findByCurrencyCode(currencyCode).orElseThrow(() -> new ExchangeRateNotFoundException(currencyCode));
        return product.getPrice() * exchangeRate.getRate();
    }

    @Override
    public double getPriceOfProduct(UUID productId) {
        Objects.requireNonNull(productId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId.toString()));
        return product.getPrice();
    }

    @Override
    public ProductDto updateProduct(UpdateProductDto updateProductDto) {
        Objects.requireNonNull(updateProductDto.id());
        Objects.requireNonNull(updateProductDto.description());
        Product product = productRepository.findById(updateProductDto.id()).orElseThrow(() -> new ProductNotFoundException(updateProductDto.id().toString()));
        product.setDescription(updateProductDto.description());
        return ProductDto.fromProduct(productRepository.save(product));
    }

    @Override
    public boolean deleteById(UUID id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
