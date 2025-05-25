package com.example.queue.dto;

import lombok.Data;

import java.util.List;

@Data
public class ListDataDto <T> {
    private List<T> data;
}
