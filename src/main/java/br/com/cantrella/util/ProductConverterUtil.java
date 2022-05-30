package br.com.cantrella.util;

import br.com.cantrella.domain.Product;
import br.com.cantrella.dto.ProductInputDTO;
import br.com.cantrella.dto.ProductOutputDTO;

public class ProductConverterUtil {

  public static ProductOutputDTO productToProductOutputDTO(Product product) {
    return new ProductOutputDTO(
        product.getId(),
        product.getName(),
        product.getPrice(),
        product.getQuantityInStock()
    );
  }

  public static Product productInputDTOToProduct(ProductInputDTO product) {
    return new Product(
        null,
        product.getName(),
        product.getPrice(),
        product.getQuantityInStock()
    );
  }

}
