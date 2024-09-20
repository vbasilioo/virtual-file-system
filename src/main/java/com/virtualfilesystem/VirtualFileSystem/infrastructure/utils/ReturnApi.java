package com.virtualfilesystem.VirtualFileSystem.infrastructure.utils;

public class ReturnApi <T>{
    private boolean success;
    private T data;
    private String message;

    public ReturnApi(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public static <T> ReturnApi<T> success(T data, String message) {
        return new ReturnApi<T>(true, data, message);
    }

    public static <T> ReturnApi<T> error(String message) {
        return new ReturnApi<T>(false, null, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}
