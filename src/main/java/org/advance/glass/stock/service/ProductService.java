package org.advance.glass.stock.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
                .productCode(productReqDto.getProductCode())
                .productName(productReqDto.getProductName())
                .productGroup(productReqDto.getProductGroup())
                .productUnit(productReqDto.getProductUnit())
                // Default to ACTIVE if not specified.
                .status(productReqDto.getStatus() != null ? productReqDto.getStatus() : "ACTIVE")
                .build();
        return productRepository.save(product);
    }

    // Retrieve a product by its productCode.
    public Product getProductByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode)
                .orElseThrow(() -> new RuntimeException("Product not found for productCode: " + productCode));
    }

    // Retrieve all products.
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Update an existing product using productCode as the identifier.
    @Transactional
    public Product updateProduct(String productCode, ProductReqDto productReqDto) {
        Product product = getProductByProductCode(productCode);
        product.setProductCode(productReqDto.getProductCode());
        product.setProductName(productReqDto.getProductName());
        product.setProductGroup(productReqDto.getProductGroup());
        product.setProductUnit(productReqDto.getProductUnit());
        product.setStatus(productReqDto.getStatus());
        return productRepository.save(product);
    }

    // Delete a product by its productCode.
    @Transactional
    public void deleteProduct(String productCode) {
        Product product = getProductByProductCode(productCode);
        product.setStatus("INACTIVE");
        productRepository.save(product);
    }

    // Search products by productName (wildcard, case-insensitive)
    public List<Product> searchProductsByName(String productName) {
        return productRepository.findByProductNameContainingIgnoreCase(productName);
    }

    public Long getProductIdByProductCode(String productCode) {
        return productRepository.findByProductCode(productCode)
                .map(Product::getId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with code: " + productCode));
    }
}
