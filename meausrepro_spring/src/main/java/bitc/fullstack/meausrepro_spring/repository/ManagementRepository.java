package bitc.fullstack.meausrepro_spring.repository;

import bitc.fullstack.meausrepro_spring.model.MeausreProInstrument;
import bitc.fullstack.meausrepro_spring.model.MeausreProManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ManagementRepository extends JpaRepository<MeausreProManagement, String> {
    List<MeausreProManagement> findByInstr(MeausreProInstrument instr);

    // 계측기 번호로 정보 조회
    @Query("SELECT m FROM MeausreProManagement m WHERE m.instr.idx =:insIdx")
    Optional<MeausreProManagement> findByInsIdx(int insIdx);

    // 계측기 번호로 정보 조회
    @Query("SELECT m FROM MeausreProManagement m WHERE m.instr.idx =:insIdx")
    List<MeausreProManagement> findByInsIdxList(int insIdx);
}