package br.com.cantrella.util;

import br.com.cantrella.domain.Product;
import br.com.cantrella.dto.ProductInputDTO;
import br.com.cantrella.dto.ProductOutputDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductConverterUtilTest {

  @Test
  @DisplayName("when convert a Product into a ProductOutputDTO")
  public void productToProductOutputDTOTest() {
    Product product = new Product(1L, "product name", 10.00, 10);
    ProductOutputDTO productOutputDTO = ProductConverterUtil.productToProductOutputDTO(product);
    Assertions.assertThat(product)
        .usingRecursiveComparison()
        .isEqualTo(productOutputDTO);
  }

  @Test
  @DisplayName("when convert a ProductInputDTO into a Product")
  public void productInputDTOToProductTest() {
    ProductInputDTO productInputDTO = new ProductInputDTO("product name", 10.00, 10);
    Product product = ProductConverterUtil.productInputDTOToProduct(productInputDTO);
    Assertions.assertThat(productInputDTO)
        .usingRecursiveComparison()
        .isEqualTo(product);
  }

}
