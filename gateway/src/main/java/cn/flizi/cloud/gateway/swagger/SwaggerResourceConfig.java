package cn.flizi.cloud.gateway.swagger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import javax.annotation.Resource;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Primary
public class SwaggerResourceConfig implements SwaggerResourcesProvider {
    protected static final String API_URI = "/v2/api-docs";

    @Resource
    private RouteLocator routeLocator;

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();

        Map<String, URI> routesMap = new HashMap<>(8);

        routeLocator.getRoutes().filter(route -> route.getUri().getHost() != null)
                .filter(route -> !appName.equals(route.getUri().getHost()))
                .subscribe(route -> routesMap.put(route.getUri().getHost(), route.getUri()));

        routesMap.forEach((name, uri) -> {
            if (name.toLowerCase().contains("gateway")) {
                return;
            }
            String url = "/" + uri.getHost().toLowerCase() + API_URI;
            SwaggerResource swaggerResource = new SwaggerResource();
            swaggerResource.setUrl(url);
            swaggerResource.setName(name.toLowerCase());
            swaggerResource.setSwaggerVersion("2.0");
            resources.add(swaggerResource);
        });
        return resources;
    }
}
