import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import { UploadVideoResponse } from './upload-video/UploadVideoResponse';
import {VideoDto} from "./video-dto";


@Injectable({
  providedIn: 'root'
})
export class VideoService {

  constructor(private httpClient: HttpClient) {  // HttpClient 모듈을 VideoService 클래스에 주입하는 생성자
  }

  uploadVideo(fileEntry: File): Observable<UploadVideoResponse> {

    const formData = new FormData()
    formData.append('file', fileEntry, fileEntry.name)

    // HTTP Post call to upload the video
    return this.httpClient.post<UploadVideoResponse>("http://localhost:8080/api/videos", formData);  // response를 <UploadVideoResponse> 로 반환
  }

  uploadThumbnail(fileEntry: File, videoId: string): Observable<string> {
    const formData = new FormData()
    formData.append('file', fileEntry, fileEntry.name);
    formData.append('videoId', videoId);

    // HTTP Post call to upload the thumbnail
    return this.httpClient.post("http://localhost:8080/api/videos/thumbnail", formData, {
      responseType: 'text'
    });
  }

  getVideo(videoId: string): Observable<VideoDto> {
    return this.httpClient.get<VideoDto>("http://localhost:8080/api/videos/" + videoId);
  }

  saveVideo(videoMetaData: VideoDto): Observable<VideoDto> {
    return this.httpClient.put<VideoDto>("http://localhost:8080/api/videos", videoMetaData);
  }
}
