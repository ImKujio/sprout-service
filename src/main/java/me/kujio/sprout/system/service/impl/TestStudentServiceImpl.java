package me.kujio.sprout.system.service.impl;

import me.kujio.sprout.core.service.TableServiceImpl;
import me.kujio.sprout.system.entity.TestStudent;
import me.kujio.sprout.system.service.TestStudentService;
import org.springframework.stereotype.Service;

@Service
public class TestStudentServiceImpl extends TableServiceImpl<TestStudent> implements TestStudentService {

}
