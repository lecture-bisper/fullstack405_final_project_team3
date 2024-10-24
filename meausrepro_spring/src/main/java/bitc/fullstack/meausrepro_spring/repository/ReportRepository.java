package bitc.fullstack.meausrepro_spring.repository;

import bitc.fullstack.meausrepro_spring.model.MeausreProReport;
import bitc.fullstack.meausrepro_spring.model.MeausreProSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<MeausreProReport, Integer> {
    List<MeausreProReport> findBySectionId(MeausreProSection sectionId);

    // userId로 리포트 찾기
    @Query("SELECT r FROM MeausreProReport r WHERE r.userIdx.id =:userId")
    List<MeausreProReport> findByUserId(String userId);

    // sectionIdx 리포트 찾기
    @Query("SELECT r FROM MeausreProReport r WHERE r.sectionId.idx =:sectionIdx")
    List<MeausreProReport> findBySectionIdx(int sectionIdx);

    // report 삭제 시 정보 조회
    @Query("SELECT r FROM MeausreProReport r WHERE r.idx =:idx")
    Optional<MeausreProReport> findByIdx(int idx);
}
