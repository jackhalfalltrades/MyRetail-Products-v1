package com.myretail.products.service;

import com.myretail.products.entity.Product;
import com.myretail.products.model.Payload;
import com.myretail.products.model.ProductResponse;
import rx.Observable;

public interface ProductsService {

    Observable<ProductResponse> getProductDetails(Payload payload);

    Product insertProductPrice(Product product);

    Product queryProductPriceByID(String productId);

    Product updateProductPrice(Product product);
}
