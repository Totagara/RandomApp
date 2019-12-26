package com.randomapps.randomapp.utils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public final class PartitionHelper<T> extends AbstractList<List<T>> {

    private final List<T> list;
    private final int partitionSize;

    public PartitionHelper(List<T> list, int partitionSize) {
        this.list = new ArrayList<>(list);
        this.partitionSize = partitionSize;
    }

    public static <T> PartitionHelper<T> ofSize(List<T> list, int partitionSize) {
        return new PartitionHelper<T>(list, partitionSize);
    }

    @Override
    public List<T> get(int index) {
        int start = index * partitionSize;
        int end = Math.min(start + partitionSize, list.size());

        if (start > end) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of the list range <0," + (size() - 1) + ">");
        }

        return new ArrayList<>(list.subList(start, end));
    }

    @Override
    public int size() {
        return (int) Math.ceil((double) list.size() / (double) partitionSize);
    }
}
