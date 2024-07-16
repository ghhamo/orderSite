package job.service.mapper;

import job.persistence.entity.Product;
import job.service.dto.productDto.ProductDto;

import java.util.HashSet;
import java.util.Set;

public record GetAllProducts(Iterable<Product> products) {
    public static Set<ProductDto> mapProductSetToProductDtoSet(Iterable<Product> products) {
        Set<ProductDto> productDtoSet = new HashSet<>();
        for (Product product : products) {
            productDtoSet.add(ProductDto.fromProduct(product));
        }
        return productDtoSet;
    }
}
