package com.uyoung.youtubeclone;


import com.uyoung.youtubeclone.service.VideoService;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class GetAllVideosBenchmark {

    private VideoService videoService;
    private ConfigurableApplicationContext context;

    @Setup(Level.Trial)
    public void setup() {
        // Spring Boot 애플리케이션 컨텍스트 초기화
        context = SpringApplication.run(YoutubeCloneApplication.class);
        // 서비스 객체 가져오기
        videoService = context.getBean(VideoService.class);
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        context.close();
    }

    @Benchmark
    public void measureGetAllVideos_OldVersion() {
        videoService.getAllVideos_OldVersion();
    }

    @Benchmark
    public void measureGetAllVideos() {
        videoService.getAllVideos();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(GetAllVideosBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(5)
                .measurementIterations(10)
                .build();

        new Runner(opt).run();
    }
}
