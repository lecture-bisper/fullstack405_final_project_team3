package bitc.fullstack.meausrepro_spring.service;

import bitc.fullstack.meausrepro_spring.model.MeausreProManagement;
import bitc.fullstack.meausrepro_spring.repository.ManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagementService {
    @Autowired
    private ManagementRepository managementRepository;
    @Autowired
    private ManagementTypeService managementTypeService;

    // 저장
    public MeausreProManagement save(MeausreProManagement management) {
        return managementRepository.save(management);
    }

    // 조회
    public List<MeausreProManagement> findByInstrument(int instrIdx) {
        return managementRepository.findByInsIdxList(instrIdx);
    }

    // 삭제
    public void deleteByInsId(int insIdx) {
        Optional<MeausreProManagement> management = managementRepository.findByInsIdx(insIdx);

        if (management.isPresent()) {
            managementTypeService.delete(management.get().getIdx());
            managementRepository.deleteById(String.valueOf(management.get().getIdx()));
        }
    }
}
