package com.uyoung.youtubeclone.service;

import com.uyoung.youtubeclone.dto.UploadVideoResponse;
import com.uyoung.youtubeclone.dto.VideoDto;
import com.uyoung.youtubeclone.exception.ResourceNotFoundException;
import com.uyoung.youtubeclone.model.Video;
import com.uyoung.youtubeclone.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;

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

    public List<VideoDto> getAllVideos() {
        return videoRepository.findAllByOrderByViewCountDesc().stream().map(this::mapToVideoDto).collect(Collectors.toList());
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
