package com.woodiny.my_awesome_repository.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
    
    private List<T> items;
    private String nextCursor;
    private String prevCursor;
    private int totalCount;
    private boolean hasNext;
    private boolean hasPrev;
    
    public static <T> PagedResponse<T> of(List<T> items, String nextCursor, String prevCursor, int totalCount, boolean hasNext, boolean hasPrev) {
        return PagedResponse.<T>builder()
                .items(items)
                .nextCursor(nextCursor)
                .prevCursor(prevCursor)
                .totalCount(totalCount)
                .hasNext(hasNext)
                .hasPrev(hasPrev)
                .build();
    }
}
