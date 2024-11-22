package com.tech_symfony.resource_server.system.importing;

import com.tech_symfony.resource_server.api.user.User;
import com.tech_symfony.resource_server.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface ImportExcelService {
    public List<User> importFrom(MultipartFile file, boolean isStudent) throws IOException;
}

@Service
@RequiredArgsConstructor
class DefaultImportExcelService<T> implements ImportExcelService {

    private final UserRepository userRepository;

    @Override
    public List<User> importFrom(MultipartFile file, boolean isStudent) throws IOException {

        List<User> newUsers = new ArrayList<>();

        // get file extension
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        if (file.getContentType() == null) {
            return null;
        } else if (file.getContentType().equals("application/vnd.ms-excel")
                || file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            File tmp = File.createTempFile("tmp_", extension);
            file.transferTo(tmp);
            try (FileInputStream fileInputStream = new FileInputStream(tmp);
                 Workbook workbook = new XSSFWorkbook(fileInputStream)) {

                Sheet sheet = workbook.getSheetAt(0);

                String emailRegexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // Skip header row
                    try {
                        User user = new User();
                        String fullName = row.getCell(2).getStringCellValue()
                                + " " + row.getCell(3).getStringCellValue();
                        user.setFullName(fullName);
                        user.setEmail(row.getCell(7).getStringCellValue());
                        user.setUsername(row.getCell(1).getStringCellValue());
                        user.setIsStudent(isStudent);
                        user.setPassword("password");
                        if (user.getFullName().isEmpty()
                        || user.getEmail().isEmpty()
                        || user.getUsername().isEmpty()
                        || !user.getEmail().matches(emailRegexPattern))
                            continue;

                        newUsers.add(user);
                    } catch (IllegalStateException ex) {
                        // ignore and continue
                    }
                }
            }
            Set<String> existingUsernames = new HashSet<>(userRepository
                    .findByUsernameIn(newUsers.stream().map(User::getUsername).collect(Collectors.toSet()))
                    .stream()
                    .map(user -> user.getUsername())
                    .collect(Collectors.toSet()));
            newUsers = newUsers.stream()
                    .filter(user -> !existingUsernames.contains(user.getUsername()))
                    .collect(Collectors.toList());
            newUsers.forEach(System.out::println);
            return userRepository.saveAll(newUsers);
        }
        return null;
    }
}
