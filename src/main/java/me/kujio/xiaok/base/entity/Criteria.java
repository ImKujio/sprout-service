package me.kujio.xiaok.base.entity;

import java.util.ArrayList;
import java.util.Comparator;

public class Criteria extends ArrayList<Where> {

    @Override
    public String toString() {
        this.sort(Comparator.comparing(Where::toString));
        return super.toString();
    }
}
