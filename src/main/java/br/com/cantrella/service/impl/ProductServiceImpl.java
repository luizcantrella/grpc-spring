package br.com.cantrella.service.impl;

import br.com.cantrella.domain.Product;
import br.com.cantrella.dto.ProductInputDTO;
import br.com.cantrella.dto.ProductOutputDTO;
import br.com.cantrella.exception.ProductAlreadyExistsException;
import br.com.cantrella.exception.ProductNotFoundException;
import br.com.cantrella.repository.ProductRepository;
import br.com.cantrella.service.IProductService;
import br.com.cantrella.util.ProductConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {

  @Autowired
  private ProductRepository productRepository;

  @Override
  public ProductOutputDTO create(ProductInputDTO inputDTO) {
    Product product = ProductConverterUtil.productInputDTOToProduct(inputDTO);
    this.checkDuplicity(product.getName());
    Product productCreated = this.productRepository.save(product);
    return ProductConverterUtil.productToProductOutputDTO(productCreated);
  }

  @Override
  public ProductOutputDTO findById(Long id) {
    Product product = this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    return ProductConverterUtil.productToProductOutputDTO(product);
  }

  @Override
  public void delete(Long id) {
    Product product = this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    this.productRepository.delete(product);
  }

  @Override
  public List<ProductOutputDTO> findAll() {
    List<Product> products = productRepository.findAll();
    return products.stream()
        .map(ProductConverterUtil::productToProductOutputDTO)
        .collect(Collectors.toList());
  }

  private void checkDuplicity(String name) {
    this.productRepository.findByNameIgnoreCase(name)
        .ifPresent(e -> {
          throw new ProductAlreadyExistsException(name);
        });
  }
}
