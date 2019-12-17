package pl.stepwise.petwise.file.service.upload;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import pl.stepwise.petwise.file.exception.StorageProviderException;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class StorageProvider {

    public Storage get() throws StorageProviderException {
        try{
            var credentials = GoogleCredentials.fromStream(new FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS")));
            return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        } catch (IOException e) {
            throw new StorageProviderException("Google storage could not be provided.", e);
        }
    }
}
