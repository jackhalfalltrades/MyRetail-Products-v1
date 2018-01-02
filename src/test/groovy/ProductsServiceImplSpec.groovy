import com.myretail.products.entity.Product
import com.myretail.products.model.CurrentPrice
import com.myretail.products.model.Item
import com.myretail.products.model.Payload
import com.myretail.products.model.ProductDescription
import com.myretail.products.model.ProductDetails
import com.myretail.products.model.ProductResponse
import com.myretail.products.model.Root
import com.myretail.products.repository.ProductsRepository
import com.myretail.products.service.ProductsServiceImpl
import groovy.json.JsonSlurper
import org.springframework.web.client.RestTemplate
import rx.Observable
import spock.lang.Shared
import spock.lang.Specification

class ProductsServiceImplSpec extends Specification {

    @Shared
    ProductsServiceImpl productsService

    @Shared
    ProductsRepository productsRepositoryMock

    @Shared
    RestTemplate restTemplateMock

    def setup() {
        productsRepositoryMock = Mock(ProductsRepository)
        restTemplateMock = Mock(RestTemplate)
        productsService = new ProductsServiceImpl()
        productsService.setProductsRepository(productsRepositoryMock)
        productsService.setRestTemplate(restTemplateMock)
    }

    def 'test get product details'() {
        setup:

        Payload payload = new Payload("13860428")


        ProductDescription productDescription = new ProductDescription("The Big Lebowski (Blu-ray)")
        Root expectedRoot = new Root(new ProductDetails(new Item(productDescription)))


        ProductResponse expectedResponse = new ProductResponse()
        expectedResponse.setProductId("13860428")
        expectedResponse.setProductName("The Big Lebowski (Blu-ray)")
        expectedResponse.setCurrentPrice(CurrentPrice.builder().value("15.49").currencyCode("USD").build())

        Product expectedProduct = new Product("13860428", "15.49", "USD")


        when:
        def response = productsService.getProductDetails(payload)

        then:

        1 * restTemplateMock.getForObject("http://redsky.target.com/v2/pdp/tcin/13860428", Root.class) >> expectedRoot
        1 * productsRepositoryMock.findOne(payload?.getId()) >> expectedProduct



    }


}
