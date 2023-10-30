package com.poramfe.poramfe.video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
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
    public ResponseEntity<InputStreamResource> streamVideo(@PathVariable Long videoId) {
        Optional<Video> videoOptional = videoRepository.findById(videoId);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();

            InputStreamResource videoStream = new InputStreamResource(new ByteArrayInputStream(video.getFile()));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("video/mp4")); // 영상의 MIME 타입을 설정

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(videoStream);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}