package com.nozomi.Models;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Eventdetail implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String content;
	private Long time;
	private String position;
	private Double longitude;
	private Double latitude;
	private Integer creatorId;
	private String creatorName;
	private Integer attend;
	private Integer mark;
	private Integer review;

	public Eventdetail() {
		super();
	}

	public Eventdetail(Integer id, String name, String content, Long time,
			String position, Double longitude, Double latitude,
			Integer creatorId, String creatorName, Integer attend,
			Integer mark, Integer review) {
		super();
		this.id = id;
		this.name = name;
		this.content = content;
		this.time = time;
		this.position = position;
		this.longitude = longitude;
		this.latitude = latitude;
		this.creatorId = creatorId;
		this.creatorName = creatorName;
		this.attend = attend;
		this.mark = mark;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
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

	public Integer getAttend() {
		return attend;
	}

	public void setAttend(Integer attend) {
		this.attend = attend;
	}

	public Integer getMark() {
		return mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
	}

	public Integer getReview() {
		return review;
	}

	public void setReview(Integer review) {
		this.review = review;
	}

}
