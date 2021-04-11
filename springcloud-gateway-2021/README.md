# 복호화 

# 신세계 1대1문의  

# 메가존 클라우드 

# 운영 반영 일정 
- 




# Spring Cloud Gateway Demo
SCG의 데모 프로젝트를 작성하고 설명한다.


buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.2.3.RELEASE")
    }
}

apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'idea'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'


repositories {
    mavenCentral()
}
group = 'com.project.demo'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = 1.11
targetCompatibility = 1.11

dependencyManagement {
    imports {
//        mavenBom "org.springframework.cloud:spring-cloud-dependencies:Greenwich.SR2"
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:Hoxton.RELEASE"
    }
}

dependencies {
    compile("org.springframework.cloud:spring-cloud-starter-gateway")
    compile("org.springframework.cloud:spring-cloud-starter-netflix-hystrix")
    compile("org.springframework.cloud:spring-cloud-starter-contract-stub-runner"){
        exclude group: "org.springframework.boot", module: "spring-boot-starter-web"
    }
    testCompile("org.springframework.boot:spring-boot-starter-test")
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
}


# Spring Cloud Gateway(SCG)를 활용한 API Gateway 구축

1. Spring Cloud Gateway란?

2. Spring Cloud Gateway로 API Gateway 구축하기

 
1. Spring Cloud Gateway(SCG)란?

Spring Cloud Gateway(SCG)란 MSA 환경에서 사용하는 API Gateway중 하나로 Spring5, Spring Boot2, Project Reactor로 구축된 API Gateway다. Spring Cloud Gateway는 API 라우팅 및 보안, 모니터링/메트릭 등의 기능을 간단하고 효과적인 방법으로 제공한다. 

1-1. 왜 API Gateway를 사용할까?

왜 MSA 환경에서는 API Gateway를 사용할까? API Gateway를 사용하는 이유는 아래와 같다.

 - 유입되는 모든 요청/응답이 통하기 때문에 인증/보안을 적용하기 좋다.
 - URI에 따라 서비스 엔드포인트를 다르게 가져가는 동적 라우팅이 가능해진다. 예를 들면 도메인 변경없이 레거시 시스템을 신규 시스템으로 점진적으로 교체해 나가는 작업을 쉽게 진행할 수 있다.
 - 모든 트래픽이 통하기 때문에 모니터링 시스템 구성이 단순해진다.
 - 동적 라우팅이 가능하므로 신규 스팩을 서비스 일부에만 적용하거나 트래픽을 점진적으로 늘려나가는 테스트를 수행하기에 수월해진다.

 
1-2. Spring Cloud Zuul과의 차이점 & 특징

Spring Cloud Zuul과의 차이점은 무엇인가? 인터넷 등에서 API Gateway 레퍼런스를 찾다보면 가장 많이 나오는 레퍼런스는 바로 Spring Cloud Zuul이다. 그런데 왜 Spring Cloud Gateway를 사용하며 그 둘의 차이점은 무엇인가?

- Spring Cloud의 초창기 버전에서는 Netfilx OSS(Open Source Software)에 포함된 컴포넌트 중 하나로서 API Gateway 패턴을 구현할 수 있는 Zuul 을 사용했다. 이렇게 Spring Cloud + Zuul의 형태를 Spring Cloud Zuul이라고 한다. Zuul은 서블릿 프레임워크 기반으로 만들어졌기 때문에 동기(Synchronous), 블로킹(Blocking) 방식으로 서비스를 처리한다. 

- 그러다 최근(?) 비동기(Asynchronous), 논블로킹(Non-Blocking) 방식이 대세가 되면서 해당 방식을 지원하는 Zuul2가 나오게 된다. 

- 하지만 Zuul은 Spring 생태계의 취지와 맞지 않아, Spring Cloud Gateway에서는 Zuul2를 사용하지 않고 API Gatewway 패턴을 구현할 수 있는 Spring Cloud Gateway를 새로 만들게 된다. 

- Spring Cloud Gateway도 Zuul2와 마찬가지로 비동기, 논블로킹 방식을 지원한다. 또한 Spring 기반으로 만들어졌기 때문에 Spring 서비스와의 호환도 좋다. 더 나아가 최근에 Zuul2와 Spring Cloud Gateway의 성능을 비교하는 글이 많이 올라오는데 Spring Boot2와 Spring Cloud2가 릴리즈 된 이후에는 Spring Cloud Gateway가 성능이 더 좋다는 분석도 있다(참고)

- Spring Cloud Gateway는 Netty 런타임 기반으로 동작한다. 때문에 서블릿 컨테이너나 WAR로 빌드된 경우 동작하지 않는다.
 
2. Spring Cloud Gateway(SCG)로 API Gateway 구축하기

2-1. Spring Boot 서비스 생성 & build.gradle 수정(Spring Cloud Gateway 포함시키기)


build.gradle : 

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:2.2.3.RELEASE")
    }
}

apply plugin: 'java'
apply plugin: 'eclipse'
apply plugin: 'idea'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'


repositories {
    mavenCentral()
}
group = 'com.project.demo'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = 1.11
targetCompatibility = 1.11

dependencyManagement {
    imports {
//        mavenBom "org.springframework.cloud:spring-cloud-dependencies:Greenwich.SR2"
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:Hoxton.RELEASE"
    }
}

dependencies {
    compile("org.springframework.cloud:spring-cloud-starter-gateway")
    compile("org.springframework.cloud:spring-cloud-starter-netflix-hystrix")
    compile("org.springframework.cloud:spring-cloud-starter-contract-stub-runner"){
        exclude group: "org.springframework.boot", module: "spring-boot-starter-web"
    }
    testCompile("org.springframework.boot:spring-boot-starter-test")
    compileOnly 'org.projectlombok:lombok'
    annotationProcessor 'org.projectlombok:lombok'
}
위 build.gradle 파일에 대해 간략히 설명하자면

 

- buildscrpit.dependencies : Spring Boot 2.2.3 버전을 사용했다. 이 글을 작성할 때 Spring Boot 2.2.3 버전이 Spring Cloud Gateway에서 사용하는 GA(Generally Available) 버전이었다.

- dependencyManagement.imports : Spring Boot 2.2.x 버전을 사용하게 되면 Spring Cloud를 Hoxton 버전을 사용해야 한다(Spring Boot 2.1.X 버전에서는 Spring Cloud Greenwich 버전을 사용한다)

- spring-cloud-starter-gateway : Spring Cloud Gateway를 위한 라이브러리

- spring-cloud-starter-netflix-hystrix : netflix에서 Circuit Breaker Pattern을 구현한 라이브러리. MSA에서 장애 전파를 방지해주는 역할을 수행한다. 하지만 hystrix github에 가보면 더이상 개발하지 않고 유지보수만 진행한다고 나와있으며 resilience4j를 사용하기를 추천한다. 이번 글에서만 netflix-hystrix를 사용해보고 다음에는 resilience4j를 써보려고 한다.

- spring-cloud-starter-contract-stub-runner : Contract Test를 하기 위한 라이브러리. Contract Test(서비스 제공자와 사용자간의 계약을 검증하는 테스트. 자세한 설명은 생략한다) 를 하지 않으려면 제외해도 된다(필자는 Gateway 작성 가이드를 보고 하느라고 추가시키긴 했다)

작성이 완료되었으면 build.gradle의 의존성에 있는 라이브러리를 받기 위한 Re-import를 수행하자.

 

 

2-2. Spring Cloud Gateway 코드 작성

- Route(경로) : 게이트웨이의 기본 골격이다. ID, 목적지 URI, 조건부(predicate) 집합, 필터(filter) 집합으로 구성된다.  조건부가 맞게 되면 해당하는 경로로 이동하게 된다. 
- Predicate(조건부) : Java8의 Function Predicate이다. Input Type은 Spring Framework ServerWebExchange이다. 조건부를 통해 Header 나 Parameter같은 HTTP 요청의 모든 항목을 비교할 수 있다.
- Filter(필터) : 특정 팩토리로 구성된 Spring Framework GatewayFilter 인스턴스다. Filter에서는 다운스트림 요청 전후에 요청/응답을 수정할 수 있다. 

Gateway는 2개의 마이크로서비스(user-svc, cafe-svc)를 라우팅 할 것이다.

application.yml : 

server:
  port: 8080
---
spring:
  cloud:
    gateway:
      default-filters:
        - name: GlobalFilter
          args:
            baseMessage: Spring Cloud Gateway GlobalFilter
            preLogger: true
            postLogger: true
      routes:
        - id: user-svc
          uri: http://localhost:8081/
          predicates:
            - Path=/user/**
          filters:
            - name: UserFilter
              args:
                baseMessage: Spring Cloud Gateway UserFilter
                preLogger: true
                postLogger: true
        - id: cafe-svc
          uri: http://localhost:8082/
          predicates:
            - Path=/cafe/**
          filters:
            - name: CafeFilter
              args:
                baseMessage: Spring Cloud Gateway CafeFilter
                preLogger: true
                postLogger: true
 

- server.port : Spring Cloud Gateway가 동작하는 port 번호
- spring.cloud.gateway.default-filters : Spring Cloud Gateway 공통 Filter
- spring.cloud.gateway.routes : 총 두 개의 마이크로서비스(user-svc, cafe-svc)를 라우팅한다. 

user-svc는 http://localhost:8081로 서비스가 구동되고 있으며, Gateway predicate(조건부)가 path=/user/** 이므로, 
GatewayURL(localhost:8080)/user/ 주소로 서비스가 유입되면 user-svc를 호출하게 된다. 
예를 들면 http://localhost:8080/user로 서비스를 호출하면, http://localhost:8081/user 서비스가 호출된다. 

cafe-svc는 http://localhost:8082로 서비스가 구동되고 있으며, Gateway predicate(조건부)가 path=/cafe/** 이므로, 
GatewayURL(localhost:8080)/cafe/ 주소로 서비스가 유입되면 cafe-svc를 호출하게 된다. 
예를 들면 http://localhost:8080/cafe로 서비스를 호출하면, http://localhost:8082/cafe 서비스가 호출된다.


- spring.cloud.gateway.routes.filters : 각 서비스(user-svc, cafe-svc) 호출 전 호출되는 필터(filter)이다. 해당 필터들도 Java Class로 추가 구현을 해줘야 한다. agrs는 필터 소스 내에서 사용되는 인자값이다. args는 필요없으면 작성하지 않아도 된다.

다음으로 application.yml에 적용된 3개의 필터를 Java Class로 구현해보도록 하자.

GlobalFilter, UserFilter, CafeFilter 를 구현하면 되고, 각 Filter는 모두 클래스 명과 로그 내용을 제외하고는 모두 동일하게 구현했기 때문에 이 글에서는 GlobalFilter만 작성했다.

GlobalFilter.java : 

import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
    private static final Logger logger = LogManager.getLogger(GlobalFilter.class);
    public GlobalFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            logger.info("GlobalFilter baseMessage>>>>>>" + config.getBaseMessage());
            if (config.isPreLogger()) {
                logger.info("GlobalFilter Start>>>>>>" + exchange.getRequest());
            }
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                if (config.isPostLogger()) {
                    logger.info("GlobalFilter End>>>>>>" + exchange.getResponse());
                }
            }));
        });
    }

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
 

위 코드에 대해 간단히 설명하자면

- AbstractGatewayFilterFactory : Gateway를 구현하기 위해서는 GatewayFilterFactory를 구현해야 하며, 상속할 수 있는 추상 클래스가 바로 AbstractGatewayFilterFactory이다. 
- exchange : 서비스 요청/응답값을 담고있는 변수로, 요청/응답값을 출력하거나 변환할 때 사용한다. 요청값은 (exchange, chain) -> 구문 이후에 얻을 수 있으며, 서비스로부터 리턴받은 응답값은 Mono.fromRunnable(()-> 구문 이후부터 얻을 수 있다.
- config : application.yml에 선언한 각 filter의 args(인자값) 사용을 위한 클래스
 

2-3. 마이크로 서비스 생성

Gateway Filter 3개 작성이 완료되었으면 이제 해당 Gateway에서 호출하는 마이크로 서비스를 스프링부트로 만들어 보자. 
서비스는 게이트웨이에서 호출하는 두 개의 마이크로 서비스를 만들 것이다(서비스명은 user-svc, cafe-svc)

application.yml : 

server:
  port: 8081
 

다음으로 UserController를 신규로 만들어서 아래와 같이 작성하자.

UserController.java : 

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @GetMapping("/info")
    public Mono<String> getUser(ServerHttpRequest request, ServerHttpResponse response) {
        logger.info("User MicroService Start>>>>>>>>");
        HttpHeaders headers = request.getHeaders();
        headers.forEach((k, v) -> {
            logger.info(k + " : " + v);
        });
        logger.info("User MicroService End>>>>>>>>");

        return Mono.just("This is User MicroService!!!!!");
    }
}
 

 두번째 마이크로서비스(cafe-svc)를 만들어보자(두번째 서비스는 포트가 8082이다)

application.yml : 

server:
  port: 8082
 

CafeController : 

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cafe")
public class CafeController {
    private static final Logger logger = LogManager.getLogger(CafeController.class);

    @GetMapping("/info")
    public Mono<String> getUser(ServerHttpRequest request, ServerHttpResponse response) {
        logger.info("Cafe MicroService Start>>>>>>>>");
        HttpHeaders headers = request.getHeaders();
        headers.forEach((k, v) -> {
            logger.info(k + " : " + v);
        });
        logger.info("Cafe MicroService End>>>>>>>>");

        return Mono.just("This is Cafe MicroService!!!!!");
    }
}
 



 GET localhost:8080/cafe/info 요청을 보내보자.

그러면 아래와 같이 두번째 마이크로서비스(cafe-svc) 가 호출될 것이다. 성공이다
 

3. 마치며
이번 글에서 구현된 API Gateway는 URI별로 각기 다른 서비스를 호출하는 기능을 가지고 있다. 그렇다면 Kubernetes의 Ingress와도 어찌보면 같은 비슷한 기능처럼 보인다. 그럼 Kuberentes Ingress를 사용하게 되면 API Gateway를 안써도 되는걸까?

필자는 toy project 진행 시 Kuberentes 기반으로 아키텍쳐를 구성하고, Ingress를 통해서 kubernetes 내 컨테이너 서비스들을 URI별로 다르게 호출했지만, 결국 API Gateway를 도입하게 되었다. 이유는 서비스 인증/보안 등의 기능을 Ingress에서 구현할 수 없었기 때문이다(JWT검증 기능 등). 때문에 해당 기능을 구현하기 위해 결국 API Gateway를 만들게 되었다. 

이번 글에서는 API Gateway를 간단하게 구현해 보았다.

다음 글에서는 API Gateway와 분산된 마이크로서비스간의 로그 트레이싱을 위한 Zipkin & Sleuth를 적용해보도록 하겠다.