package oss.product.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import oss.core.exception.EventApplyException;
import oss.product.exception.ProductNotFoundException;
import oss.product.infra.ProductEventHandler;
import oss.product.infra.ProductEventStoreRepository;
import oss.product.infra.read.OrderReadRepository;
import oss.product.model.Product;
import oss.product.model.command.ProductCommand;

/**
 * Created by jaceshim on 2017. 3. 3..
 */
@Service
@Transactional
public class ProductService {

	@Autowired
	private ProductEventHandler productEventHandler;

	@Autowired
	private ProductEventStoreRepository productEventStoreRepository;
	
	@Autowired
	private OrderReadRepository orderReadRepository;


	/**
	 * 상품 등록
	 *
	 * @param productCreateCommand
	 * @return
	 */
	public Product createProduct(ProductCommand.CreateProduct productCreateCommand) {
		// DB sequence를 통해서 유일한 productId값 생성
		final Long productId = productEventStoreRepository.createProductId();
		final Product product = Product.create(productId, productCreateCommand);
		// 이벤트 저장
		productEventHandler.save(product);

		return product;
	}

	/**
	 * 상품명 변경
	 *
	 * @param productId
	 * @param productChangeNameCommand
	 * @return
	 * @throws EventApplyException
	 */
	public Product changeName(Long productId, ProductCommand.ChangeName productChangeNameCommand) {
		final Product product = this.getProduct(productId);
		product.changeName(productChangeNameCommand);

		productEventHandler.save(product);

		return product;
	}

	/**
	 * 상품 가격 변경
	 * @param productChangePriceCommand
	 * @return
	 * @throws EventApplyException
	 */
	public Product changePrice(Long productId, ProductCommand.ChangePrice productChangePriceCommand) {
		final Product product = this.getProduct(productId);
		product.changePrice(productChangePriceCommand);

		productEventHandler.save(product);

		return product;
	}

	/**
	 * 상품 조회
	 *
	 * @param productId
	 * @return
	 */
	public Product getProduct(Long productId) {
		final Product product = productEventHandler.find(productId);
		if (product == null) {
			throw new ProductNotFoundException(productId + "is not exists.");
		}
		return product;
	}

	/**
	 * 상품 수량 증가 처리
	 *
	 * @param productId
	 * @param productIncreaseQuantityCommand
	 * @return
	 * @throws EventApplyException
	 */
	public Product increaseQuantity(Long productId, ProductCommand.IncreaseQuantity productIncreaseQuantityCommand) {
		final Product product = this.getProduct(productId);
		product.increaseQuantity(productIncreaseQuantityCommand);

		productEventHandler.save(product);

		return product;
	}

	/**
	 * 상품 수량 감소 처리
	 *
	 * @param productId
	 * @param productDecreaseQuantityCommand
	 * @return
	 * @throws EventApplyException
	 */
	public Product decreaseQuantity(Long productId, ProductCommand.DecreaseQuantity productDecreaseQuantityCommand) {
		final Product product = this.getProduct(productId);
		product.decreaseQuantity(productDecreaseQuantityCommand);

		productEventHandler.save(product);

		return product;
	}
	
	public List getProductList(List orderItems) {
		return orderReadRepository.findProuctInfo(orderItems);
	}
}
