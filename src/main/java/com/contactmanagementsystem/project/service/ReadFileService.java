package com.contactmanagementsystem.project.service;

import com.contactmanagementsystem.project.exception.PhoneNoAlreadyPresent;
import com.contactmanagementsystem.project.model.User;
import com.contactmanagementsystem.project.repository.UserRepository;
import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;

@Service
public class ReadFileService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CountryToPhonePrefix countryToPhonePrefix;

    @SneakyThrows
    public boolean readDataFromExcel(MultipartFile file) {
        boolean isFlag = false;
        Workbook workbook = new HSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        rows.next();
        while (rows.hasNext()) {
            Row row = rows.next();
            User user = new User();
            if (row.getCell(0).getCellType() == CellType.STRING) {
                user.setName(row.getCell(0).getStringCellValue());
            }
            if (row.getCell(1).getCellType() == CellType.STRING) {
                user.setAddress(row.getCell(1).getStringCellValue());
            }
            if (row.getCell(2).getCellType() == CellType.STRING) {
                user.setCountryCode(row.getCell(2).getStringCellValue());
            }
            if (row.getCell(3).getCellType() == CellType.NUMERIC) {
                user.setPh(NumberToTextConverter.toText(row.getCell(3).getNumericCellValue()));
            } else if (row.getCell(3).getCellType() == CellType.STRING) {
                user.setPh(row.getCell(3).getStringCellValue());
            }
            if (row.getCell(4).getCellType() == CellType.STRING) {
                user.setEmail(row.getCell(4).getStringCellValue());
            }
            String code = countryToPhonePrefix.prefixCode(user.getCountryCode());
            user.setPh(code.concat(user.getPh()));
            System.out.println(user.getPh());
            if (userRepository.existsByPh(user.getPh()))
                throw new PhoneNoAlreadyPresent("Phone Number already present");
            else
                userRepository.save(user);
        }

        return true;
    }

}
