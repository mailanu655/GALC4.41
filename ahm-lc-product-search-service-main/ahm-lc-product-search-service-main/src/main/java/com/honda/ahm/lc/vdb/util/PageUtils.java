package com.honda.ahm.lc.vdb.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongSupplier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.PageableExecutionUtils;

public class PageUtils {
	
	public static <T> Page<T> extractPage(Pageable page, List<T> contents) {
        int startIndex = page.getPageNumber() == 0 ? page.getPageNumber() : page.getPageNumber() + page.getPageSize();
        int toIndex = startIndex + page.getPageSize();
        toIndex = toIndex > contents.size() ? contents.size() : toIndex;

        final int total = contents.size();

        List<T> filteredContents;

        if (startIndex < contents.size()) {
            filteredContents = contents.subList(startIndex, toIndex);
        } else {
            filteredContents = new ArrayList<>();
        }
        
        LongSupplier totalSupplier = () -> {
            return total;
        };

        return PageableExecutionUtils.getPage(filteredContents, gotoPage(page.getPageNumber(), page.getPageSize(), page.getSort()),
                totalSupplier);
    }
	
	private static PageRequest gotoPage(int number, int size, Sort sort) {
        return PageRequest.of(number, size, sort);
    }


}
