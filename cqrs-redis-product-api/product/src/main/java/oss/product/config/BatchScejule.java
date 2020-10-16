package oss.product.config;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class BatchScejule {

	@Scheduled(fixedDelay = 5000)
	//@Scheduled(cron = "* * * * *")
	//@Scheduled("${stat-use-channel}")
	public void doRedis2CreateProduct() {
		
	}

	
}
