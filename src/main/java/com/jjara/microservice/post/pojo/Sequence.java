package com.jjara.microservice.post.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "post_sequence")
public class Sequence {

	private long seq;
	
	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

}
