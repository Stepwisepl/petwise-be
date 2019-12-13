package pl.stepwise.petwise.vision.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Builder
@Getter
public class PetwiseLabel {

    private String mid;
    private String description;
    private float topicality;
    private float score;
}
