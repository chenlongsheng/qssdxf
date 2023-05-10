package cn.cdsoft.modules.sys.security;

public class UserCenterResult {
    private boolean success;
    private UserCenterData data;
    private String message;
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public boolean getSuccess() {
        return success;
    }
    public UserCenterData getData() {
        return data;
    }

    public void setData(UserCenterData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

class UserCenterData{
    private Integer status;
    private UserCenterPrincipal principal;

    public UserCenterPrincipal getPrincipal() {
        return principal;
    }

    public void setPrincipal(UserCenterPrincipal principal) {
        this.principal = principal;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

class UserCenterPrincipal{
    private String id;
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}