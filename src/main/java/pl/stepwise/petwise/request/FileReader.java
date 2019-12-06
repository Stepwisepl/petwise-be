package pl.stepwise.petwise.request;

import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileReader {

    public ByteString readFile(String filePath) throws IOException {
        File file = ResourceUtils.getFile("classpath:" + filePath);
        byte[] data = Files.readAllBytes(Paths.get(file.getPath()));
        return ByteString.copyFrom(data);
    }
}
