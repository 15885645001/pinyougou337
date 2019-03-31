package entity;

import java.io.Serializable;

/**
 * @描述: 返回结果封装实体类
 * @Auther: yanlong
 * @Date: 2019/3/2 21:23:03
 * @Version: 1.0
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean flag;
    private String message;

    public Result(boolean flag, String message) {
        this.flag = flag;
        this.message = message;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
