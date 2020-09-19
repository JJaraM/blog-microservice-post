package com.jjara.microservice.post.shell.status.mongo;

import com.jjara.microservice.post.shell.status.api.StatusShellProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoStatusShellProvider implements StatusShellProvider {

  @Value("${spring.data.mongodb.databaseName}") private String databaseName;
  @Value("${spring.data.mongodb.uri}") private String uri;

  @Override
  public String status() {
    var status = new StringBuilder();
    status.append("Database name: ").append(databaseName).append(System.lineSeparator());
    status.append("uri: ").append(uri).append(System.lineSeparator());
    return status.toString();
  }

}
