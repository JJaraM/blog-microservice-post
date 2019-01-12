package com.jjara.microservice.post.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "post_sequence")
public class Sequence {

	private String id;
	private long seq;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

}
