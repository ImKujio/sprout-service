package me.kujio.xiaok.base.entity;

import java.util.ArrayList;
import java.util.Comparator;

public class Sorting extends ArrayList<Order> {

    @Override
    public String toString() {
        this.sort(Comparator.comparing(Order::toString));
        return super.toString();
    }

}
