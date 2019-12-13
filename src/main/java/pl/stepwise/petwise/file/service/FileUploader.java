package pl.stepwise.petwise.file.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.file.exception.FileUploadException;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;

@Service
public class FileUploader {

    public Blob upload(MultipartFile file) throws FileUploadException {
        try {
            var credentials = GoogleCredentials.fromStream(new FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS")));
            var storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            var bucketName = "petwise-resources";
            var fileName = file.getOriginalFilename();
            List<Acl> acl = (Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.OWNER)));

            BlobId blobId = BlobId.of(bucketName, fileName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/*").setAcl(acl).build();
            return storage.create(blobInfo, file.getInputStream().readAllBytes());
        } catch (Exception e) {
            throw new FileUploadException("File upload failed.");
        }
    }

    //    @PostMapping("upload-file")
//    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, ModelMap modelMap) {
//        modelMap.addAttribute("file", file);
//        String bucketName = "mcqimages";
//        checkFileExtension(file.getName());
//        final String fileName = file.getName();
//
//        File file2 = convertMultiPartToFile( file );
//
//        BlobInfo blobInfo =
//                storage.create(
//                        BlobInfo
//                                .newBuilder(bucketName, fileName)
//                                .setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER))))
//                                .build()
//                        //                     file.openStream()
//                );
//        System.out.println(blobInfo.getMediaLink());
//        return blobInfo.getMediaLink();
//    }
//    private File convertMultiPartToFile(MultipartFile file ) throws IOException
//    {
//        File convFile = new File( file.getOriginalFilename() );
//        FileOutputStream fos = new FileOutputStream( convFile );
//        fos.write( file.getBytes() );
//        fos.close();
//        return convFile;
//    }
//
//
//    private void checkFileExtension(String fileName) throws ServletException {
//        if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
//            String[] allowedExt = {".jpg", ".jpeg", ".png"};
//            for (String ext : allowedExt) {
//                if (fileName.endsWith(ext)) {
//                    return;
//                }
//            }
//            throw new ServletException("file must be an image");
//        }
//        }
}
