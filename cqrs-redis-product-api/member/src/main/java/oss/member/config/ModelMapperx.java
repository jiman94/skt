package oss.member.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperx {

	ModelMapper modelMapper;
	
    @Bean
    public ModelMapper getMapper() {
    	ModelMapper mapper = new ModelMapper();
    	init(mapper);
        return mapper;
    }

    private void init(ModelMapper mapper) {
    	mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    	mapper.getConfiguration().setDeepCopyEnabled(true);
	}
    
    @Bean
    public void initModelMapper() {
    	modelMapper = new ModelMapper();
    	init(modelMapper);
    }
    
	public <D> D map(Object source, Class<D> destinationType) {
		try {
			return modelMapper.map(source, destinationType);
		} catch (Exception e) {
//			log.error("err={}", e.getMessage(),e);
			return null;
		}
	}
}
