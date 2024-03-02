import { Component, Inject, Input, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-video-player',
  templateUrl: './video-player.component.html',
  styleUrl: './video-player.component.css'
})
export class VideoPlayerComponent {

  @Input() videoUrl!: string | '';

  // 플랫폼이 브라우저인지 아닌지를 저장할 변수
  isBrowser: boolean;

  constructor(@Inject(PLATFORM_ID) platformId: Object) {
    // 플랫폼이 브라우저인지 아닌지 체크
    this.isBrowser = isPlatformBrowser(platformId);
  }

}
