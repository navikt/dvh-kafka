package no.nav.dvh.kafka.konsument.model.dvh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Properties;

@Component
public class Secret {

    private final static Logger LOGGER =
            LoggerFactory.getLogger(Secret.class);

    @Value("${vault.path}")
    private String vaultPath;

    private Properties secretProps;

    @PostConstruct
    public void init() throws IOException {
        File secretsFolder = new File(vaultPath);
        File[] files = secretsFolder.listFiles();
        secretProps = new Properties();
        for (File file : files) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String key = file.getName();
            String value = br.readLine();
            secretProps.setProperty(key, value);
        }
    }
    public String getDbUsername() {
        return secretProps.getProperty("db.username");
    }

    public String getDbPassword() {
        return secretProps.getProperty("db.password");
    }

    public String getKafkaUsername() {
        return secretProps.getProperty("kafka.username");
    }

    public String getKafkaPassword() {
        return secretProps.getProperty("kafka.password");
    }
}
