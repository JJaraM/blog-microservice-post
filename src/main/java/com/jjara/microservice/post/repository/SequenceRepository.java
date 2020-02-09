package com.jjara.microservice.post.repository;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.jjara.microservice.post.pojo.Sequence;

import javax.annotation.Resource;

@Repository
public class SequenceRepository {

	@Resource private MongoOperations operation;
	
	public long getNextSequenceId(final String key) {

		// get sequence id
		Query query = new Query(Criteria.where("_id").is(key));

		// increase sequence id by 1
		Update update = new Update();
		update.inc("seq", 1);

		// return new increased id
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);

		// this is the magic happened.
		Sequence seqId = operation.findAndModify(query, update, options, Sequence.class);

		return seqId.getSeq();

	}

}
