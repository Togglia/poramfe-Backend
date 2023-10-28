package com.poramfe.poramfe.video;


import jakarta.persistence.*;
import java.io.Serializable;

@Entity
public class Video implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private byte[] videoData;

    public void setTitle(String title) {
    }

    public void setVideoData(byte[] bytes) {
    }

    public byte[] getVideoContent() {
        return videoData;
    }

    public String getFileName() {
        return title;
    }
}

