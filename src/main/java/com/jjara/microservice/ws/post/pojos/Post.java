package com.jjara.microservice.ws.post.pojos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Object that identifies a Post instance
 */
@Data
@Document
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Post {

	@Id
	private long id;
	private long views;
	private String title;
	private String content;
	private String image;
	private Date createDate;
	private Date updateDate;
	private List<Long> tags = new ArrayList();
	private String description;
	private String link;

}
