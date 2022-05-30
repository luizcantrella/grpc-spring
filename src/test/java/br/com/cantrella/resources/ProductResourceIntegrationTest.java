package br.com.cantrella.resources;

import br.com.cantrella.*;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@DirtiesContext
public class ProductResourceIntegrationTest {

  @GrpcClient("inProcess")
  private ProductServiceGrpc.ProductServiceBlockingStub serviceBlockingStub;

  @Autowired
  private Flyway flyway;

  @BeforeEach
  public void setUp() {
    flyway.clean();
    flyway.migrate();
  }

  @Test
  @DisplayName("when receive a valid data, a product is created ")
  public void createProductSuccessTest() {
    ProductRequest productRequest = ProductRequest.newBuilder()
        .setName("product name")
        .setPrice(10.00)
        .setQuantityInStock(10).build();
    ProductResponse productResponse = serviceBlockingStub.create(productRequest);

    Assertions.assertThat(productRequest)
        .usingRecursiveComparison()
        .comparingOnlyFields("name","price","quantity_in_stock")
        .isEqualTo(productResponse);
  }

  @Test
  @DisplayName("when receive a product with duplicated name, throws ProductAlreadyExistsException")
  public void createProductAlreadyExistsExceptionTest() {
    ProductRequest productRequest = ProductRequest.newBuilder()
        .setName("Product A")
        .setPrice(10.00)
        .setQuantityInStock(10).build();

    Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
        .isThrownBy(() -> serviceBlockingStub.create(productRequest))
        .withMessage("ALREADY_EXISTS: Product Product A already exists.");
  }

  @Test
  @DisplayName("when receive an Id that exists, return the product found.")
  public void findByIdSuccessTest() {
    RequestById requestById = RequestById.newBuilder()
        .setId(1L).build();
    ProductResponse productResponse = serviceBlockingStub.findById(requestById);

    Assertions.assertThat(requestById)
        .usingRecursiveComparison()
        .comparingOnlyFields("name","price","quantity_in_stock")
        .isEqualTo(productResponse);
  }

  @Test
  @DisplayName("when receive an Id that not exists, throws ProductNotFoundException.")
  public void findByIdProductNotFoundExceptionTest() {
    RequestById requestById = RequestById.newBuilder()
        .setId(100L).build();

    Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
        .isThrownBy(() -> serviceBlockingStub.findById(requestById))
        .withMessage("NOT_FOUND: Product with ID 100 not found.");
  }

  @Test
  @DisplayName("when receive an Id that exists, doesn't throw a exception.")
  public void deleteSuccessTest() {
    RequestById requestById = RequestById.newBuilder()
        .setId(1L).build();

    Assertions.assertThatNoException().isThrownBy(() -> serviceBlockingStub.delete(requestById));
  }

  @Test
  @DisplayName("when receive an Id that not exists, throws ProductNotFoundException.")
  public void deleteProductNotFoundExceptionTest() {
    RequestById requestById = RequestById.newBuilder()
        .setId(100L).build();

    Assertions.assertThatExceptionOfType(StatusRuntimeException.class)
        .isThrownBy(() -> serviceBlockingStub.delete(requestById))
        .withMessage("NOT_FOUND: Product with ID 100 not found.");
  }

  @Test
  @DisplayName("when findAll is called, a product list is returned.")
  public void FindAllSuccessTest() {
    EmptyRequest emptyRequest = EmptyRequest.newBuilder().build();

    ProductResponseList productResponseList = serviceBlockingStub.findAll(emptyRequest);

    Assertions.assertThat(productResponseList).isInstanceOf(ProductResponseList.class);
    Assertions.assertThat(productResponseList.getProductsCount()).isEqualTo(2);
    Assertions.assertThat(productResponseList.getProductsList())
        .extracting("id", "name", "price", "quantityInStock")
        .contains(
            Tuple.tuple(1L, "Product A", 10.99, 10),
            Tuple.tuple(2L, "Product B", 10.99, 10)
        );
  }

}
