import { Injectable } from '@angular/core';
import { OidcSecurityService } from 'angular-auth-oidc-client';
import { HttpClient } from "@angular/common/http";
import { filter } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private oidcSecurityService: OidcSecurityService,
              private httpClient: HttpClient) {
    // 여기에서 isAuthenticated 속성을 검사하여 boolean 값을 반환하도록 수정
    this.oidcSecurityService.isAuthenticated$
      .pipe(filter((result) => result.isAuthenticated)) // 수정된 부분
      .subscribe(() => {
        this.login().subscribe(response => {
          console.log('Login successful', response);
        }, error => {
          console.error('Login failed', error);
        });
      });
  }

  login() {
    return this.httpClient.get("http://localhost:8080/api/user/login", {responseType: "text"});
  }
}
