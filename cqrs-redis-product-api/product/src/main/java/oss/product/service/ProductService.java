package oss.product.service;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import lombok.extern.slf4j.Slf4j;
import oss.core.exception.EventApplyException;
import oss.core.infra.InfraConstants;
import oss.product.config.RedisCmd;
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
@Slf4j
@Transactional
public class ProductService {

	@Autowired
	private ProductEventHandler productEventHandler;

	@Autowired
	private ProductEventStoreRepository productEventStoreRepository;
	
	@Autowired
	private OrderReadRepository orderReadRepository;
	
	@Autowired
	private RedisCmd redisCmd;


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
	 * 상품 등록 REDIS큐 저장 
	 *
	 * @param productCreateCommand
	 * @return
	 */
	public int product2Redis(Map<String, MultipartFile> files) {
		CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
		CsvMapper csvMapper = new CsvMapper();
		MappingIterator<Map<?, ?>> mappingIterator = null;
		
		Iterator itr = files.values().iterator();
		MultipartFile file = (MultipartFile) itr.next();
		List ld = null;
		try { 
			mappingIterator = csvMapper.reader(ProductCommand.CreateProduct.class).with(bootstrap).readValues(file.getInputStream()); 
			ld = mappingIterator.readAll();
		} catch (IOException e) {
			log.warn("fail file parse D:{}",file.getOriginalFilename());
			return -1;
		}
		
		/** 예외처리 고려 해야 됨
		 *파일 입력 도중 끈킨다면 ? 1. 재입력 프로세스 정의 2. 실패 라인 로깅 3. 남은 데이터 저장
		 */
		ld.stream().forEach(data -> {
			long n_res = redisCmd.push(InfraConstants.CREATE_SCV_Q_PRODUCT, data);
			if(n_res < 0) log.warn("fail data line = {}", data);
		});

		return 0;
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
