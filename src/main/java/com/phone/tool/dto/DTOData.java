package com.phone.tool.dto;

public class DTOData<T> {
    private T data;

    public DTOData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}