import {Component, OnInit} from '@angular/core';
import {OidcSecurityService} from "angular-auth-oidc-client";
import {UserService} from "./user.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'youtube-clone-ui';

  constructor(private oidcSecurityService: OidcSecurityService,
              private userService: UserService,  // OAuth2 인증 과정의 완료를 감지 후 API 호출 위해
              ) {
  }

  ngOnInit(): void {
    this.oidcSecurityService.checkAuth()
      .subscribe(({ isAuthenticated }) => {
        console.log('app is authenticated', isAuthenticated);
    });
  }
}
