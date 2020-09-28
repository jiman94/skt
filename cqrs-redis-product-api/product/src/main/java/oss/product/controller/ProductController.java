package oss.product.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import oss.order.model.read.OrderItem;
import oss.product.error.ErrorResource;
import oss.product.error.FieldErrorResource;
import oss.product.exception.InvalidRequestException;
import oss.product.exception.ProductNotFoundException;
import oss.product.infra.read.ProductReadRepository;
import oss.product.model.Product;
import oss.product.model.command.ProductCommand;
import oss.product.service.ProductService;

/**
 * Created by jaceshim on 2017. 3. 3..
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductReadRepository productReadRepository;

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
	public ResponseEntity<oss.product.model.read.Product> getProduct(@PathVariable final Long productId) {
		final oss.product.model.read.Product product = productReadRepository.findByProductId(productId);
		if (product == null) {
			throw new ProductNotFoundException(String.format("product id is %d", productId));
		}
		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/products", method = RequestMethod.PUT)
	public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductCommand.CreateProduct command, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Product product = productService.createProduct(command);
		return new ResponseEntity<>(product, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.POST, params = "type=changeName")
	public ResponseEntity<Product> changeName(@PathVariable final Long productId,
		@RequestBody @Valid ProductCommand.ChangeName productChangeNameCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Product product = productService.changeName(productId, productChangeNameCommand);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.POST, params = "type=changePrice")
	public ResponseEntity<Product> changePrice(@PathVariable final Long productId,
		@RequestBody @Valid ProductCommand.ChangePrice productChangePriceCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Product product = productService.changePrice(productId, productChangePriceCommand);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.POST, params = "type=increaseQuantity")
	public ResponseEntity<Product> increaseQuantity(@PathVariable final Long productId,
		@RequestBody @Valid ProductCommand.IncreaseQuantity productIncreaseQuantityCommand, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Product product = productService.increaseQuantity(productId, productIncreaseQuantityCommand);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@RequestMapping(value = "/products/{productId}", method = RequestMethod.POST, params = "type=decreaseQuantity")
	public ResponseEntity<Product> decreaseQuantity(@PathVariable final Long productId,
		@RequestBody @Valid ProductCommand.DecreaseQuantity productDecreaseQuantityCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidRequestException("Invalid Parameter!", bindingResult);
		}

		final Product product = productService.decreaseQuantity(productId, productDecreaseQuantityCommand);

		return new ResponseEntity<>(product, HttpStatus.OK);
	}

	@ExceptionHandler(InvalidRequestException.class)
	public ResponseEntity handleInvalidRequest(InvalidRequestException exception) {
		List<FieldError> fieldErrors = exception.getErrors().getFieldErrors();
		List<FieldErrorResource> fieldErrorResources =
			fieldErrors.stream().map(fieldError -> getFieldErrorResource(fieldError)).collect(Collectors.toList());

		ErrorResource error = new ErrorResource("InvalidRequest", exception.getMessage());
		error.setFieldErrors(fieldErrorResources);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return new ResponseEntity(error, headers, HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value = "/productlist", method = RequestMethod.POST)
	public ResponseEntity<List<OrderItem>> productList(@RequestBody List<OrderItem> orderItems) {
		return new ResponseEntity<>(productService.getProductList(orderItems), HttpStatus.OK);
	}

	private FieldErrorResource getFieldErrorResource(FieldError fieldError) {
		FieldErrorResource fieldErrorResource = new FieldErrorResource();
		fieldErrorResource.setResource(fieldError.getObjectName());
		fieldErrorResource.setField(fieldError.getField());
		fieldErrorResource.setCode(fieldError.getCode());
		fieldErrorResource.setMessage(fieldError.getDefaultMessage());
		return fieldErrorResource;
	}

}
