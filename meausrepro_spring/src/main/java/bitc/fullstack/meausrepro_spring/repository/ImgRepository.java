package bitc.fullstack.meausrepro_spring.repository;

import bitc.fullstack.meausrepro_spring.model.MeausreProImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImgRepository extends JpaRepository<MeausreProImg, String> {
    // 특정 구간 이미지 보기
    @Query("SELECT i FROM MeausreProImg i WHERE i.sectionId.idx =:sectionId ORDER BY i.idx ASC")
    List<MeausreProImg> findAllBySectionId(int sectionId);

    // 이미지 찾기
    @Query("SELECT i FROM MeausreProImg i WHERE i.idx =:idx")
    Optional<MeausreProImg> findByIdx(int idx);
}
