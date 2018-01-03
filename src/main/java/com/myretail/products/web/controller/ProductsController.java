package com.myretail.products.web.controller;


import com.myretail.products.model.Payload;
import com.myretail.products.model.Product;
import com.myretail.products.model.ProductResponse;
import com.myretail.products.service.ProductsServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation("fetch product details")
    @GetMapping(value = "/product/{id}", produces =
            MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    ProductResponse getProductDetails (@Valid Payload payload) {

        LOGGER.debug("Product:{}", payload.toString());
         return productsServiceImpl.getProductDetails(payload);
    }

    @ApiOperation("insert product price")
    @PostMapping(value = "/product/price", consumes = MediaType.APPLICATION_JSON_VALUE, produces =
            MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Product insertProductPrice(@Valid @RequestBody Product product) {

        LOGGER.debug("Product:{}", product.toString());
        return productsServiceImpl.insertProductPrice(product);
    }

    @ApiOperation("query product price")
    @GetMapping(value = "/product/price/{id}", produces =
            MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Product queryProductPriceByID(@Valid Payload payload) {

        LOGGER.debug("Product:{}", payload.toString());
        return productsServiceImpl.queryProductPriceByID(payload.getId());
    }

    @ApiOperation("update product price")
    @PutMapping(value = "/product/{id}", produces =
            MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Product updateProductPrice(@Valid @RequestBody Product product) {

        LOGGER.debug("Product:{}", product.toString());
        return productsServiceImpl.updateProductPrice(product);
    }

}
