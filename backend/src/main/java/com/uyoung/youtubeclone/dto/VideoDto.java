package com.uyoung.youtubeclone.dto;

import com.uyoung.youtubeclone.model.Comment;
import com.uyoung.youtubeclone.model.VideoStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {

    // 해당 비디오의 메타테이터를 업데이트하기 위해 생성해야하는 필드

    private String id;
    private String title;
    private String description;
    private Set<String> tags;
    private String videoUrl;
    private VideoStatus videoStatus;
    private String thumbnailUrl;
    private int viewCount; // DTO는 주로 데이터 전송 목적으로 사용되기 때문에, 티스레드 환경에서 안전하게 카운트를 증가하는 고려가 필요없음

}
