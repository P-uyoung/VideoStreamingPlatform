import {Component, OnInit} from '@angular/core';
import {OidcSecurityService} from "angular-auth-oidc-client";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'youtube-clone-ui';

  constructor(private oidcSecurityService: OidcSecurityService) {
  }

  ngOnInit(): void {
    this.oidcSecurityService.checkAuth()
      .subscribe(({ isAuthenticated }) => {
        console.log('app is authenticated', isAuthenticated);
    });
  }
}
