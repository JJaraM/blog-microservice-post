package com.jjara.microservice.shell.status;

import com.jjara.microservice.shell.status.mongo.MongoStatusShellProvider;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class StatusShellProviderStrategy {

    @Resource private MongoStatusShellProvider mongo;

    public String getStatus(String application) {
        String status = "Application not found";
        if ("mongo".equals(application))
            status = mongo.status();
        return status;
    }
}
