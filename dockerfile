# Stage 1: 백엔드 및 프론트엔드 빌드
FROM maven:3.8.4-openjdk-17 as build
WORKDIR /project
# 루트 pom.xml 파일 복사
COPY pom.xml .
# 백엔드 모듈의 pom.xml 및 소스 코드 복사
COPY backend/pom.xml backend/
COPY backend/src backend/src
# 프론트엔드 모듈의 pom.xml 파일 및 소스 코드 복사
# frontend 폴더 내의 pom.xml 파일을 /project/frontend로 복사합니다.
COPY frontend/pom.xml frontend/
COPY frontend/youtube-clone-ui frontend/youtube-clone-ui
# Maven을 사용하여 백엔드 및 프론트엔드 빌드
RUN mvn clean package -DskipTests

# Stage 2: 프론트엔드 빌드 (Node.js 버전 18.19.0 사용)
FROM node:18.19.0 as frontend-build
WORKDIR /project/frontend/youtube-clone-ui
# Angular CLI 설치
RUN npm install -g @angular/cli
# 프론트엔드 의존성 파일 복사 및 설치
COPY frontend/youtube-clone-ui/package.json .
COPY frontend/youtube-clone-ui/package-lock.json .
RUN npm install
# 나머지 프론트엔드 소스 코드 복사
COPY frontend/youtube-clone-ui/ .
# Angular 빌드 (Use the production configuration if needed)
# If your angular.json is already configured for production, you can just use `ng build`
RUN ng build

# Stage 3: 최종 이미지 생성
FROM openjdk:17
WORKDIR /app
# 백엔드 애플리케이션 jar 파일 복사
COPY --from=build /project/backend/target/*.jar ./backend.jar
# 프론트엔드 빌드 결과 복사
# 복사할 Angular 빌드 결과물의 경로는 실제 Angular 빌드 후 생성되는 위치에 따라 달라질 수 있습니다.
COPY --from=frontend-build /project/frontend/youtube-clone-ui/dist/youtube-clone-ui /app/public
# 애플리케이션 실행
CMD ["java", "-jar", "backend.jar"]
