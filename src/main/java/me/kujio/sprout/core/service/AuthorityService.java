package me.kujio.sprout.core.service;

import me.kujio.sprout.core.entity.AuthInfo;
import me.kujio.sprout.core.exception.SysException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthorityService {
    private final Map<String,Checker> checkerMap = new HashMap<>();

    public void register(Register register) {
        this.checkerMap.putAll(register.getCheckerMap());
    }

    public boolean permit(String authority) {
        AuthInfo authInfo = AuthInfo.loginUser();
        if (authInfo.hasAuthority("admin")) return true;
        Checker checker = checkerMap.get(authority);
        if (checker == null) throw new SysException("权限配置错误，未注册权限:"+authority);
        return checker.isPermit(authInfo);
    }

    public static class Register{
        private final Map<String,Checker> checkerMap = new HashMap<>();

        public Map<String, Checker> getCheckerMap() {
            return checkerMap;
        }

        public void set(String authority,Checker checker){
            checkerMap.put(authority,checker);
        }

        public void setDef(String authority){
            set(authority,user -> user.hasAuthority(authority));
        }
    }


    interface Checker {
        boolean isPermit(AuthInfo authInfo);
    }

}
