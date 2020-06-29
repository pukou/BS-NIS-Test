package com.bsoft.nis.core.restful.api;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 *  按模块区分接口API
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
@ComponentScan(basePackages = {"com.bsoft.nis.controller"})
public class SwaggerConfig extends WebMvcConfigurationSupport{
    private Boolean showSwagger = false;
    private Predicate<RequestHandler> handlerPredicate = RequestHandlerSelectors.any();

    @Bean
    public Docket swaggerSpringMvcPlugin() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo("护理评估 RESTFul API","1.0"))
                    .groupName("护理评估-api")
                    .select()   // 选择那些路径和api会生成document
                    .apis(RequestHandlerSelectors.basePackage("com.bsoft.nis.controller.evaluation"))
                    //.paths(paths())
                    .apis(handlerPredicate)  // 对所有api进行监控
                    .paths(PathSelectors.any())   // 对所有路径进行监控
                    .build();
    }

    @Bean
    public Docket commonDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("生命体征-api")
                .apiInfo(apiInfo("生命体征 RESTFul API","1.0"))
                .pathMapping("/")
                .select()
                .apis(handlerPredicate)
                .paths(PathSelectors.any())
                .build();
    }



    private Contact contact() {
        return new Contact("Wilson", "http://blog.csdn.net/z28126308", "z28126308@163.com");
    }
    private ApiInfo apiInfo(String title,String apiVersion) {
        return new ApiInfoBuilder()
                .title(title)
                .termsOfServiceUrl("")
                .description("此API提供HTTP协议调用")
                .license("License Version 2.0")
                .licenseUrl("")
                .version(apiVersion).build();
    }

    public Boolean getShowSwagger() {
        return showSwagger;
    }

    public void setShowSwagger(Boolean showSwagger) {
        this.showSwagger = showSwagger;
        if (showSwagger){
            handlerPredicate = RequestHandlerSelectors.any();
        }else{
            handlerPredicate = RequestHandlerSelectors.none();
        }
    }
}
