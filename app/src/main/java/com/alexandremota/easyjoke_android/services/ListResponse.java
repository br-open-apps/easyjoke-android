package com.alexandremota.easyjoke_android.services;

import java.util.List;

/**
 * Defines the API response for a list of the DataObject and Meta data with Pagiantion
 *
 * @param <DataObject> object type for request
 * @see Response
 * @see Meta
 * @see Pagination
 */
public class ListResponse<DataObject> extends Response<List<DataObject>> {

    private Meta meta;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }
}
