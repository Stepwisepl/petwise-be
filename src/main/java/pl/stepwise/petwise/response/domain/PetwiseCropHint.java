package pl.stepwise.petwise.response.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Builder
@Getter
public class PetwiseCropHint {

    private float confidence;
}
