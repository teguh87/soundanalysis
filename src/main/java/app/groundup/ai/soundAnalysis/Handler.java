package app.groundup.ai.soundAnalysis;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.sql.Timestamp;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@AllArgsConstructor
@Component
@Slf4j
public class Handler {
    private final DataRepository posts;

    public Mono<ServerResponse> all(ServerRequest req) {
        return posts.findAll()
                .map(p -> {
                    p.setDetected(new Timestamp(Long.valueOf(p.getDetected().toString())));
                    return p;
                })
                .collectList()
                .flatMap(post -> ok().contentType(APPLICATION_STREAM_JSON)
                            .bodyValue(post)
                            .onErrorResume(err -> ServerResponse.status(HttpStatus.NOT_FOUND).build()));
    }

    public Mono<ServerResponse> get(ServerRequest req) {
        return this.posts.findById(req.pathVariable("id"))
                .map(p -> {
                    p.setDetected(new Timestamp(Long.valueOf(p.getDetected().toString())));
                    return p;
                })
                .flatMap(post -> ok().contentType(APPLICATION_JSON)
                        .bodyValue(post)
                        .onErrorResume(err -> ServerResponse.status(HttpStatus.NOT_FOUND).build()));

    }

    public Mono<ServerResponse> update(ServerRequest req)  {
        log.info("update id {}", req.pathVariable("id"));
        var data = req.bodyToMono(Post.class);
        var post = posts.findById(req.pathVariable("id"));
        return Mono.zip(post, data, (p, d) -> {
            p.setCause(d.getCause());
            p.setSeverity(d.getSeverity());
            p.setComments(d.getComments());
            p.setFrom(d.getFrom());

            return p;
        })
        .flatMap(p -> posts.save(p))
        .flatMap(p -> ok().contentType(APPLICATION_JSON).bodyValue(p)
                .onErrorResume(err -> ServerResponse.status(HttpStatus.NOT_MODIFIED).build()));
    }

    public Mono<ServerResponse> create(ServerRequest req) {
        return req.bodyToMono(Post.class).
                flatMap(p -> posts.save(p))
                .flatMap(p -> ServerResponse.created(URI.create("/posts/" + p.getId())).build());
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
        return ServerResponse.noContent().build(posts.deleteById(req.pathVariable("id")));
    }

}
