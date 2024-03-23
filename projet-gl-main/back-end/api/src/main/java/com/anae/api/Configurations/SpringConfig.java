package com.anae.api.Configurations;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import java.util.List;

import static java.util.Objects.nonNull;

@Configuration
public class SpringConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        String endpoint = "/", location = "classpath:/static/";
        this.serveDirectory(registry, endpoint, location);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedOriginPatterns("*");
    }

    private void serveDirectory(ResourceHandlerRegistry registry, String endpoint, String location) {
        String[] endpointPatterns = endpoint.endsWith("/")
                ? new String[]{endpoint.substring(0, endpoint.length() - 1), endpoint, endpoint + "**"}
                : new String[]{endpoint, endpoint + "/", endpoint + "/**"};
        registry
                .addResourceHandler(endpointPatterns)
                .addResourceLocations(location.endsWith("/") ? location : location + "/")
                .resourceChain(false)
                .addResolver(new PathResourceResolver() {
                    @Override
                    public Resource resolveResource(HttpServletRequest request, @NonNull String requestPath, @NonNull List<? extends Resource> locations, @NonNull ResourceResolverChain chain) {
                        Resource resource = super.resolveResource(request, requestPath, locations, chain);
                        if (nonNull(resource)) {
                            return resource;
                        }
                        return super.resolveResource(request, "/index.html", locations, chain);
                    }
                });
    }
}
