package com.uyoung.youtubeclone.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uyoung.youtubeclone.dto.UploadVideoResponse;
import com.uyoung.youtubeclone.dto.VideoDto;
import com.uyoung.youtubeclone.exception.ResourceNotFoundException;
import com.uyoung.youtubeclone.model.Video;
import com.uyoung.youtubeclone.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public UploadVideoResponse uploadVideo(MultipartFile multipartFile) {
        String videoUrl = s3Service.uploadFile(multipartFile);
        var video = new Video();
        video.setVideoUrl(videoUrl);

        var savedVideo = videoRepository.save(video);
        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getVideoUrl());
    }

    public VideoDto editVideo(VideoDto videoDto){
        // Find the video by videoID
        var savedVideo = getVideoById(videoDto.getId());
        // Map the videoDto fields to video
        savedVideo.setTitle(videoDto.getTitle());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setThumbnailUrl(videoDto.getThumbnailUrl());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());

        // Save the video to the DB
        videoRepository.save(savedVideo);
        return videoDto;
    }

    public String uploadThumbnail(MultipartFile file, String videoId) {
        var savedVideo = getVideoById(videoId);

        String thumbnailUrl = s3Service.uploadFile(file);

        savedVideo.setThumbnailUrl(thumbnailUrl);

        videoRepository.save(savedVideo);

        return thumbnailUrl;

    }

    public Video getVideoById(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find video by id - " + videoId));
    }
    public VideoDto getVideoDetails(String videoId) {
        Video video= getVideoById(videoId);
        video.incrementViewCount();
//        userService.addVideoToHistory(videoId);
        videoRepository.save(video);
        return mapToVideoDto(video);
    }

    public List<VideoDto> getAllVideos_OldVersion() {
        return videoRepository.findAllByOrderByViewCountDesc().stream().map(this::mapToVideoDto).collect(Collectors.toList());
    }

    // Redis 사용
    public List<VideoDto> getAllVideos() {
        // Redis에서 조회
        List<VideoDto> cachedVideos = getCachedVideos();
        if (cachedVideos != null) {
            return cachedVideos;
        }

        // 캐시에 없으면 데이터베이스에서 조회
        List<VideoDto> videos = videoRepository.findAllByOrderByViewCountDesc().stream().map(this::mapToVideoDto).collect(Collectors.toList());

        // 조회한 데이터를 Redis에 캐시
        cacheVideos(videos);

        return videos;
    }

    private List<VideoDto> getCachedVideos() {
        String cacheKey = "allVideos";
        BoundValueOperations<String, Object> cache = redisTemplate.boundValueOps(cacheKey);
        return (List<VideoDto>) cache.get();
    }

    private void cacheVideos(List<VideoDto> videos) {
        String cacheKey = "allVideos";
        BoundValueOperations<String, Object> cache = redisTemplate.boundValueOps(cacheKey);
        cache.set(videos);
        cache.expire(Duration.ofHours(1));  // 캐시의 유효시간을 1시간으로 설정
    }


    private VideoDto mapToVideoDto(Video videoById) {
        VideoDto videoDto = new VideoDto();
        videoDto.setVideoUrl(videoById.getVideoUrl());
        videoDto.setThumbnailUrl(videoById.getThumbnailUrl());
        videoDto.setId(videoById.getId());
        videoDto.setTitle(videoById.getTitle());
        videoDto.setDescription(videoById.getDescription());
        videoDto.setTags(videoById.getTags());
        videoDto.setVideoStatus(videoById.getVideoStatus());
//        videoDto.setLikeCount(videoById.getLikes().get());
//        videoDto.setDislikeCount(videoById.getDisLikes().get());
//        videoDto.setViewCount(videoById.getViewCount().get());
        return videoDto;
    }
}
