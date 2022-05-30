package br.com.cantrella.service;

import br.com.cantrella.dto.ProductInputDTO;
import br.com.cantrella.dto.ProductOutputDTO;

import java.util.List;

public interface IProductService {
  ProductOutputDTO        create (ProductInputDTO inputDTO);
  ProductOutputDTO        findById (Long id);
  void                    delete (Long id);
  List<ProductOutputDTO>  findAll ();
}
