package com.contactmanagementsystem.project.service;

import com.contactmanagementsystem.project.util.UploadUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class UploadService {
    @Autowired
    private UploadUtil uploadUtill;

    public List<Map<String, String>> upload(MultipartFile file) throws Exception {
        Path path = Files.createTempDirectory("");
        File tempFile = path.resolve(file.getOriginalFilename()).toFile();
        file.transferTo(tempFile);
        Workbook workbook = WorkbookFactory.create(tempFile);
        Sheet sheet = workbook.getSheetAt(0);

        Supplier<Stream<Row>> rowStreamSupplier = uploadUtill.getRowStreamSupplier(sheet);
        Row headerRow = rowStreamSupplier.get().findFirst().get();

        List<String> headerCells = uploadUtill.getSteam(headerRow)
                .map(Cell::getStringCellValue)
                .collect(Collectors.toList());

        int colCount = headerCells.size();

        return rowStreamSupplier.get()
                .skip(1)
                .map(row -> {

                    List<String> cellList = uploadUtill.getSteam(row)
                            .map(Cell::getStringCellValue)
                            .collect(Collectors.toList());
                    return uploadUtill.cellIteratorSupplier(colCount)
                            .get()
                            .collect(Collectors.toMap(
                                    index -> headerCells.get(index),
                                    index -> cellList.get(index)));
                })
                .collect(Collectors.toList());
    }
}
