package com.instagram.common.dto.request;

/**
 * Request DTO for search queries.
 */
public class SearchRequest {

    private String query;
    private String type; // "users", "hashtags", "posts"
    private int page;
    private int size;

    public SearchRequest() {
        this.page = 0;
        this.size = 20;
    }

    public SearchRequest(String query, String type, int page, int size) {
        this.query = query;
        this.type = type;
        this.page = page;
        this.size = size;
    }

    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
    public int getSize() { return size; }
    public void setSize(int size) { this.size = size; }
}
