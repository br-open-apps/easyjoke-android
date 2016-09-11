package com.alexandremota.easyjoke_android.services;

/**
 * Defines the API response of a single object
 *
 * @param <DataObject> object type for request
 */
public class Response<DataObject> {

    private DataObject data;

    /**
     * Get response object
     *
     * @return object or null if not exists data
     */
    public DataObject getData() {
        return data;
    }

    /**
     * Set response object
     *
     * @param data object of response
     */
    public void setData(DataObject data) {
        this.data = data;
    }
}
