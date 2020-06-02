package com.jjara.microservice.post.configuration.webflux;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.handler.AbstractUrlHandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.resource.ResourceWebHandler;
import org.springframework.web.util.pattern.PathPattern;

import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class WebFluxConfigurationTest {

    private WebFluxConfiguration webFluxConfiguration = new WebFluxConfiguration();

    @Test
    public void addResourceHandlers() {
        final ResourceHandlerRegistryTestable registry = getResourceHandlerRegistryTestable();
        Map<String, ?> handlerMap = registry.getUrlMap();

        ResourceWebHandler resourceWebHandler = (ResourceWebHandler) handlerMap.get("/swagger-ui.html**");
        assertThat("Expected classpath:/META-INF/resources/", resourceWebHandler.getLocationValues().get(0),
                CoreMatchers.equalTo("classpath:/META-INF/resources/"));

        resourceWebHandler = (ResourceWebHandler) handlerMap.get("/webjars/**");
        assertThat("Expected classpath:/META-INF/resources/", resourceWebHandler.getLocationValues().get(0),
                CoreMatchers.equalTo("classpath:/META-INF/resources/webjars/"));
    }

    private ResourceHandlerRegistryTestable getResourceHandlerRegistryTestable() {
        final ResourceLoader resourceLoader = mock(ResourceLoader.class);
        final ResourceHandlerRegistryTestable registry = new ResourceHandlerRegistryTestable(resourceLoader);
        webFluxConfiguration.addResourceHandlers(registry);
        return registry;
    }

    public class ResourceHandlerRegistryTestable extends ResourceHandlerRegistry {

        public ResourceHandlerRegistryTestable(ResourceLoader resourceLoader) {
            super(resourceLoader);
        }

        public Map<String, ?> getUrlMap() {
            return SimpleUrlHandlerMapping.class.cast(getHandlerMapping()).getUrlMap();
        }
    }


}
