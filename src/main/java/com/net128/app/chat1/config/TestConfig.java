package com.net128.app.chat1.config;

import com.net128.app.chat1.model.JsonObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "testconfig")
public class TestConfig implements JsonObject<TestConfig> {
    public static class User {
        public String name;
        public String password;
        public String role;

        public void setName(String name) {
            this.name = name;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
    public void setValue1(String value1) {
        this.value1 = value1;
    }
//
//    public void setUsers(List<User> users) {
//        this.users = users;
//    }

    public void setUser(User user) {
        this.user = user;
    }

    public User user;
    //public List<User> users;
    public String value1;

    public String toString() {
        return toJson();
    }

}
