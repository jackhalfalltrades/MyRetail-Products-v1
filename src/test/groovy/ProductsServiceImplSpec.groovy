import com.myretail.products.model.*
import com.myretail.products.repository.ProductsRepository
import com.myretail.products.service.ProductsServiceImpl
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.web.client.RestTemplate
import spock.lang.Shared
import spock.lang.Specification

class ProductsServiceImplSpec extends Specification {

    @Shared
    ProductsServiceImpl productsService

    @Shared
    ProductsRepository productsRepositoryMock

    @Shared
    RestTemplate restTemplateMock

    @Shared
    Payload payload

    def setup() {
        productsRepositoryMock = Mock(ProductsRepository)
        restTemplateMock = Mock(RestTemplate)
        productsService = new ProductsServiceImpl()
        productsService.setProductsRepository(productsRepositoryMock)
        productsService.setRestTemplate(restTemplateMock)
        payload = new Payload("13860428")
    }

    def 'test get product details'() {
        setup:
        def productDescription = new ProductDescription("The Big Lebowski (Blu-ray)")
        def expectedRoot = new Root(new ProductDetails(new Item(productDescription)))
        def expectedResponse = new ProductResponse()
        expectedResponse.setProductId("13860428")
        expectedResponse.setProductName("The Big Lebowski (Blu-ray)")
        expectedResponse.setCurrentPrice(CurrentPrice.builder().value("15.49").currencyCode("USD").build())
        def expectedProduct = new Product("13860428", "15.49", "USD")


        when:
        def response = productsService.getProductDetails(payload)

        then:
        response == expectedResponse
        _ * restTemplateMock.getForObject(_, _) >> expectedRoot
        _ * productsRepositoryMock.findOne(payload?.getId()) >> expectedProduct
    }

    def 'test get product details entity not found exception'() {
        setup:
        productsRepositoryMock.findOne(payload?.getId()) >> { throw new EntityNotFoundException("Product price not found for " + payload?.getId())}

        when:
        def response = productsService.getProductDetails(payload)

        then:
        thrown(EntityNotFoundException)
    }

    def 'test get product details resource not found exception'() {
        setup:
        restTemplateMock.getForObject(_, _) >> { throw new ResourceNotFoundException()}

        when:
        def response = productsService.getProductDetails(payload)

        then:
        thrown(ResourceNotFoundException)

    }
}
