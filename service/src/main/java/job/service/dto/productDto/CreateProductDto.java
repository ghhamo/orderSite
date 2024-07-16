package job.service.dto.productDto;

import job.persistence.entity.Product;

public record CreateProductDto(String name, String brand, String description, double price) {

    public static Product toProduct(CreateProductDto createProductDto) {
        Product product = new Product();
        product.setName(createProductDto.name);
        product.setBrand(createProductDto.brand);
        product.setDescription(createProductDto.description);
        product.setPrice(createProductDto.price);
        return product;
    }
}
