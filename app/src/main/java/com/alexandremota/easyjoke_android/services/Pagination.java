package com.alexandremota.easyjoke_android.services;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Paging for a data list
 *
 * @see ListResponse
 */
public class Pagination {

    @SerializedName("total")
    private int total;
    @SerializedName("count")
    private int count;
    @SerializedName("per_page")
    private int perPage;
    @SerializedName("current_page")
    private int currentPage;
    @SerializedName("total_pages")
    private int totalPages;

    /**
     * Get count of the items
     *
     * @return integer containing the number of the items
     */
    public int getCount() {
        return count;
    }

    /**
     * Set count of returned items
     *
     * @param count number of items
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Get request total
     *
     * @return integer containing the total number
     */
    public int getTotal() {
        return total;
    }

    /**
     * Set total number
     *
     * @param limit number
     */
    public void setTotal(int limit) {
        this.total = limit;
    }

    /**
     * Get total of elements per page
     *
     * @return
     */
    public int getPerPage() {
        return perPage;
    }

    /**
     * Set total of elements per page
     *
     * @param perPage
     */
    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    /**
     * Get current page
     *
     * @return integer containing the number of the current page
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * Set current page
     *
     * @param currentPage number of current page
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Get total of page
     *
     * @return integer containing o number of pages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * Set total of page
     *
     * @param totalPages number of pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Verify if has a next page
     *
     * @return true if has a next page otherwise false
     */
    public boolean hasNextPage() {
        return totalPages > currentPage;
    }

    /**
     * Verify if has a previous page
     *
     * @return true if has a previous page otherwise false
     */
    public boolean hasPreviousPage() {
        return currentPage < totalPages;
    }

    /**
     * Get next page number
     *
     * @return nextPage integer for the next page
     */
    public int getNextPage() {
        if (this.hasNextPage()) return this.getCurrentPage() + 1;
        return 0;
    }

    /**
     * Get previous page number
     *
     * @return nextPage integer for the next page
     */
    public int getPreviousPage() {
        if (this.hasPreviousPage()) {
            return this.getCurrentPage() - 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
