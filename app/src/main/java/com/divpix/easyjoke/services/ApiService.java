package com.divpix.easyjoke.services;

public class ApiService {
    private Api api;

    public ApiService() {
        this.api = ServiceGenerator.createService(Api.class);
    }

    public Api getApi() {
        return api;
    }
}
