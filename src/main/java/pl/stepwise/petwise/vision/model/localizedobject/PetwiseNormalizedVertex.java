package pl.stepwise.petwise.vision.model.localizedobject;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Builder
@Getter
public class PetwiseNormalizedVertex {

    private float x;
    private float y;
}
