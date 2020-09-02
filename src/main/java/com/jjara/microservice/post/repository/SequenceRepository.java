package com.jjara.microservice.post.repository;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.jjara.microservice.post.pojo.Sequence;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

//https://www.baeldung.com/spring-data-mongodb-reactive
//https://github.com/eugenp/tutorials/blob/master/persistence-modules/spring-boot-persistence-mongodb/src/main/java/com/baeldung/mongodb/services/SequenceGeneratorService.java
//https://www.baeldung.com/spring-data-mongodb-tutorial
@Service
public class SequenceRepository {

	@Autowired private ReactiveMongoTemplate mongoTemplate;

	public Mono<Sequence> getNextSequenceId(final String key) {

		// get sequence id
		Query query = new Query(Criteria.where("_id").is(key));

		// increase sequence id by 1
		Update update = new Update();
		update.inc("seq", 1);

		// return new increased id
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);

		// this is the magic happened.
		Mono<Sequence> seqId = mongoTemplate.findAndModify(query, update, options, Sequence.class);

		return seqId;

	}

}
