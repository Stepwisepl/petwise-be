package pl.stepwise.petwise.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "file")
@Getter
@Setter
@Validated
public class FileUploadConfiguration {

    @NotNull
    private String bucketName;
}
