package com.jjara.post.pojo;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Object that identifies a Post instance
 */
@Document
public class Post {

	@Id
	private String id;
	private String title;
	private String draftTitle;
	private String content;
	private String draftContent;
	private String image;
	private String draftImage;
	private Date createDate;
	private Date updateDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

}
