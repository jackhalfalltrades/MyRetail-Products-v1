import com.myretail.products.entity.Product
import com.myretail.products.exception.EntityNotFoundException
import com.myretail.products.model.CurrentPrice
import com.myretail.products.model.Item
import com.myretail.products.model.Payload
import com.myretail.products.model.ProductDescription
import com.myretail.products.model.ProductDetails
import com.myretail.products.model.ProductResponse
import com.myretail.products.model.Root
import com.myretail.products.repository.ProductsRepository
import com.myretail.products.service.ProductsServiceImpl
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.web.client.RestTemplate
import rx.exceptions.OnErrorNotImplementedException
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
        String url = "http://redsky.target.com/v2/pdp/tcin/"
        ProductResponse expectedResponse = new ProductResponse()
        expectedResponse.setProductId("13860428")
        expectedResponse.setProductName("The Big Lebowski (Blu-ray)")
        expectedResponse.setCurrentPrice(CurrentPrice.builder().value("15.49").currencyCode("USD").build())
        Observable<ProductResponse> expectedObservable = Observable.just(expectedResponse)

        def expectedProduct = new Product("13860428", "15.49", "USD")


        when:
        def response = productsService.getProductDetails(payload)

        then:

        //response == expectedObservable
        _ * restTemplateMock.getForObject(_, _) >> expectedRoot
        _ * productsRepositoryMock.findOne(payload?.getId()) >> expectedProduct
    }

    def 'test get product details entity not found exception'() {
        setup:

        Payload payload = new Payload("13860428")

        productsRepositoryMock.findOne(payload?.getId()) >> { throw new EntityNotFoundException("Product price not found for " + payload?.getId())}

        when:
        def response = productsService.getProductDetails(payload).subscribe()

        then:

        thrown(OnErrorNotImplementedException)

    }

    def 'test get product details resource not found exception'() {
        setup:

        Payload payload = new Payload("13860428")

        restTemplateMock.getForObject(_, _) >> { throw new ResourceNotFoundException()}

        when:
        def response = productsService.getProductDetails(payload).subscribe()

        then:

        thrown(OnErrorNotImplementedException)
    }
}
