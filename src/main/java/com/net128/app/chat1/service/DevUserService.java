package com.net128.app.chat1.service;

import com.net128.app.chat1.model.UserContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Profile("dev")
public class DevUserService implements UserService {
    @Override
    public UserContext getUserContext(HttpServletRequest request) {
        UserContext userContext;
        if("1".equals(request.getParameter("ca"))) {
            userContext = new UserContext(true, "ca12345678");
        } else {
            userContext = new UserContext(false, "user12345678");
        }
        return userContext;
    }

    public UserContext getAdminUserContext() {
        return new UserContext(true, "admin12345678");
    }
}
