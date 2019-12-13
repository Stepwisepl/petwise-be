package pl.stepwise.petwise.vision.model.localizedobject;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import pl.stepwise.petwise.vision.model.Category;

@EqualsAndHashCode
@ToString
@Builder
@Getter
public class PetwiseLocalizedObject {

    private String objectName;
    private PetwiseBoundingPoly boundingPoly;
    private Category category;

    public boolean hasEligibleCategory() {
        return this.getCategory().isValid();
    }
}
