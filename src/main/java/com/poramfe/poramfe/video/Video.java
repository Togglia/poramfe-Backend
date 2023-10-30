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
    private byte[] file; // videoData 대신 file로 변경

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
    public void setVideoData(byte[] bytes) {
        this.file = file;
    }

    public byte[] getVideoContent() {
        return file;
    }

    public String getFileName() {
        return title;
    }


}



