import {FormControl, FormGroup} from "@angular/forms";
import {Component, inject, Input} from '@angular/core';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {ActivatedRoute} from "@angular/router";
import {VideoService} from "../video.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatChipEditedEvent, MatChipInputEvent} from '@angular/material/chips';
import {LiveAnnouncer} from '@angular/cdk/a11y';
import { PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import {VideoDto} from "../video-dto";


@Component({
  selector: 'app-save-video-details',
  templateUrl: './save-video-details.component.html',
  styleUrl: './save-video-details.component.css',
})
export class SaveVideoDetailsComponent {
  // 동영상 메타데이터 Form
  saveVideoDetailsForm: FormGroup;
  title: FormControl = new FormControl('');
  description: FormControl = new FormControl('');
  videoStatus: FormControl = new FormControl('');
  selectable = true;
  removable = true;

  // 동영상 메타데이터 Form (태그) matchip
  addOnBlur = true;
  readonly separatorKeysCodes = [ENTER, COMMA] as const;
  tags: string[] = [];

  // 동영상 썸네일
  selectedFile!: File;
  selectedFileName = '';
  videoId = '';
  fileSelected = false;

  // 동영상 재생 부분
  @Input() videoUrl!: string;
  thumbnailUrl!: string;

  constructor(private activatedRoute: ActivatedRoute, private videoService: VideoService,
              private matSnackBar: MatSnackBar) {
    this.videoId = this.activatedRoute.snapshot.params['videoId'];

    this.videoService.getVideo(this.videoId).subscribe(data => {
      this.videoUrl = data.videoUrl;
      this.thumbnailUrl = data.thumbnailUrl;
    })

    this.saveVideoDetailsForm = new FormGroup({
      title: this.title,
      description: this.description,
      videoStatus: this.videoStatus,
    })
  }

  announcer = inject(LiveAnnouncer);

  add(event: MatChipInputEvent): void {
      const value = (event.value || '').trim();

      // Add our tag
      if (value) {
        this.tags.push(value);
      }

      // Clear the input value
      event.chipInput!.clear();
    }

    remove(tag: string): void {
      const index = this.tags.indexOf(tag);

      if (index >= 0) {
        this.tags.splice(index, 1);

        this.announcer.announce(`Removed ${tag}`);
      }
    }

    edit(tag: string, event: MatChipEditedEvent) {
      const value = event.value.trim();

      // Remove tag if it no longer has a name
      if (!value) {
        this.remove(tag);
        return;
      }

      // Edit existing tag
      const index = this.tags.indexOf(tag);
      if (index >= 0) {
        this.tags[index] = value;
      }
    }

  onFileSelected(event: Event) {
    // @ts-ignore
    this.selectedFile = event.target.files[0];
    this.selectedFileName = this.selectedFile.name;
    this.fileSelected = true;
  }

  onUpload() {
    this.videoService.uploadThumbnail(this.selectedFile, this.videoId)
      .subscribe(() => {
        // show an upload success notification.
        this.matSnackBar.open("Thumbnail Upload Successful", "OK");
      })
  }

    saveVideo() {
      // Call the video service to make a http call to our backend
      const videoMetaData: VideoDto = {
        "id": this.videoId,
        "title": this.saveVideoDetailsForm.get('title')?.value,
        "description": this.saveVideoDetailsForm.get('description')?.value,
        "tags": this.tags,
        "videoStatus": this.saveVideoDetailsForm.get('videoStatus')?.value,
        "videoUrl": this.videoUrl,
        "thumbnailUrl": this.thumbnailUrl,
        "likeCount": 0,
        "dislikeCount": 0,
        "viewCount": 0
      }
      console.log(videoMetaData);
      this.videoService.saveVideo(videoMetaData).subscribe(data => {
        this.matSnackBar.open("Video Metadata Updated successfully", "OK");
      })
    }

}
