package com.nozomi.Models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Review implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer creatorId;
	private String creatorName;
	private Long createTime;
	private String content;

	public Review() {
		super();
	}

	public Review(Integer creatorId, String creatorName, Long createTime,
			String content) {
		super();
		this.creatorId = creatorId;
		this.creatorName = creatorName;
		this.createTime = createTime;
		this.content = content;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
