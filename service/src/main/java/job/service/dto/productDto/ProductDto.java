package job.service.dto.productDto;


import job.persistence.entity.Product;

import java.util.UUID;

public record ProductDto(UUID id, String name, String brand, String description, double price) {

    public static ProductDto fromProduct(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getBrand(), product.getDescription(), product.getPrice());
    }

    public static Product toProduct(ProductDto productDto) {
        Product product = new Product();
        product.setId(productDto.id);
        product.setName(productDto.name);
        product.setBrand(productDto.brand);
        product.setDescription(productDto.description);
        product.setPrice(productDto.price);
        return product;
    }
}