package com.myretail.products.service;

import com.myretail.products.entity.Product;
import com.myretail.products.exception.EntityNotFoundException;
import com.myretail.products.model.*;
import com.myretail.products.repository.ProductsRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import rx.Observable;
import sun.util.resources.cldr.ebu.CurrencyNames_ebu;

import java.util.Optional;

@Service("productsServiceImpl")
public class ProductsServiceImpl implements ProductsService {

    @Value("${rest.endpoint}")
    private String url;


    private ProductsRepository productsRepository;

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setProductsRepository(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public Observable<ProductResponse> getProductDetails(Payload payload) {

        Observable<Product> productObservable = Observable.fromCallable(() ->
                queryProductPriceByID(payload.getId())
        );
        Observable<Root> rootObservable = Observable.fromCallable(() ->
                restTemplate.getForObject(url + payload.getId(), Root.class)
        );

        return Observable.zip(rootObservable, productObservable, (Root root, Product product) -> {
            ProductResponse productResponse = new ProductResponse();
            Optional.of(root)
                    .map(Root::getProductDetails)
                    .map(ProductDetails::getItem)
                    .map(Item::getProductDescription)
                    .ifPresent(productDescription -> {
                        productResponse.setProductName(productDescription.getTitle());
                        productResponse.setProductId(payload.getId());
                    });
            productResponse.setCurrentPrice(CurrentPrice.builder()
                    .value(product.getPrice())
                    .currencyCode(product.getCurrencyCode())
                    .build());
            return productResponse;
        });
    }

    @Override
    public Product updateProductPrice(Product product) {
        Product existingProduct = queryProductPriceByID(product.get_id());
        existingProduct.setPrice(product.getPrice());
        if (StringUtils.isNotEmpty(product.getCurrencyCode()))
            existingProduct.setCurrencyCode(product.getCurrencyCode());
        return insertProductPrice(existingProduct);
    }

    @Override
    public Product insertProductPrice(Product product) {
        return productsRepository.save(product);
    }

    public Product queryProductPriceByID(String productId) {
        Product product = productsRepository.findOne(productId);
        if (product == null)
            throw new EntityNotFoundException("ProductPrice not found for " + productId);
        return product;
    }
}
