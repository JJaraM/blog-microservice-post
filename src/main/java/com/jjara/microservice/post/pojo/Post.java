package com.jjara.microservice.post.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Object that identifies a Post instance
 */
@Document
@Getter @Setter @NoArgsConstructor(access = AccessLevel.PACKAGE)
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
