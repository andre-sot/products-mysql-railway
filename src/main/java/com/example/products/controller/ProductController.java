package com.example.products.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import com.example.products.dto.DiscountRequest;
import com.example.products.dto.ProductRequest;
import com.example.products.dto.ProductResponse;
import com.example.products.exception.ResourceNotFoundException;
import com.example.products.model.Product;
import com.example.products.service.ProductService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/products")
public class ProductController {


    private final ProductService service;


    public ProductController(ProductService service){
        this.service = service;
    }


    @GetMapping ()
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<ProductResponse> lista = new ArrayList<>();
        for(Product elem: service.findAll()){
            lista.add(toResponse(elem));
        }
        return ResponseEntity.ok(lista) ;
    }
    @GetMapping("/{id}")
    //gdgg
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        Product product =service.findById(id).orElseThrow(
            ()->
            new ResourceAccessException("producto no encontrado" + id)
        );

        return ResponseEntity.ok(toResponse(product));
    }
    
@PostMapping
    public ResponseEntity<ProductResponse> save(@Valid @RequestBody ProductRequest product) {


        Product newProduct =service.save(toEntity(product),product.categoryId());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(newProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,@Valid @RequestBody ProductRequest productUpdated) {
        Product product1 =service.update(
            id,
            toEntity( productUpdated),
            productUpdated.categoryId()
        );
        
        return ResponseEntity.ok(toResponse(product1));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        boolean deleted =service.delete(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

        @PatchMapping("/{id}")
    public ResponseEntity<ProductResponse> updatePrice (@PathVariable Long id,
    @Valid @RequestBody DiscountRequest request ){ 

        
        Product product = service.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("Producto no encontrado con el id: " + id)
        );


        if (product.getStock() == 0) {
            throw new ResourceNotFoundException(
                "No se permite aplicar un descuento si el producto tiene un stock de 0 unidades."
            );
        }
        
        double precioDescontado = product.getPrice() * (1 - (request.getPercentage() / 100.0));
        product.setPrice(precioDescontado);

        Product productoGuardado = service.update(id, product, product.getCategory().getId());

        return ResponseEntity.ok(toResponse(productoGuardado));
    }

    




    private ProductResponse toResponse(Product product){
        return new ProductResponse(product.getId(),product.getName(),product.getPrice(),product.getStock(),0L,"");
    }


    private Product toEntity(ProductRequest product){
        Product productEntity = new Product();
        productEntity.setName(product.name());
        productEntity.setPrice(product.price());
        productEntity.setStock(product.stock());
        return productEntity;
    }
}
