package com.contactmanagementsystem.project.service;

import com.contactmanagementsystem.project.exception.PhoneNoAlreadyPresent;
import com.contactmanagementsystem.project.model.User;
import com.contactmanagementsystem.project.repository.UserRepository;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.IntStream;


@Service
public class UploadService {
    @Autowired
    private UserRepository userRepository;

    /**
     * @param file
     * @throws Exception
     */
    public void upload(MultipartFile file) throws Exception {
        Path path = Files.createTempDirectory("");
        File tempFile = path.resolve(file.getOriginalFilename()).toFile();
        file.transferTo(tempFile);
        Workbook workbook = WorkbookFactory.create(tempFile);
        Sheet sheet = workbook.getSheetAt(0);
        IntStream.range(0, sheet.getPhysicalNumberOfRows())
                .skip(1)
                .forEach(index -> {
                    User user = new User();

                    user.setName(sheet.getRow(index).getCell(0).toString());
                    user.setAddress(sheet.getRow(index).getCell(1).toString());
                    user.setCountryCode(sheet.getRow(index).getCell(2).toString());
                    user.setPh(CountryToPhonePrefix.prefixCode(user.getCountryCode())
                            .concat(sheet.getRow(index).getCell(3).toString()));
                    user.setEmail(sheet.getRow(index).getCell(4).toString());

                    if (userRepository.existsByPh(user.getPh())) {
                        throw new PhoneNoAlreadyPresent("Phone Number already present");
                    } else {
                        userRepository.save(user);
                    }
                    System.out.println(user);
                });

    }
}
