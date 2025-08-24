package com.Aeb.AebDMS.shared.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PageMeta {
    private long totalElements;
    private int size;
    private int number;
}
