package me.kujio.sprout.base.entity;

public class Page {
    public static final int DEF_SIZE = 20;
    public static final int DEF_PAGE = 1;

    int size;
    int page;

    public Page() {
        this.page = DEF_PAGE;
        this.size = DEF_SIZE;
    }

    public Page(int limit) {
        this.size = limit <= 0 ? DEF_SIZE : limit;
        this.page = DEF_PAGE;
    }

    public Page(Integer size, Integer page) {
        this.size = size == null || size <= 0 ? DEF_SIZE : size;
        this.page = page == null || page <= 0 ? DEF_PAGE : page;
    }

    public int getSize() {
        return size;
    }

    public int getPage() {
        return page - 1;
    }

    public int getOffset() {
        return getSize() * getPage();
    }
}
