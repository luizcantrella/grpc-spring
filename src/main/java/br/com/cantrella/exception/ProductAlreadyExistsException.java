package br.com.cantrella.exception;

import io.grpc.Status;

public class ProductAlreadyExistsException extends BaseBusinessException {

  private static final String ERROR_MESSAGE = "Product %s already exists.";
  private final String name;

  public ProductAlreadyExistsException(String name) {
    super(String.format(ERROR_MESSAGE, name));
    this.name = name;
  }

  @Override
  public Status getStatusCode() {
    return Status.ALREADY_EXISTS;
  }

  @Override
  public String getErrorMessage() {
    return String.format(ERROR_MESSAGE, this.name);
  }
}
