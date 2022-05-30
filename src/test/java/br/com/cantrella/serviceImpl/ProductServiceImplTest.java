package br.com.cantrella.serviceImpl;

import br.com.cantrella.domain.Product;
import br.com.cantrella.dto.ProductInputDTO;
import br.com.cantrella.dto.ProductOutputDTO;
import br.com.cantrella.exception.ProductAlreadyExistsException;
import br.com.cantrella.exception.ProductNotFoundException;
import br.com.cantrella.repository.ProductRepository;
import br.com.cantrella.service.impl.ProductServiceImpl;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

  @Mock
  private ProductRepository productRepository;
  @InjectMocks
  private ProductServiceImpl productService;

  @Test
  @DisplayName("when create is called with valid data, a product is returned")
  public void createProductSuccessTest() {
    Product product = new Product(1L, "product name", 10.00, 10);
    when(productRepository.save(any())).thenReturn(product);
    ProductInputDTO productInputDTO = new ProductInputDTO(
      "product name",
      10.00,
      10
    );
    ProductOutputDTO productOutputDTO = productService.create(productInputDTO);

    Assertions.assertThat(productOutputDTO)
        .usingRecursiveComparison()
        .isEqualTo(product);
  }

  @Test
  @DisplayName("when create a product with duplicated name, throw ProductAlreadyExistsException")
  public void createProductExceptionTest() {
    Product product = new Product(1L, "Product A", 10.00, 10);
    when(productRepository.findByNameIgnoreCase(any())).thenReturn(Optional.of(product));
    ProductInputDTO productInputDTO = new ProductInputDTO(
        "Product A",
        10.00,
        10
    );

    Assertions.assertThatExceptionOfType(ProductAlreadyExistsException.class)
        .isThrownBy(() -> productService.create(productInputDTO));
  }

  @Test
  @DisplayName("when findById is called with valid id, a product is returned")
  public void findByIdProductSuccessTest() {
    Long id = 1L;
    Product product = new Product(1L, "product name", 10.00, 10);
    when(productRepository.findById(any())).thenReturn(Optional.of(product));
    ProductOutputDTO productOutputDTO = productService.findById(id);

    Assertions.assertThat(productOutputDTO)
        .usingRecursiveComparison()
        .isEqualTo(product);
  }

  @Test
  @DisplayName("when findById is called with an invalida Id, throw ProductNotFoundException")
  public void findByIdExceptionTest() {
    Long id = 1L;
    when(productRepository.findById(any())).thenReturn(Optional.empty());

    Assertions.assertThatExceptionOfType(ProductNotFoundException.class)
        .isThrownBy(() -> productService.findById(id));
  }

  @Test
  @DisplayName("when delete is called with a valid id, the product found is deleted.")
  public void deleteProductSuccessTest() {
    Long id = 1L;
    Product product = new Product(1L, "product name", 10.00, 10);
    when(productRepository.findById(any())).thenReturn(Optional.of(product));

    Assertions.assertThatNoException().isThrownBy(() -> productService.delete(id));
  }

  @Test
  @DisplayName("when delete is called with an invalida Id, throw ProductNotFoundException")
  public void deleteExceptionTest() {
    Long id = 3L;
    when(productRepository.findById(any())).thenReturn(Optional.empty());

    Assertions.assertThatExceptionOfType(ProductNotFoundException.class)
        .isThrownBy(() -> productService.delete(id));
  }

  @Test
  @DisplayName("when findAll is called, a list of product is returned")
  public void findAllProductSuccessTest() {
    List<Product> products = List.of(
        new Product(1L, "product name", 10.00, 10),
        new Product(2L, "product name second", 12.00, 12));
    when(productRepository.findAll()).thenReturn(products);
    List<ProductOutputDTO> productOutputDTOS = productService.findAll();

    Assertions.assertThat(productOutputDTOS)
        .extracting("id", "name","price","quantityInStock")
        .contains(
            Tuple.tuple(1L, "product name", 10.00, 10),
            Tuple.tuple(2L, "product name second", 12.00, 12)
        );
  }

}
