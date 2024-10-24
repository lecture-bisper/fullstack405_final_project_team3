package bitc.fullstack.meausrepro_spring.dto;

import bitc.fullstack.meausrepro_spring.model.MeausreProInsType;
import bitc.fullstack.meausrepro_spring.model.MeausreProInstrument;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstrumenetDTO {
    private MeausreProInstrument instrument;
    private MeausreProInsType insType;
}
