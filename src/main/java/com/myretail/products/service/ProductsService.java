package com.myretail.products.service;

import com.myretail.products.model.Payload;
import com.myretail.products.model.Product;
import com.myretail.products.model.ProductResponse;

public interface ProductsService {

    ProductResponse getProductDetails(Payload payload);

    Product insertProductPrice(Product product);

    Product queryProductPriceByID(String productId);

    Product updateProductPrice(Product product);
}
