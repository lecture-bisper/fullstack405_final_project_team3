package bitc.fullstack.meausrepro_spring.service;

import bitc.fullstack.meausrepro_spring.model.MeausreProReport;
import bitc.fullstack.meausrepro_spring.model.MeausreProSection;
import bitc.fullstack.meausrepro_spring.model.MeausreProUser;
import bitc.fullstack.meausrepro_spring.repository.ReportRepository;
import bitc.fullstack.meausrepro_spring.repository.SectionRepository;
import bitc.fullstack.meausrepro_spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SectionRepository sectionRepository;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository, SectionRepository sectionRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
    }

    // 보고서 저장
    public MeausreProReport saveReport(MeausreProReport report, int sectionId, int userId) {
        // 유저 정보 조회
        MeausreProUser user = userRepository.findByIdx(userId).orElseThrow(() ->
                new IllegalArgumentException("유저가 존재하지 않습니다."));

        // 구간 정보 조회
        MeausreProSection section = sectionRepository.findByIdx(sectionId).orElseThrow(() ->
                new IllegalArgumentException("구간이 존재하지 않습니다."));

        // 보고서에 유저와 구간 정보 설정
        report.setUserIdx(user);
        report.setSectionId(section);

        return reportRepository.save(report);
    }

    // 구간별 보고서 가져옴
    public List<MeausreProReport> getReportsBySection(int sectionId) {
        MeausreProSection section = sectionRepository.findByIdx(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 구간이 존재하지 않습니다."));
        return reportRepository.findBySectionId(section);
    }

    // 보고서 다운로드 파일 조회
    public Optional<MeausreProReport> getReportById(int reportId) {
        return reportRepository.findById(reportId);
    }

    // 유저 삭제 시, 리포트도 함께 삭제
    public void deleteByUserId(String userId) {
        List<MeausreProReport> reports = reportRepository.findByUserId(userId);

        for (MeausreProReport report : reports) {
            // 파일 삭제 로직 분리
            deleteFile(report.getFilePath());
            // 리포트 데이터베이스에서 삭제
            reportRepository.delete(report);
        }
    }

    // 구간 삭제 시, 리포트도 함께 삭제
    public void deleteBySectionIdx(int sectionIdx) {
        List<MeausreProReport> reports = reportRepository.findBySectionIdx(sectionIdx);

        for (MeausreProReport report : reports) {
            // 파일 삭제 로직 분리
            deleteFile(report.getFilePath());
            // 리포트 데이터베이스에서 삭제
            reportRepository.delete(report);
        }
    }

    // 리포트 번호로 삭제
    public ResponseEntity<String> deleteByReportIdx(int idx) {
        Optional<MeausreProReport> reportOpt = reportRepository.findByIdx(idx);
        if (reportOpt.isPresent()) {
            MeausreProReport report = reportOpt.get();

            // 파일 삭제 로직 분리
            deleteFile(report.getFilePath());

            // 데이터베이스에서 리포트 삭제
            reportRepository.deleteById(idx);
            return ResponseEntity.ok("리포트 삭제 성공");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("리포트를 찾을 수 없습니다.");
        }
    }

    // 파일 삭제 로직을 별도로 분리
    private void deleteFile(String filePath) {
        String localFilePath = filePath.replace("http://localhost:8080", "");
        File file = new File("/path/to/local/reports/directory" + localFilePath);

        if (file.exists()) {
            if (!file.delete()) {
                System.out.println("파일 삭제 실패: " + file.getPath());
            }
        } else {
            System.out.println("파일이 존재하지 않습니다: " + file.getPath());
        }
    }
}
