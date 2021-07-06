package no.nav.dvh.kafka.consumer;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.lang.annotation.*;

@Inherited
@EntityScan
@Documented
@Configuration
@EnableJpaRepositories
@SpringBootApplication
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import( {CommonConfig.class} )
public @interface EnableKafkaConsumer {
}
