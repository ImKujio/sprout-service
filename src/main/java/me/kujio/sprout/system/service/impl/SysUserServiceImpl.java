package me.kujio.sprout.system.service.impl;

import cn.hutool.crypto.SecureUtil;
import me.kujio.sprout.base.entity.EntityHandle;
import me.kujio.sprout.base.entity.Where;
import me.kujio.sprout.base.service.impl.BaseServiceImpl;
import me.kujio.sprout.core.entity.AuthInfo;
import me.kujio.sprout.core.exception.SysException;
import me.kujio.sprout.core.service.AuthInfoService;
import me.kujio.sprout.system.entity.SysUser;
import me.kujio.sprout.system.service.SysUserService;
import me.kujio.sprout.utils.CacheUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService, AuthInfoService {

    public SysUserServiceImpl(EntityHandle<SysUser> entityHandle) {
        super(entityHandle);
    }

    @Override
    public SysUser loadUserByUsername(String username) throws UsernameNotFoundException {
        String cacheKey = entityHandle.entityName() + ": loadUserByUsername";
        return CacheUtils.getOrPut(cacheKey, () -> {
            List<SysUser> sysUsers = list(Where.of(Where.item("name", "=", username)));
            if (sysUsers.size() == 0) throw new SysException("用户不存在:" + username);
            return sysUsers.get(0);
        });
    }

    @Override
    public void changePassword(AuthInfo authInfo, String password) {
        SysUser user = loadUserByUsername(authInfo.getUsername());
        if (user == null) throw new SysException("用户不存在");
        user.setPassword(password);
        set(user);
    }

    @Override
    public void resetPassword(String username) {
        SysUser user = loadUserByUsername(username);
        if (user == null) throw new SysException("用户不存在");
        user.setPassword(SecureUtil.md5("123456"));
        set(user);
    }
}
