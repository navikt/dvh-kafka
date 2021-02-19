package no.nav.dvh.kafka.konsument;

import no.nav.dvh.kafka.consumer.EnableKafkaConsumer;
import org.springframework.boot.SpringApplication;

@EnableKafkaConsumer
public class KonsumentApplication {

	public static void main(String[] args) {
		SpringApplication.run(KonsumentApplication.class, args);
	}

}
