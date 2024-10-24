package bitc.fullstack.meausrepro_spring.controller;

import bitc.fullstack.meausrepro_spring.model.MeausreProReport;
import bitc.fullstack.meausrepro_spring.service.ReportService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/MeausrePro/report")
public class ReportController {

    private final ReportService reportService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 서버 시작 시 디렉토리 존재 여부 확인 및 생성
    @PostConstruct
    public void init() {
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("업로드 디렉토리 생성 실패", e);
            }
        }
    }

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadReport(@RequestParam("file") MultipartFile file,
                                          @RequestParam("sectionId") int sectionId,
                                          @RequestParam("userId") int userId) {
        try {
            // 파일 저장 경로 설정
            String fileName = file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);

            // 파일 저장
            Files.write(filePath, file.getBytes());

            // 서버 URL 생성 (정적 경로를 통해 접근할 수 있는 URL)
            String fileUrl = "http://localhost:8080/reports/" + fileName;

            // 파일 정보 DB 저장
            MeausreProReport report = new MeausreProReport();
            report.setFileName(fileName);
            report.setFilePath(fileUrl);
            report.setUploadDate(LocalDateTime.now().toString());

            // 서비스 호출하여 보고서 저장
            reportService.saveReport(report, sectionId, userId);

            return ResponseEntity.ok("파일 업로드 성공");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }
    }

    // 구간별 report 리스트
    @GetMapping("/reports/{sectionId}")
    public List<MeausreProReport> getReportsBySection(@PathVariable int sectionId) {
        return reportService.getReportsBySection(sectionId);
    }

    // 리포트 다운로드
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadDir, fileName);  // 파일 경로 설정
            Resource resource = new UrlResource(filePath.toUri());  // 파일 리소스 로드

            if (!resource.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 파일이 없을 경우 404 처리
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);  // 리소스를 반환
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // 오류 시 500 처리
        }
    }

    // 리포트 삭제
    @DeleteMapping("/delete/{idx}")
    public ResponseEntity<String> deleteReport(@PathVariable int idx) {
        return reportService.deleteByReportIdx(idx);
    }
}
