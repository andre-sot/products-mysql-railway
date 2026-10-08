package com.example.products.service;

import com.example.products.model.Product;
import com.example.products.repository.ProductRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService (ProductRepository repo){
        this.repo = repo;
    }
    public List<Product> findAll () {
        return repo.findAll();
    }
    public Optional<Product> findById(Long id){
        return repo.findById(id);
    }
    public Product save(Product product){
        return repo.save(product);
    }
    public Optional<Product> update(Long id,Product product){
        Optional<Product> optional = repo.findById(id);
        Product productBd=optional.get();
        productBd.setName(product.getName());
        productBd.setPrice(product.getPrice());
        productBd.setStock(product.getStock());
        save(productBd);
        return Optional.of(save(productBd));
    }
    public boolean delete(Long id){
        if (!repo.existsById(id)) {
            return false;
        }
        repo.deleteById(id);
        return true;
    }
}
