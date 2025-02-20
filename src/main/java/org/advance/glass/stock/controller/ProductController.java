package org.advance.glass.stock.controller;

import lombok.RequiredArgsConstructor;
import org.advance.glass.stock.model.db.Product;
import org.advance.glass.stock.model.request.ProductReqDto;
import org.advance.glass.stock.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductReqDto productReqDto) {
        Product created = productService.createProduct(productReqDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{productCode}")
    public ResponseEntity<Product> getProduct(@PathVariable String productCode) {
        Product product = productService.getProductByProductCode(productCode);
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{productCode}")
    public ResponseEntity<Product> updateProduct(@PathVariable String productCode, @RequestBody ProductReqDto productReqDto) {
        Product updated = productService.updateProduct(productCode, productReqDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{productCode}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productCode) {
        productService.deleteProduct(productCode);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam("productName") String productName) {
        List<Product> products = productService.searchProductsByName(productName);
        return ResponseEntity.ok(products);
    }
}
