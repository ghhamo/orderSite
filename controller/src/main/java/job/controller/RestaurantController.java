package job.controller;


import job.service.dto.PaginationDto;
import job.service.dto.productDto.ProductDto;
import job.service.dto.restaurantDto.CreateRestaurantDto;
import job.service.dto.restaurantDto.RestaurantDto;
import job.service.dto.restaurantDto.UpdateRestaurantDto;
import job.service.serviceInterface.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Value("${page.max.size}")
    private Integer pageMaxSize;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<RestaurantDto> create(@RequestBody CreateRestaurantDto createRestaurantDto) {
        return new ResponseEntity<>(restaurantService.create(createRestaurantDto), HttpStatus.CREATED);
    }

    @PostMapping("/{restaurantId}/products/{productId}")
    public ResponseEntity<RestaurantDto> addProductToRestaurant(@PathVariable UUID restaurantId, @PathVariable UUID productId) {
        return new ResponseEntity<>(restaurantService.addProductToRestaurant(restaurantId, productId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Iterable<RestaurantDto>> getAll(@RequestParam int pageIndex, @RequestParam int pageSize) {
        if (pageMaxSize < pageSize) {
            throw new IllegalStateException();
        }
        return new ResponseEntity<>(restaurantService.findAllRestaurants(new PaginationDto(pageIndex, pageSize)), HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<RestaurantDto> getOne(@PathVariable UUID id) {
        return new ResponseEntity<>(restaurantService.findRestaurantById(id), HttpStatus.FOUND);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<RestaurantDto> getByName(@PathVariable String name) {
        return new ResponseEntity<>(restaurantService.findRestaurantByName(name), HttpStatus.FOUND);
    }

    @GetMapping("/address/{address}")
    public ResponseEntity<RestaurantDto> getByAddress(@PathVariable String address) {
        return new ResponseEntity<>(restaurantService.findRestaurantByAddress(address), HttpStatus.FOUND);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<Iterable<ProductDto>> getRestaurantsByProductId(@PathVariable UUID id) {
        return new ResponseEntity<>(restaurantService.findProductsByRestaurantId(id), HttpStatus.FOUND);
    }

    @PatchMapping
    public ResponseEntity<RestaurantDto> update(@RequestBody UpdateRestaurantDto updateRestaurantDto) {
        return new ResponseEntity<>(restaurantService.update(updateRestaurantDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        if (restaurantService.deleteById(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
