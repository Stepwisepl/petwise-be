package pl.stepwise.petwise.vision.model.localizedobject;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode
@ToString
@Builder
@Getter
public class PetwiseBoundingPoly {

    private List<PetwiseNormalizedVertex> normalizedVertices;
}
