package me.kujio.sprout.core.conifg;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import me.kujio.sprout.core.resolver.QueryArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new QueryArgumentResolver());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        config.setWriterFeatures(JSONWriter.Feature.WriteNonStringKeyAsString);
        // 自定义LocalDate序列化格式
        JSON.register(LocalDate.class, (jsonWriter, object, fieldName, fieldType, features) -> {
            if (object == null) {
                jsonWriter.writeNull();
                return;
            }
            LocalDate localDate = (LocalDate) object;
            jsonWriter.writeString(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        });
        // 自定义LocalTime序列化格式
        JSON.register(LocalTime.class, (jsonWriter, object, fieldName, fieldType, features) -> {
            if (object == null) {
                jsonWriter.writeNull();
                return;
            }
            LocalTime localTime = (LocalTime) object;
            jsonWriter.writeString(localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        });
        converter.setFastJsonConfig(config);
        converters.add(0,converter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:"+FilesConfig.getUploadFull());

        registry.addResourceHandler("download/**")
                .addResourceLocations("file:"+FilesConfig.getDownloadFull());

        registry.addResourceHandler("/resource/**")
                .addResourceLocations("file:"+FilesConfig.getResourceFull());
    }
}


