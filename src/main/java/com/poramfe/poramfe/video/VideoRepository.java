package com.poramfe.poramfe.video;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
    // 추가적인 쿼리 메서드를 정의할 수 있음
}
