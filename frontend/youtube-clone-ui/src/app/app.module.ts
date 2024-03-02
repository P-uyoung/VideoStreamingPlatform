import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UploadVideoComponent } from './upload-video/upload-video.component';
import { HttpClientModule } from '@angular/common/http';
import { NgxFileDropModule } from 'ngx-file-drop';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { HeaderComponent } from './header/header.component';
import {MatButtonModule} from "@angular/material/button";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import { SaveVideoDetailsComponent } from './save-video-details/save-video-details.component';
import { FlexLayoutModule } from '@angular/flex-layout';
import { FlexLayoutServerModule } from '@angular/flex-layout/server';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {MatOptionModule} from "@angular/material/core";
import {MatInputModule} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { MatChipsModule } from '@angular/material/chips';
import {VgCoreModule} from '@videogular/ngx-videogular/core';
import {VgControlsModule} from '@videogular/ngx-videogular/controls';
import {VgOverlayPlayModule} from '@videogular/ngx-videogular/overlay-play';
import {VgBufferingModule} from '@videogular/ngx-videogular/buffering';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { VideoPlayerComponent } from './video-player/video-player.component';
import { AuthConfigModule } from './auth/auth-config.module';
import { AuthInterceptor, AuthModule } from 'angular-auth-oidc-client';
import {HTTP_INTERCEPTORS} from '@angular/common/http';


@NgModule({
declarations: [
  AppComponent,
  UploadVideoComponent,
  HeaderComponent,
  SaveVideoDetailsComponent,
  VideoPlayerComponent,
],
imports: [
  BrowserModule,
  AppRoutingModule,
  FormsModule,
  HttpClientModule,
  NgxFileDropModule,
  MatButtonModule,
  MatToolbarModule,
  MatIconModule,

  FlexLayoutModule,
  FlexLayoutServerModule,
  MatFormFieldModule,
  MatSelectModule,
  MatOptionModule,
  MatInputModule,
  ReactiveFormsModule,
  MatChipsModule,

  VgCoreModule,
  VgControlsModule,
  VgOverlayPlayModule,
  VgBufferingModule,
  MatSnackBarModule,
  AuthConfigModule,

],
  providers: [
    provideClientHydration(),
    provideAnimationsAsync(),
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
