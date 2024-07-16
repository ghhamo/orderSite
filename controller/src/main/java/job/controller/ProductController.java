package job.controller;

import job.service.dto.PaginationDto;
import job.service.dto.productDto.*;
import job.service.dto.restaurantDto.*;
import job.service.serviceInterface.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Value("${page.max.size}")
    private Integer pageMaxSize;

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody CreateProductDto createProductDto) {
        return new ResponseEntity<>(productService.create(createProductDto), HttpStatus.CREATED);
    }

    @PostMapping("/{productId}/restaurants/{restaurantId}")
    public ResponseEntity<ProductDto> addRestaurantToProduct(@PathVariable UUID productId, @PathVariable UUID restaurantId) {
        return new ResponseEntity<>(productService.addRestaurantToProduct(productId, restaurantId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<ProductDto>> getAll(@RequestParam int pageIndex, @RequestParam int pageSize) {
        if (pageMaxSize < pageSize) {
            throw new IllegalStateException();
        }
        return new ResponseEntity<>(productService.findAllProducts(new PaginationDto(pageIndex, pageSize)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<ProductDto> getById(@PathVariable UUID id) {
        return new ResponseEntity<>(productService.findById(id), HttpStatus.FOUND);
    }

    @GetMapping("/compose")
    public ResponseEntity<ProductDto> getByCompose(@RequestParam String name, @RequestParam String brand, @RequestParam String description) {
        return new ResponseEntity<>(productService.findProductByNameBrandAndDescription(name, brand, description), HttpStatus.FOUND);
    }

    @GetMapping("/{id}/restaurants")
    public ResponseEntity<Iterable<RestaurantDto>> getRestaurantsByProductId(@PathVariable UUID id) {
        return new ResponseEntity<>(productService.findRestaurantsByProductId(id), HttpStatus.FOUND);
    }

    @GetMapping("/{productId}/exchangeRates/{currencyCode}")
    public ResponseEntity<Double> getExchangeRateOfPriceOfProduct(@PathVariable UUID productId, @PathVariable String currencyCode) {
        return new ResponseEntity<>(productService.getExchangeRateOfPriceOfProduct(productId, currencyCode), HttpStatus.FOUND);
    }

    @GetMapping("/{id}/price")
    public ResponseEntity<Double> getPriceOfProduct(@PathVariable UUID id) {
        return new ResponseEntity<>(productService.getPriceOfProduct(id), HttpStatus.FOUND);
    }

    @PatchMapping
    public ResponseEntity<ProductDto> update(@RequestBody UpdateProductDto updateProductDto) {
        return new ResponseEntity<>(productService.updateProduct(updateProductDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (productService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

