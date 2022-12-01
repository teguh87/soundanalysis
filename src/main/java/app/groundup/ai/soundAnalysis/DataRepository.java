package app.groundup.ai.soundAnalysis;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface DataRepository extends ReactiveMongoRepository<Post, String> {
}
