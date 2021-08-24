package no.nav.dvh.kafka.consumer.datasource.config;

import no.nav.dvh.kafka.consumer.datasource.personidentity.PersonIdentity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "no.nav.dvh.kafka.consumer.datasource.personidentity",
        entityManagerFactoryRef = "personidentityEntityManagerFactory",
        transactionManagerRef = "personidentityTransactionManager"
)
class DsPersonIdentityConfig {

    @Bean(name = "personidentityDataSource")
    @ConfigurationProperties(prefix = "dvh-kafka.database.personidentity")
    public DataSource mkidentDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "personidentityEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean personidentityEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mkidentDataSource())
                .packages(PersonIdentity.class)
                .properties(DsUtil.hibernateNamingStrategy())
                .build();
    }

    @Bean
    public PlatformTransactionManager mkidentTransactionManager(
            final @Qualifier("personidentityEntityManagerFactory") LocalContainerEntityManagerFactoryBean
                    personidentityEntityManagerFactory) {
        return new JpaTransactionManager(personidentityEntityManagerFactory.getObject());
    }

}
