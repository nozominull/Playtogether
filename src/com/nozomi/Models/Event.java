package com.nozomi.Models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Long time;
	private String position;
	private String creatorName;
	private Integer attend;
	private Integer review;

	public Event() {
		super();
	}

	public Event(Integer id, String name, Long time, String position,
			String creatorName, Integer attend, Integer review) {
		super();
		this.id = id;
		this.name = name;
		this.time = time;
		this.position = position;
		this.creatorName = creatorName;
		this.attend = attend;
		this.review = review;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Integer getAttend() {
		return attend;
	}

	public void setAttend(Integer attend) {
		this.attend = attend;
	}

	public Integer getReview() {
		return review;
	}

	public void setReview(Integer review) {
		this.review = review;
	}

}
