package eCommerceService.controller;

import com.bimelody.ecommerceservice.controller.ProductController;
import com.bimelody.ecommerceservice.model.Product;
import com.bimelody.ecommerceservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    ProductController productController;

    @Test
    public void test_getProducts_WhenProductServiceReturnResults_ThenShouldReturnOkResponse() {
        Product mockProduct = mock(Product.class);
        List<Product> productList = List.of(mockProduct);
        when(productService.searchProducts("uniqueStoreName", "productCategory", 1, 10))
                .thenReturn(productList);

        Response response = productController.getProducts("uniqueStoreName", "productCategory", 1, 10);

        assertEquals(Response.Status.OK, response.getStatusInfo().toEnum());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertEquals(productList, response.getEntity());
    }
}
