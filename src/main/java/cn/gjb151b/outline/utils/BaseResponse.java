package cn.gjb151b.outline.utils;

/**
 * data 回传数据
 * status 错误状态 取值： success error
 * message 错误信息 当status 为 error 时生效
 */
public class BaseResponse<T> {
    private String status;
    private T data;
    private String message;

    public BaseResponse() {
        this.status = "error";
        this.data = null;
        this.message = "";
    }

    public BaseResponse(T data) {
        this.setResponse(data);
    }

    public void setError(String msg) {
        this.status = "error";
        this.message = msg;
    }

    public void setError(ServiceException exception) {
        this.setError(exception.getExceptionEnums().getErrMsg());
    }

    public void setResponse(T data) {
        this.status = "success";
        this.data = data;
        this.message = "";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
