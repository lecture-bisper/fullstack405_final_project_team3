package bitc.fullstack.meausrepro_spring.repository;

import bitc.fullstack.meausrepro_spring.model.MeausreProManType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ManagementTypeRepository extends JpaRepository<MeausreProManType, String> {
    // 계측기 추가 측정 값 삭제
    @Transactional
    @Modifying
    @Query("DELETE FROM MeausreProManType m WHERE m.maIdx.idx =:maIdx")
    void deleteByMaIdx(int maIdx);
}
