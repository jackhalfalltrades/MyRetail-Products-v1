package com.myretail.products.web.controller;


import com.myretail.products.entity.Product;
import com.myretail.products.model.Payload;
import com.myretail.products.model.ProductResponse;
import com.myretail.products.service.ProductsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import rx.Observable;

import javax.validation.Valid;

@RestController
@RequestMapping("/myretail")
@Slf4j
public class ProductsController {

    private ProductsServiceImpl productsServiceImpl;

    @Autowired
    public void setProductsServiceImpl(ProductsServiceImpl productsServiceImpl) {
        this.productsServiceImpl = productsServiceImpl;
    }

    // get combined productDetails details form external API and Mongo DB
    @GetMapping(value = "/product/{id}", produces =
            MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ProductResponse getProductDetails(@Valid Payload payload) {

        LOGGER.debug("Payload:{}", payload.toString());
        Observable<ProductResponse> o = productsServiceImpl.getProductDetails(payload);
        return o.toBlocking().single();
    }

    //  insert product price details into Mongo DB
    @PostMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Product insertProductPrice(@Valid @RequestBody Product product) {

        LOGGER.debug("payload:{}", product.toString());
        return productsServiceImpl.insertProductPrice(product);
    }

    // query product price details form Mongo DB by ID
    @GetMapping(value = "/product/price/{id}", produces =
            MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Product queryProductPriceByID(@Valid Payload payload) {

        LOGGER.debug("Payload:{}", payload.toString());
        return productsServiceImpl.queryProductPriceByID(payload.getId());
    }

    // update product price details in Mongo DB by ID
    @PutMapping(value = "/product/{product_id}", produces =
            MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Product updateProductDetails(@Valid @RequestBody Product product) {

        LOGGER.debug("Product:{}", product.toString());
        return productsServiceImpl.updateProductPrice(product);
    }

}
