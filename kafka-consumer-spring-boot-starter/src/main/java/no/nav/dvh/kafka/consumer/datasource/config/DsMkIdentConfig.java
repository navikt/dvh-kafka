package no.nav.dvh.kafka.consumer.datasource.config;

import no.nav.dvh.kafka.consumer.datasource.mkident.model.MkIdent;
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
        basePackages = "no.nav.dvh.kafka.consumer.datasource.mkident",
        entityManagerFactoryRef = "mkidentEntityManagerFactory",
        transactionManagerRef = "mkidentTransactionManager"
)
class DsMkIdentConfig {

    @Bean(name = "mkIdentDataSource")
    @ConfigurationProperties(prefix = "database.mkident")
    public DataSource mkidentDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mkidentEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mkidentEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mkidentDataSource())
                .packages(MkIdent.class)
                .properties(DsUtil.hibernateNamingStrategy())
                .build();
    }

    @Bean
    public PlatformTransactionManager mkidentTransactionManager(
            final @Qualifier("mkidentEntityManagerFactory") LocalContainerEntityManagerFactoryBean
                    mkidentEntityManagerFactory) {
        return new JpaTransactionManager(mkidentEntityManagerFactory.getObject());
    }

}
