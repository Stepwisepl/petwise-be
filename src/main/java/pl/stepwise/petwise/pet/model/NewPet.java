package pl.stepwise.petwise.pet.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@EqualsAndHashCode
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NewPet {

    private MultipartFile image;
    private String name;
}
