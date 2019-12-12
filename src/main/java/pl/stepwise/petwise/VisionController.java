package pl.stepwise.petwise;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.stepwise.petwise.exception.InvalidLocalizedObjectAmountException;
import pl.stepwise.petwise.exception.PetwiseImageProcessingException;
import pl.stepwise.petwise.response.domain.PetwiseCropHint;
import pl.stepwise.petwise.response.domain.PetwiseLabel;
import pl.stepwise.petwise.response.domain.localizedobject.PetwiseLocalizedObject;
import com.google.cloud.storage.BlobInfo;
import java.io.FileInputStream;
import com.google.cloud.storage.Acl;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(value = "api/test", produces = MediaType.APPLICATION_JSON_VALUE)
public class VisionController {

    private final VisionService visionService;

    public VisionController(VisionService visionService) {
        this.visionService = visionService;
    }

    @GetMapping("labels")
    public ResponseEntity<List<PetwiseLabel>> detectImageLabels(@RequestParam("filepath") String filePath) throws PetwiseImageProcessingException, IOException {
        return ResponseEntity.ok(visionService.detectLabels(filePath));
    }

    @GetMapping("labels-after-crop")
    public ResponseEntity<List<PetwiseLabel>> detectImageLabelsAfterCrop(@RequestParam("filepath") String filePath) throws PetwiseImageProcessingException, IOException, InvalidLocalizedObjectAmountException {
        return ResponseEntity.ok(visionService.detectLabelsAfterCroppingObject(filePath));
    }

    @GetMapping("objects")
    public ResponseEntity<List<PetwiseLocalizedObject>> detectObjectLocalization(@RequestParam("filepath") String filePath) throws PetwiseImageProcessingException {
        return ResponseEntity.ok(visionService.localizeObjects(filePath));
    }

    @GetMapping("eligible-objects")
    public ResponseEntity<PetwiseLocalizedObject> detectEligibleObjectLocalization(@RequestParam("filepath") String filePath) throws PetwiseImageProcessingException {
        return ResponseEntity.ok(visionService.getCategorizedObjects(filePath));
    }

    @GetMapping("crop")
    public ResponseEntity<List<PetwiseCropHint>> getCropHints(@RequestParam("filepath") String filePath) throws PetwiseImageProcessingException {
        return ResponseEntity.ok(visionService.getCropHints(filePath));
    }

    @PostMapping("upload-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try{
            var credentials = GoogleCredentials.fromStream(new FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS")));
            var storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
            var bucketName = "petwise-resources";
            var fileName = file.getOriginalFilename();
            List<Acl> acl = (Collections.singletonList(Acl.of(Acl.User.ofAllUsers(), Acl.Role.OWNER)));
            BlobInfo blobInfo = storage.create(BlobInfo.newBuilder(bucketName, fileName)
                    .setAcl(acl)
                    .build(), file.getInputStream());
            return ResponseEntity.ok(blobInfo.getMediaLink());
        }catch (Exception e){
            return ResponseEntity.ok("error");
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
