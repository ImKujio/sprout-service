package me.kujio.sprout.system.service.impl;

import lombok.RequiredArgsConstructor;
import me.kujio.sprout.core.service.TableServiceImpl;
import me.kujio.sprout.system.entity.TestStudent;
import me.kujio.sprout.system.service.TestStudentService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestStudentServiceImpl extends TableServiceImpl<TestStudent> implements TestStudentService {

}
