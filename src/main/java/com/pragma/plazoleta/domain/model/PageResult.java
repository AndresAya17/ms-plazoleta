package com.pragma.plazoleta.domain.model;


import com.pragma.plazoleta.domain.exception.DomainException;
import com.pragma.plazoleta.domain.exception.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResult<T> {
    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean hasNext;

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
        this.totalPages = (int) ((totalElements + size - 1) / size);
        this.hasNext = page + 1 < this.totalPages;
    }
}
