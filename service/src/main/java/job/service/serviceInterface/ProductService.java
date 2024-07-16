package job.service.serviceInterface;

import job.service.dto.PaginationDto;
import job.service.dto.productDto.CreateProductDto;
import job.service.dto.productDto.ProductDto;
import job.service.dto.productDto.UpdateProductDto;
import job.service.dto.restaurantDto.RestaurantDto;

import java.util.UUID;

public interface ProductService {
    ProductDto create(CreateProductDto createProductDto);
    ProductDto addRestaurantToProduct(UUID productId, UUID restaurantId);
    Iterable<ProductDto> findAllProducts(PaginationDto paginationDto);
    ProductDto findById(UUID id);
    ProductDto findProductByNameBrandAndDescription(String name, String brand, String description);
    Iterable<RestaurantDto> findRestaurantsByProductId(UUID id);
    double getExchangeRateOfPriceOfProduct(UUID productId, String currencyCode);
    double getPriceOfProduct(UUID productId);
    ProductDto updateProduct(UpdateProductDto updateProductDto);
    boolean deleteById(UUID id);
}
