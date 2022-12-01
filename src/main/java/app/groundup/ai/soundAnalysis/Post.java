package app.groundup.ai.soundAnalysis;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.sql.Timestamp;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sound")
public class Post {
    @Id
    String      id;
    String      sensor;
    Integer     from;
    Integer     cause;
    Integer     severity;
    String      comments;
    Object      detected;
    String      audio;

    @Field("new")
    boolean     isNew;
}
