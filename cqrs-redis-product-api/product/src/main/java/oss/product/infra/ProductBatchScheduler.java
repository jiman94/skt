package oss.product.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import oss.core.event.EventProjector;
import oss.core.infra.InfraConstants;
import oss.product.config.RedisCmd;
import oss.product.model.Product;
import oss.product.model.command.ProductCommand;

@Component
@EnableScheduling
public class ProductBatchScheduler {
	@Autowired
	private ProductEventStoreRepository productEventStoreRepository;
	
	@Autowired
	private ProductEventHandler productEventHandler;
	
	@Autowired
	private RedisCmd redisCmd;
	
	@Scheduled(fixedDelay = 100)
	//@Scheduled(cron = "* * * * *") //크론 형식 가능 
	//@Scheduled("${stat-use-channel}") //yml 에서 가져오기 
	public void doRedis2CreateProduct() {
		
		ProductCommand.CreateProduct data = (ProductCommand.CreateProduct) redisCmd.pop(InfraConstants.CREATE_SCV_Q_PRODUCT);
		if(data == null) return;
		
		/**
		 *샘플은 이벤트 스토어 테워줌, 대량 DB 입력시에는 목록 조회 후 sql batch session 및 예외처리 구성 필요.
		 */
		final Long productId = productEventStoreRepository.createProductId();
		final Product product = Product.create(productId, data);
		
		productEventHandler.save(product);
	}
}
