package com.pragma.plazoleta.domain.model;


import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;

import java.util.List;


public class PageResult<T> {
    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;

    public PageResult(
            List<T> content,
            int page,
            int size,
            long totalElements
    ) {

        if (page < 0 || size <= 0) {
            throw new DomainException(
                    ErrorCode.INVALID_PAGINATION,
                    "Invalid pagination parameters"
            );
        }

        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
    }

    public List<T> getContent() { return content; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public long getTotalElements() { return totalElements; }

    public int getTotalPages() {
        return (int) ((totalElements + size - 1) / size);
    }

    public boolean hasNext() {
        return page + 1 < getTotalPages();
    }
}
