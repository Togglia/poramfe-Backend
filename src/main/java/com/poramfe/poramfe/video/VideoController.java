package com.poramfe.poramfe.video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
@CrossOrigin("*")

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("title") String title, @RequestParam("file") MultipartFile file) {
        videoService.uploadVideo(title, file);
        return new ResponseEntity<>("Video uploaded successfully", HttpStatus.OK);
    }

    @Autowired
    private VideoRepository videoRepository;

    @GetMapping("/{videoId}/stream")
    public ResponseEntity<byte[]> streamVideo(@PathVariable Long videoId) {
        Optional<Video> videoOptional = videoRepository.findById(videoId);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", video.getFileName()); // 파일 이름 동적으로 설정

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(video.getVideoContent());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
