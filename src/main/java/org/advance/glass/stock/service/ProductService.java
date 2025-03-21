package org.advance.glass.stock.service;

import lombok.RequiredArgsConstructor;
import org.advance.glass.stock.constant.ProductStatus;
import org.advance.glass.stock.model.db.Product;
import org.advance.glass.stock.model.request.ProductReqDto;
import org.advance.glass.stock.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Create a new product.
    @Transactional
    public Product createProduct(ProductReqDto productReqDto) {
        Product product = Product.builder()
                .productName(productReqDto.getProductName())
                .productGroup(productReqDto.getProductGroup())
                .productUnit(productReqDto.getProductUnit())
                .productStatus(productReqDto.getProductStatus() != null ? productReqDto.getProductStatus() : ProductStatus.ACTIVE.name())
                .build();
        return productRepository.save(product);
    }

    // Retrieve a product by its productId.
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found for productId: " + productId));
    }

    // Retrieve all products.
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Update an existing product using productCode as the identifier.
    @Transactional
    public Product updateProduct(Long productId, ProductReqDto productReqDto) {
        Product product = getProductById(productId);
        product.setProductName(productReqDto.getProductName());
        product.setProductGroup(productReqDto.getProductGroup());
        product.setProductUnit(productReqDto.getProductUnit());
        product.setProductStatus(productReqDto.getProductStatus());
        return productRepository.save(product);
    }

    // Delete a product by its productId.
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = getProductById(productId);
        product.setProductStatus(ProductStatus.INACTIVE.name());
        productRepository.save(product);
    }

    // Search products by productName (wildcard, case-insensitive)
    public List<Product> searchProductsByName(String productName) {
        return productRepository.findByProductNameContainingIgnoreCase(productName);
    }

    @Transactional
    public List<Product> createMultipleProducts(List<ProductReqDto> productReqDtoList) {
        List<Product> products = productReqDtoList.stream()
                .map(productReqDto -> Product.builder()
                        .productName(productReqDto.getProductName())
                        .productGroup(productReqDto.getProductGroup())
                        .productUnit(productReqDto.getProductUnit())
                        .productStatus(productReqDto.getProductStatus() != null ? productReqDto.getProductStatus() : ProductStatus.ACTIVE.name())
                        .build())
                .toList();
        return productRepository.saveAll(products);
    }

//    public Product getOrCreateProduct(Long productId) {
//        return productRepository.findById(productId)
//                .orElseGet(() -> {
//                    Product newProduct = Product.builder()
//                            .id(productId)
//                            .productName("Default Product")
//                            .productGroup("Uncategorized")
//                            .productUnit("pcs")
//                            .status("ACTIVE")
//                            .build();
//                    return productRepository.save(newProduct);
//                });
//    }
}
