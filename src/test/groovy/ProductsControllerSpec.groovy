import com.myretail.products.model.CurrentPrice
import com.myretail.products.model.Payload
import com.myretail.products.model.Product
import com.myretail.products.model.ProductResponse
import com.myretail.products.service.ProductsServiceImpl
import com.myretail.products.web.controller.ProductsController
import groovy.json.JsonSlurper
import org.springframework.data.rest.webmvc.ResourceNotFoundException
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.bind.MethodArgumentNotValidException
import spock.lang.Shared
import spock.lang.Specification

class ProductsControllerSpec extends Specification {

    @Shared
    ProductsController productsController

    @Shared
    ProductsServiceImpl productsServiceImplMock

    @Shared
    MockMvc mockMvc


    def setup() {
        productsServiceImplMock = Mock(ProductsServiceImpl)
        productsController = new ProductsController()
        productsController.setProductsServiceImpl(productsServiceImplMock)
        mockMvc = MockMvcBuilders.standaloneSetup(productsController).build()
    }

    def 'test get product details'() {
        setup:
        ProductResponse expectedResponse = new ProductResponse()
        expectedResponse.setProductId("13860428")
        expectedResponse.setProductName("The Big Lebowski (Blu-ray)")
        expectedResponse.setCurrentPrice(CurrentPrice.builder().value("15.49").currencyCode("USD").build())

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/myretail/product/13860428").contentType("application/json")).andReturn().response
        def content = new JsonSlurper().parseText(response.contentAsString)

        then:
        1 * productsServiceImplMock.getProductDetails(_ as Payload) >> expectedResponse
        content?.id == expectedResponse.getProductId()
        content?.name == expectedResponse.getProductName()
        content?.current_price?.currency_code == expectedResponse.getCurrentPrice().getCurrencyCode()
        content?.current_price?.value == expectedResponse.getCurrentPrice().getValue()
    }

    def 'test get product details not found'() {
        setup:
        productsServiceImplMock.getProductDetails(new Payload("12345")) >> { throw new ResourceNotFoundException() }

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.get("/myretail/product/12345").contentType("application/json")).andReturn().response

        then:
        response.status == 404
    }

    def 'test get product price details'() {
        setup:
        Product expectedProduct = new Product("13860428", "15.49", "USD")

        when:
        def product = mockMvc.perform(MockMvcRequestBuilders.get("/myretail/product/price/13861428").contentType("application/json")).andReturn().response
        def productContent = new JsonSlurper().parseText(product.contentAsString)

        then:
        1 * productsServiceImplMock.queryProductPriceByID(_ as String) >> expectedProduct
        productContent?.product_id == expectedProduct?.get_id()
        productContent?.value == expectedProduct?.getPrice()
        productContent?.currency_code == expectedProduct?.getCurrencyCode()
    }

    def 'test save product price details'() {
        setup:
        Product expectedProduct = new Product("13860428", "15.49", "USD")
        String requestBody = "{\"product_id\":\"13860428\",\"value\":\"15.49\",\"currency_code\":\"USD\"}"

        when:
        def product = mockMvc.perform(MockMvcRequestBuilders.post("/myretail/product/price").content(requestBody).contentType("application/json")).andReturn().response
        def productContent = new JsonSlurper().parseText(product.contentAsString)

        then:
        1 * productsServiceImplMock.insertProductPrice(_) >> expectedProduct
        productContent?.product_id == expectedProduct?.get_id()
        productContent?.value == expectedProduct?.getPrice()
        productContent?.currency_code == expectedProduct?.getCurrencyCode()
    }

    def 'test save product price details with invalid request'() {
        setup:
        productsServiceImplMock.insertProductPrice(new Product()) >> { throw new MethodArgumentNotValidException() }

        when:
        def response = mockMvc.perform(MockMvcRequestBuilders.post("/myretail/product/price").content("{}").contentType("application/json")).andReturn().response

        then:
        response.status == 400

    }

    def 'update product price details'() {
        setup:
        Product expectedProduct = new Product("13860428", "15.49", "INR")
        String requestBody = "{\"product_id\":\"13860428\",\"value\":\"15.49\",\"currency_code\":\"INR\"}"

        when:
        def product = mockMvc.perform(MockMvcRequestBuilders.put("/myretail/product/13860428").content(requestBody).contentType("application/json")).andReturn().response
        def productContent = new JsonSlurper().parseText(product.contentAsString)

        then:
        1 * productsServiceImplMock.updateProductPrice(_) >> expectedProduct
        productContent?.product_id == expectedProduct?.get_id()
        productContent?.value == expectedProduct?.getPrice()
        productContent?.currency_code == expectedProduct?.getCurrencyCode()
    }

}