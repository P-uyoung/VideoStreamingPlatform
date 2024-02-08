package com.uyoung.youtubeclone.repository;

import com.uyoung.youtubeclone.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video, String> {
}
