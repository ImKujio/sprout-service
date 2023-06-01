package me.kujio.xiaok.system.service.impl;

import me.kujio.xiaok.base.entity.EntityHandle;
import me.kujio.xiaok.base.service.impl.BaseServiceImpl;
import me.kujio.xiaok.system.entity.TestStudent;
import me.kujio.xiaok.system.service.TestStudentService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class TestStudentServiceImpl extends BaseServiceImpl<TestStudent> implements TestStudentService {
    public TestStudentServiceImpl(ApplicationContext context, EntityHandle<TestStudent> entityHandle) {
        super(context, entityHandle);
    }
}
