package com.bzh.cloud.maintenance.restFul;

public class CommonResponseEntity implements JsonResponseEntity{

    private String result;

    private Class<?> clazz;


    @Override
    public void init(String jsonStr) {
        result=jsonStr;
    }

    @Override
    public boolean status() {
        return false;
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public Class<?> getResponseClass() {
        return clazz;
    }

    @Override
    public String getArrayJson() {
        return result;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }
}
