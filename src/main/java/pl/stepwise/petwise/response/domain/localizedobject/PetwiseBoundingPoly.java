package pl.stepwise.petwise.response.domain.localizedobject;

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
