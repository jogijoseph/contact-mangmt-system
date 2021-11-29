package com.contactmanagementsystem.project.util;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class UploadUtil {
    public Supplier<Stream<Row>> getRowStreamSupplier(Iterable<Row> rows) {
        return () -> getSteam(rows);
    }

    public <T> Stream<T> getSteam(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public Supplier<Stream<Integer>> cellIteratorSupplier(int end) {
        return () -> numberSteam(end);
    }

    private Stream<Integer> numberSteam(int end) {
        return IntStream.range(0,end)
                .boxed();
    }
}
