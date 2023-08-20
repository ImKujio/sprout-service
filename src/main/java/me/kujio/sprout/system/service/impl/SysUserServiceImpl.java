package me.kujio.sprout.system.service.impl;

import cn.hutool.crypto.SecureUtil;
import me.kujio.sprout.core.entity.AuthInfo;
import me.kujio.sprout.core.exception.SysException;
import me.kujio.sprout.core.service.AuthInfoService;
import me.kujio.sprout.core.service.TableServiceImpl;
import me.kujio.sprout.system.entity.SysUser;
import me.kujio.sprout.system.service.SysUserService;
import me.kujio.sprout.utils.CacheUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends TableServiceImpl<SysUser> implements SysUserService, AuthInfoService {

    @Override
    public SysUser loadUserByUsername(String username) throws UsernameNotFoundException {
        return CacheUtils.getOrPut(cacheKey("loadUserByUsername", username), () -> {
            SysUser sysUser = get("name", username);
            if (sysUser == null) throw new SysException("用户不存在:" + username);
            return sysUser;
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
