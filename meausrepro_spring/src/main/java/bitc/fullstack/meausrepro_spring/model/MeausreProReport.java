package bitc.fullstack.meausrepro_spring.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class MeausreProReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idx")
    private int idx; // 리포트 idx

    @ManyToOne
    @JoinColumn(name="user_idx", nullable = false)
    private MeausreProUser userIdx; // 사용자

    @ManyToOne
    @JoinColumn(name="section_id", nullable = false)
    private MeausreProSection sectionId; // 구간 번호

    @Column(name = "file_name", nullable = false)
    private String fileName; // 업로드된 파일명

    @Column(name = "file_path", nullable = false)
    private String filePath; // 업로드된 파일 경로

    @Column(name = "upload_date", nullable = false)
    private String uploadDate; // 파일 업로드 일시
}
