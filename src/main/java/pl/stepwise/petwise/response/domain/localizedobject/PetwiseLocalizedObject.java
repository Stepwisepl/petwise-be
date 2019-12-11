package pl.stepwise.petwise.response.domain.localizedobject;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Builder
@Getter
public class PetwiseLocalizedObject {

    private String objectName;
    private PetwiseBoundingPoly boundingPoly;
}
