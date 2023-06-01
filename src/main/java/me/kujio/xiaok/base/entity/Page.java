package me.kujio.xiaok.base.entity;

public class Page {
    private int size;
    private int page;

    public static final int DEF_SIZE = 20;
    public static final int DEF_PAGE = 1;

    public Page() {
        this.size = DEF_SIZE;
        this.page = DEF_PAGE;
    }

    public Page(int size) {
        this.size = size;
    }

    public int getSize() {
        return size > 0 ? size : DEF_SIZE;
    }

    public int getPage() {
        return page > 0 ? page - 1 : DEF_PAGE;
    }

    public static Page create(Integer[] pager) {
        Page page = new Page();
        if (pager == null) return page;
        if (pager.length > 0){
            page.size = pager[0];
        }
        if (pager.length > 1){
            page.page = pager[1];
        }
        return page;
    }

    @Override
    public String toString() {
        return '{' +
                "size=" + size +
                ", page=" + page +
                '}';
    }
}
