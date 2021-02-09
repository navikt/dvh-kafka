package no.nav.dvh.kafka.config.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private final static Logger LOGGER =
            LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    Secret secret;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    private String dbUsername;
    private String dbPassword;

    public DataSourceConfig(Secret secret) {
        this.secret = secret;
        this.dbUsername = secret.getDbUsername();
        this.dbPassword = secret.getDbPassword();
    }

    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(jdbcUrl);
        dataSourceBuilder.username(dbUsername);
        dataSourceBuilder.password(dbPassword);
        return dataSourceBuilder.build();
    }
}
