package com.jjara.microservice.ws.post.pojos;

import java.util.*;

public class PostBuilder {

    private long id;
    private long views;
    private String title;
    private String content;
    private String image;
    private Date createDate;
    private Date updateDate;
    private List<Long> tags = new ArrayList();
    private Set<String> ips = new HashSet<>();
    private String description;
    private String link;

    private PostBuilder() {

    }

    private PostBuilder(Post post) {
        this.id = post.getId();
        this.views = post.getViews();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.image = post.getImage();
        this.createDate = post.getCreateDate();
        this.updateDate = post.getUpdateDate();
        this.tags = post.getTags();
        this.description = post.getDescription();
        this.link = post.getLink();
        this.ips = post.getIps();
    }

    public static PostBuilder newInstance() {
        return new PostBuilder();
    }

    public static PostBuilder newInstance(Post post) {
        return new PostBuilder(post);
    }

    public PostBuilder addIp(List<String> ip) {
        ips.addAll(ip);
        return this;
    }

    public PostBuilder setId(long id) {
        this.id = id;
        return this;
    }

    public PostBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public PostBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public PostBuilder setImage(String image) {
        this.image = image;
        return this;
    }

    public PostBuilder setCreateDate(Date createDate) {
        this.createDate = createDate;
        return this;
    }

    public PostBuilder setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
        return this;
    }

    public PostBuilder setTags(List<Long> tags) {
        this.tags = tags;
        return this;
    }

    public PostBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public PostBuilder setViews(long views) {
        this.views = views;
        return this;
    }

    public PostBuilder setLink(String link) {
        this.link = link;
        return this;
    }

    public Post build() {
        Post post = new Post();
        post.setId(id);
        post.setViews(views);
        post.setTitle(title);
        post.setContent(content);
        post.setImage(image);
        post.setCreateDate(createDate);
        post.setUpdateDate(updateDate);
        post.setTags(tags);
        post.setDescription(description);
        post.setLink(link);
        post.setIps(ips);
        return post;
    }

}
