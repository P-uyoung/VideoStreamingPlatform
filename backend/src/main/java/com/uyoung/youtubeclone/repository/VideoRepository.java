package com.uyoung.youtubeclone.repository;

import com.uyoung.youtubeclone.model.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;

public interface VideoRepository extends MongoRepository<Video, String> {
    Collection<Video> findAllByOrderByViewCountDesc();
}
