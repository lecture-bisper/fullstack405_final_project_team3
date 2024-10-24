package bitc.fullstack.meausrepro_spring.dto;

import bitc.fullstack.meausrepro_spring.model.MeausreProInsType;
import bitc.fullstack.meausrepro_spring.model.MeausreProInstrument;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsGeometryDto {
    private String insGeometryData;
    private int idx; // 계측기 번호
    private MeausreProInstrument instrument; // 추가
    private MeausreProInsType insType; // 추가

    // 생성자 추가
    public InsGeometryDto(String insGeometryData, int idx, MeausreProInstrument instrument, MeausreProInsType insType) {
        this.insGeometryData = insGeometryData;
        this.idx = idx;
        this.instrument = instrument;
        this.insType = insType;
    }

    // 기본 생성자 추가
    public InsGeometryDto() {
    }

    // 모델 반환 메서드 구현
    public MeausreProInstrument getInstrument() {
        return this.instrument;
    }

    public MeausreProInsType getInsType() {
        return this.insType;
    }
}
