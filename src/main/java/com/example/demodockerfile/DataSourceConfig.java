package com.example.demodockerfile;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.driver-class-name}")
    private String dbDriverClassName;

    @Value("${javax.net.ssl.trustStore:}")
    private String trustStore;

    @Value("${javax.net.ssl.trustStorePassword:}")
    private String trustStorePassword;

    @Bean
    @Profile("dev")
    public DataSource prodDataSource() {
        try {
            configurarSSL();
            return crearDataSource();
        } catch (Exception e) {
            log.error("Error al configurar el DataSource para producción", e);
            throw new RuntimeException("Error al configurar el DataSource para producción", e);
        }
    }



    private DriverManagerDataSource crearDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUser);
        dataSource.setPassword(dbPassword);
        dataSource.setDriverClassName(dbDriverClassName);
        return dataSource;
    }

    private void configurarSSL() {
        if (trustStore != null && !trustStore.isEmpty()) {
            System.setProperty("javax.net.ssl.trustStore", trustStore);
            System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
            log.info("Configuración SSL aplicada con trustStore: {}", trustStore);
        } else {
            log.warn("TrustStore no está configurado para el perfil 'prod'");
        }
    }
}
