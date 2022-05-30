package br.com.cantrella.resources;

import br.com.cantrella.*;
import br.com.cantrella.dto.ProductInputDTO;
import br.com.cantrella.dto.ProductOutputDTO;
import br.com.cantrella.service.IProductService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class ProductResource extends ProductServiceGrpc.ProductServiceImplBase {

  @Autowired
  private IProductService productService;

  @Override
  public void create(ProductRequest request, StreamObserver<ProductResponse> responseObserver) {
    ProductInputDTO productInputDTO = new ProductInputDTO(
        request.getName(),
        request.getPrice(),
        request.getQuantityInStock());

    ProductOutputDTO productOutputDTO = this.productService.create(productInputDTO);
    ProductResponse response = ProductResponse.newBuilder()
        .setId(productOutputDTO.getId())
        .setName(productOutputDTO.getName())
        .setPrice(productOutputDTO.getPrice())
        .setQuantityInStock(productOutputDTO.getQuantityInStock())
        .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void findById(RequestById request, StreamObserver<ProductResponse> responseObserver) {
    Long id = request.getId();
    ProductOutputDTO productOutputDTO = this.productService.findById(id);
    ProductResponse productResponse = ProductResponse.newBuilder()
        .setId(productOutputDTO.getId())
        .setName(productOutputDTO.getName())
        .setPrice(productOutputDTO.getPrice())
        .setQuantityInStock(productOutputDTO.getQuantityInStock())
        .build();

    responseObserver.onNext(productResponse);
    responseObserver.onCompleted();
  }

  @Override
  public void delete(RequestById request, StreamObserver<EmptyResponse> responseObserver) {
    Long id = request.getId();
    productService.delete(id);
    EmptyResponse empty = EmptyResponse.newBuilder().build();

    responseObserver.onNext(empty);
    responseObserver.onCompleted();
  }

  @Override
  public void findAll(EmptyRequest request, StreamObserver<ProductResponseList> responseObserver) {
    List<ProductOutputDTO> productOutputDTOS = productService.findAll();
    List<ProductResponse> productResponses = productOutputDTOS.stream()
        .map(productOutputDTO ->
            ProductResponse.newBuilder()
                .setId(productOutputDTO.getId())
                .setName(productOutputDTO.getName())
                .setPrice(productOutputDTO.getPrice())
                .setQuantityInStock(productOutputDTO.getQuantityInStock())
                .build())
        .collect(Collectors.toList());
    ProductResponseList productResponseList = ProductResponseList.newBuilder()
        .addAllProducts(productResponses)
        .build();

    responseObserver.onNext(productResponseList);
    responseObserver.onCompleted();
  }

}
