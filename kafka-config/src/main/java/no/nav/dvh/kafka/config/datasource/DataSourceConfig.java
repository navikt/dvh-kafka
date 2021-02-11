package no.nav.dvh.kafka.config.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private final static Logger LOGGER =
            LoggerFactory.getLogger(DataSourceConfig.class);

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "database.target")
    public DataSource targetDataSource() {
        return DataSourceBuilder.create().build();
    }
}
