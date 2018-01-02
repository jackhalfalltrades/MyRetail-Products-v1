package com.myretail.products.repository;

import com.myretail.products.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductsRepository extends MongoRepository<Product, String> {
    @Override
    Product findOne(String id);

    @Override
     Product save(Product entity);
}
