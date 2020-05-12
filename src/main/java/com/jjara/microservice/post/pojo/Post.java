package com.jjara.microservice.post.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Object that identifies a Post instance
 */
@Document
public class Post {

	@Id
	private long id;
	private long views;
	private String title;
	private String draftTitle;
	private String content;
	private String draftContent;
	private String image;
	private String draftImage;
	private Date createDate;
	private Date updateDate;
	private List<Long> tags = new ArrayList();
	private String description;
	private String draftDescription;
	private String link;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getDraftContent() {
		return draftContent;
	}

	public void setDraftContent(String draftContent) {
		this.draftContent = draftContent;
	}

	public String getDraftImage() {
		return draftImage;
	}

	public void setDraftImage(String draftImage) {
		this.draftImage = draftImage;
	}

	public String getDraftTitle() {
		return draftTitle;
	}

	public void setDraftTitle(String draftTitle) {
		this.draftTitle = draftTitle;
	}

	public List<Long> getTags() {
		return tags;
	}

	public void setTags(List<Long> tags) {
		this.tags = tags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDraftDescription() {
		return draftDescription;
	}

	public void setDraftDescription(String draftDescription) {
		this.draftDescription = draftDescription;
	}

	public long getViews() {
		return views;
	}

	public void setViews(long views) {
		this.views = views;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
