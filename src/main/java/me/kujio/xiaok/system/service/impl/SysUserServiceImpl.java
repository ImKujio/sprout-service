package me.kujio.xiaok.system.service.impl;

import me.kujio.xiaok.base.entity.EntityHandle;
import me.kujio.xiaok.base.service.impl.BaseServiceImpl;
import me.kujio.xiaok.system.entity.SysUser;
import me.kujio.xiaok.system.service.SysUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService {
    public SysUserServiceImpl(ApplicationContext context, EntityHandle<SysUser> entityHandle) {
        super(context, entityHandle);
    }
}
