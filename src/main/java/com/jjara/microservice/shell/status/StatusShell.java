package com.jjara.microservice.shell.status;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import javax.annotation.Resource;

@ShellComponent("status")
public class StatusShell {

    @Resource private StatusShellProviderStrategy strategy;

    @ShellMethod("Indicates the status of the component")
    public String status(@ShellOption({"-a", "-application"}) final String application) {
        return strategy.getStatus(application);
    }

}
