package com.jjara.microservice.configuration.r2dbc;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Duration;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
public class ConnectionConfiguration {

    @Bean public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get(builder()
                .option(DRIVER, "postgresql")
                .option(HOST, "tai.db.elephantsql.com")
                .option(PORT, 5432)  // optional, defaults to 5432
                .option(USER, "bgcbxomr")
                .option(PASSWORD, "YjxvlY5aUclBTDGyGyib80ETsVQWZuR-")
                .option(DATABASE, "bgcbxomr")  // optional
                .build());

    }

    @Bean public ConnectionPool connectionPool(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        return new ConnectionPool(ConnectionPoolConfiguration.builder(connectionFactory)
                .maxIdleTime(Duration.ofMillis(1000))
                .maxSize(5)
                .build());
    }


}
