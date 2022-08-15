package com01.QQcommon;

//客户信息

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID=1L;
    private String UseId;
    private String pwd;

    public User(){}

    public User(String useId, String pwd) {
        UseId = useId;
        this.pwd = pwd;
    }

    public String getUseId() {
        return UseId;
    }

    public void setUseId(String useId) {
        UseId = useId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
