package no.nav.dvh.kafka.consumer.datasource.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static no.nav.dvh.kafka.consumer.datasource.config.DsUtil.hibernateNamingStrategy;

@Configuration
@EnableTransactionManagement
class DataSourceConfig {
    @Autowired
    EntityScanPackages entityScanPackages;

    private final static Logger LOGGER =
            LoggerFactory.getLogger(DataSourceConfig.class);

    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "dvh-kafka.database.target")
    public DataSource targetDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean targetEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        String[] packages = entityScanPackages.getPackageNames().stream()
                .toArray(String[]::new);
        return builder
                .dataSource(targetDataSource())
                .packages(packages)
                .properties(hibernateNamingStrategy())
                .build();
    }

    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager targetTransactionManager(
            final @Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean
                    targetEntityManagerFactory) {
        return new JpaTransactionManager(targetEntityManagerFactory.getObject());
    }
}
