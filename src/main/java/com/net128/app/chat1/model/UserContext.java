package com.net128.app.chat1.model;

import java.util.Objects;

public class UserContext {
    public boolean isElevated;
    public String userId;
    public UserContext() {}
    public UserContext(boolean isElevated, String userId) {
        this.isElevated = isElevated;
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserContext that = (UserContext) o;
        return isElevated == that.isElevated &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isElevated, userId);
    }
}
