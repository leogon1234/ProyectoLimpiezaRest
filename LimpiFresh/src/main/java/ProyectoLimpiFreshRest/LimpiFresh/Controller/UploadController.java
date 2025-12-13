package ProyectoLimpiFreshRest.LimpiFresh.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UploadController {

    // En EC2 te conviene guardar en /opt/backend/uploads
    private static final Path UPLOAD_DIR = Paths.get("/opt/backend/uploads");

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("Archivo vacío");
            }

            // Crear carpeta si no existe
            Files.createDirectories(UPLOAD_DIR);

            String original = StringUtils.cleanPath(file.getOriginalFilename() == null ? "imagen" : file.getOriginalFilename());
            String ext = "";

            int dot = original.lastIndexOf('.');
            if (dot >= 0) ext = original.substring(dot);

            // nombre único
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
            String filename = "img_" + stamp + ext;

            Path target = UPLOAD_DIR.resolve(filename).normalize();
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            // URL pública (la servimos con /uploads/**)
            String url = "/uploads/" + filename;

            return ResponseEntity.ok(Map.of("url", url, "filename", filename));

        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Error guardando archivo: " + e.getMessage());
        }
    }
}
