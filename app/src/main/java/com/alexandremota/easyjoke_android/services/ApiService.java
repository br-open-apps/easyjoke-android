package com.alexandremota.easyjoke_android.services;

public class ApiService {
    private Api api;

    public ApiService() {
        this.api = ServiceGenerator.createService(Api.class);
    }

    public Api getApi() {
        return api;
    }
}
