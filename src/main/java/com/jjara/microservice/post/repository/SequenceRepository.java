package com.jjara.microservice.post.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.jjara.microservice.post.pojo.Sequence;

@Repository
public class SequenceRepository {

	@Autowired private MongoOperations mongoOperation;
	
	private final String KEY = "post";

	public long getNextSequenceId() {

		// get sequence id
		Query query = new Query(Criteria.where("_id").is(KEY));

		// increase sequence id by 1
		Update update = new Update();
		update.inc("seq", 1);

		// return new increased id
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);

		// this is the magic happened.
		Sequence seqId = mongoOperation.findAndModify(query, update, options, Sequence.class);

		return seqId.getSeq();

	}

}
