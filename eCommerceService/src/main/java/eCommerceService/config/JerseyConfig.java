package eCommerceService.config;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.RegexPatternTypeFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import javax.annotation.PostConstruct;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
@Component
@ApplicationPath("/")
public class JerseyConfig extends ResourceConfig {
  public JerseyConfig() {

    // packages("eCommerceService"); is Not working.
    // See https://github.com/spring-projects/spring-boot/issues/1468 for more details
    ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
   //provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*"))); // Overkill
    provider.addIncludeFilter(new AnnotationTypeFilter(Provider.class));
    provider.addIncludeFilter(new AnnotationTypeFilter(Path.class));
    provider.addIncludeFilter(new RegexPatternTypeFilter(Pattern.compile(".*Controller")));

    Set<BeanDefinition> candidateComponents = provider.findCandidateComponents("eCommerceService");
    for (BeanDefinition candidateComponent : candidateComponents) {
      register(ClassUtils.resolveClassName(candidateComponent.getBeanClassName(), ClassUtils.getDefaultClassLoader()));
    }
  }

  @PostConstruct
  public void init() {
    // Register components where DI is needed
    configureSwagger();
  }

  private void configureSwagger() {
    // Available at localhost:port/swagger.json
    this.register(ApiListingResource.class);
    this.register(SwaggerSerializers.class);

    BeanConfig config = new BeanConfig();
    config.setConfigId("eCommerceService");
    config.setTitle("eCommerceService APIs");
    config.setVersion("v1");
    config.setSchemes(new String[] {"http", "https"});
    config.setBasePath("/");
    config.setResourcePackage("eCommerceService.controller"); // set resource package
    config.setPrettyPrint(true);
    config.setScan(true);
  }
}
