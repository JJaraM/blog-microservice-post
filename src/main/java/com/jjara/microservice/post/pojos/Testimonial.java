package com.jjara.microservice.post.builder;

import java.util.Date;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter @Setter @NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Testimonial {

	@Id
	private int id;
	private String name;
	private String text;
	private Date date;
	private String title;
	private String img;
	
}
