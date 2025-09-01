package com.hp.gstreviewfeedbackapp.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "file_master", schema = "analytics")
public class FileMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_master_seq")
	@SequenceGenerator(name = "file_master_seq", sequenceName = "file_master_seq", allocationSize = 1)
	private int id;

	@Column(name = "year")
	private String year;

	@Column(name = "type")
	private String type;

	@Column(name = "category")
	private String category;

	@Column(name = "level")
	private String level;

	@Column(name = "file_name")
	private String fileName;

	@Temporal(TemporalType.DATE)
	@Column(name = "upload_date")
	private Date uploadDate;

	@Column(name = "is_Deleted")
	private String isDeleted;

	public String getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	// Getters and Setters
}
