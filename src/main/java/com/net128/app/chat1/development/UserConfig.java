package com.net128.app.chat1.development;

import com.net128.app.chat1.model.JsonObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "chat1.userconfig")
@Profile({"local", "test"})
public class UserConfig {
    public static class User implements JsonObject<UserConfig> {
        private String name;
        private String password;
        private String role;

        public void setName(String name) {
            this.name = name;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }

        public String getRole() {
            return role;
        }

        public String [] getRoles() {
            return role.replace(" ", "").split(",");
        }

        @Override
        public String toString() { return toJson(); }
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    private List<User> users;

    @Override
    public String toString() {
        return "{\"UserConfig\":{\"" +  "users\":" + users +  "\"}";
    }
}
