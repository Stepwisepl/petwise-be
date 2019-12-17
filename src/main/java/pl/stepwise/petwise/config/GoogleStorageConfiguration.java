package pl.stepwise.petwise.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.stepwise.petwise.file.exception.StorageProviderException;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GoogleStorageConfiguration {

    @Bean
    public Storage storage() throws StorageProviderException {
        try {
            var credentials = GoogleCredentials.fromStream(
                    new FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS")));
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (IOException e) {
            throw new StorageProviderException("Google storage could not be provided.", e);
        }
    }
}
