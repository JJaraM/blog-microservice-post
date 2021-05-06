package com.jjara.microservice.ws.post.repository;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.jjara.microservice.pojo.Sequence;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation that generate the primary key for mongo db
 *
 * For more information please visit the following links:
 *      https://www.baeldung.com/spring-data-mongodb-reactive
 *      https://github.com/eugenp/tutorials/blob/master/persistence-modules/spring-boot-persistence-mongodb/src/main/java/com/baeldung/mongodb/services/SequenceGeneratorService.java
 *      https://www.baeldung.com/spring-data-mongodb-tutorial
 */
@Service
public class SequenceRepository {

	private final ReactiveMongoTemplate reactiveMongoTemplate;
	private final String KEY = "seq";
	private final String ID = "_id";

	public SequenceRepository(ReactiveMongoTemplate reactiveMongoTemplate) {
		this.reactiveMongoTemplate = reactiveMongoTemplate;
	}

	public Mono<Sequence> getNextSequenceId(final String key) {
		// get sequence id
		var query = new Query(Criteria.where(ID).is(key));

		// increase sequence id by 1
		var update = new Update();
		update.inc(KEY, 1);


		var options = new FindAndModifyOptions();
		options.returnNew(true);

		// this is the magic happened.
		return reactiveMongoTemplate.findAndModify(query, update, options, Sequence.class, key.concat("_sequence"));

	}

}
