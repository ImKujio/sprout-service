package me.kujio.sprout.system.service.impl;

import me.kujio.sprout.base.entity.EntityHandle;
import me.kujio.sprout.base.service.impl.BaseServiceImpl;
import me.kujio.sprout.system.entity.SysUser;
import me.kujio.sprout.system.service.SysUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService {
    public SysUserServiceImpl(ApplicationContext context, EntityHandle<SysUser> entityHandle) {
        super(context, entityHandle);
    }
}
