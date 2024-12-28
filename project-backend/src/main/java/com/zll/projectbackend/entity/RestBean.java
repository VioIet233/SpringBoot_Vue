package com.zll.projectbackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RestBean<T> {
    @JsonProperty
    private int status;
    @JsonProperty
    private boolean success;
    @JsonProperty
    private T message;

    private RestBean(int status, boolean success, T message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

    public static <T> RestBean<T> success() {
        return new RestBean<>(200, true, null);
    }

    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, true, data);
    }

    public static <T> RestBean<T> failure(int status) {
        return new RestBean<>(status, false, null);
    }

    public static <T> RestBean<T> failure(int status , T data) {
        return new RestBean<>(status, false, data);
    }

}
