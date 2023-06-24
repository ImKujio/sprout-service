package me.kujio.sprout.core.conifg;

import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import me.kujio.sprout.core.resolver.QueryArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FilesConfig filesConfig;

    public WebConfig(FilesConfig filesConfig) {
        this.filesConfig = filesConfig;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new QueryArgumentResolver());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setWriterFeatures(JSONWriter.Feature.WriteNonStringKeyAsString);
        converter.setFastJsonConfig(config);
        converters.add(converter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:"+FilesConfig.getUpload()+"/");

        registry.addResourceHandler("download/**")
                .addResourceLocations("file:"+FilesConfig.getDownload() + "/");

        registry.addResourceHandler("/resource/**")
                .addResourceLocations("file:"+FilesConfig.getResource() + "/");
    }
}


