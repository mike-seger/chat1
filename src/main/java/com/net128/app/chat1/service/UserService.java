package com.net128.app.chat1.service;

import com.net128.app.chat1.model.UserContext;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    UserContext getUserContext(HttpServletRequest request);
}
