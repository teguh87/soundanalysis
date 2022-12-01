package app.groundup.ai.soundAnalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableMongoAuditing
public class SoundAnalysisApplication {
	public static void main(String[] args) {
		SpringApplication.run(SoundAnalysisApplication.class, args);
	}
	@Bean
	public RouterFunction<ServerResponse> routes(Handler handler) {
		return route(GET("/posts"), handler::all)
				.andRoute(POST("/posts"), handler::create)
				.andRoute(GET("/posts/{id}"), handler::get)
				.andRoute(PUT("/posts/{id}"), handler::update)
				.andRoute(DELETE("/posts/{id}"), handler::delete);
	}
}
