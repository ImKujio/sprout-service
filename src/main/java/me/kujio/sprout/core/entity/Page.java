package me.kujio.sprout.core.entity;

import lombok.Getter;


@Getter
public final class Page {
    public static final int DEF_SIZE = 20;
    public static final int DEF_POS = 1;

    private final int size;
    private final int pos;

    public Page(Integer size){
        this(size,DEF_POS);
    }

    public Page(Integer size, Integer pos) {
        this.size = size == null || size < DEF_SIZE ? DEF_SIZE : size;
        this.pos = pos == null || pos < DEF_POS ? DEF_POS : pos;
    }

    public int getOffset() {
        return getSize() * (getPos() - 1);
    }

    @Override
    public String toString() {
        return "pos=" + getPos() + " pos=" + getPos();
    }

}
