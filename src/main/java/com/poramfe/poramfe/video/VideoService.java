package com.poramfe.poramfe.video;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class VideoService {

    @Autowired
    private VideoRepository videoRepository;

    public void uploadVideo(String title, MultipartFile videoFile) {
        try {
            if (videoFile != null && !videoFile.isEmpty()) {
                Video video = new Video();
                video.setTitle(title);
                byte[] file = videoFile.getBytes();
                video.setFile(file); // videoData 대신 file로 설정
                videoRepository.save(video);
            } else {
                throw new IllegalArgumentException("Video file is empty or null.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload video");
        }
    }
}
