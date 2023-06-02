package me.kujio.sprout.system.service.impl;

import me.kujio.sprout.base.entity.EntityHandle;
import me.kujio.sprout.base.service.impl.BaseServiceImpl;
import me.kujio.sprout.system.entity.TestStudent;
import me.kujio.sprout.system.service.TestStudentService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class TestStudentServiceImpl extends BaseServiceImpl<TestStudent> implements TestStudentService {
    public TestStudentServiceImpl(ApplicationContext context, EntityHandle<TestStudent> entityHandle) {
        super(context, entityHandle);
    }
}
