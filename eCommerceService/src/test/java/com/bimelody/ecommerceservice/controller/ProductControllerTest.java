package com.bimelody.ecommerceservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.bimelody.ecommerceservice.model.Product;
import com.bimelody.ecommerceservice.model.request.SearchProductsRequest;
import com.bimelody.ecommerceservice.service.ProductService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Unit test for {@link ProductController}.
 */
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

  @Mock
  private ProductService productService;

  @InjectMocks
  ProductController productController;

  @Test
  public void testGetProductsWhenProductServiceReturnResultsThenShouldReturnOkResponse() {
    Product mockProduct = mock(Product.class);
    List<Product> productList = List.of(mockProduct);
    SearchProductsRequest searchProductsRequestMock = mock(SearchProductsRequest.class);
    when(productService.searchProducts(searchProductsRequestMock))
        .thenReturn(productList);

    ResponseEntity<List<Product>> response =
        productController.searchProducts(searchProductsRequestMock);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(productList, response.getBody());
  }
}
