package me.kujio.sprout.base.mapper;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class BaseMapperHolder implements ApplicationContextAware {

    private static BaseMapper baseMapper;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        baseMapper = applicationContext.getBean(BaseMapper.class);
    }

    public static BaseMapper getBaseMapper() {
        return baseMapper;
    }
}
