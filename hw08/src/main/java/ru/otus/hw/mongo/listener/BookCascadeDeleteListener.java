import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

@Component
public class BookCascadeDeleteListener extends AbstractMongoEventListener<Book> {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public BookCascadeDeleteListener(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Book> event) {
        String bookId = event.getDocument().get("_id").toString();
        mongoTemplate.remove(Query.query(Criteria.where("book._id").is(bookId)), Comment.class);
    }
}