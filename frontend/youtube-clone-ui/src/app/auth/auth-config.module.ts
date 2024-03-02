import { NgModule, Inject, PLATFORM_ID } from '@angular/core';
import { AuthModule } from 'angular-auth-oidc-client';
import { isPlatformBrowser } from '@angular/common';

@NgModule({
    imports: [AuthModule.forRoot({
        config: {
            authority: 'http://uyoung-videostreaming.jp.auth0.com',
            redirectUrl: isPlatformBrowser(PLATFORM_ID) ? window.location.origin : 'http://localhost:4200',
            postLogoutRedirectUri: isPlatformBrowser(PLATFORM_ID) ? window.location.origin : 'http://localhost:4200',
            clientId: 'rR5hmOdspC2gAOmg2FSBxNKJBcMXvzmn',
            usePushedAuthorisationRequests: false,
            scope: 'openid profile email', // 'openid profile offline_access ' + your scopes
            responseType: 'code',
            silentRenew: true,
            useRefreshToken: true,
            ignoreNonceAfterRefresh: true,
            secureRoutes: ['http://localhost:8080'],
            customParamsAuthRequest: {
              audience : 'http://localhost:8080',
              prompt: 'consent', // login, consent
            },
        }
    })],
    exports: [AuthModule],
})
export class AuthConfigModule {}
