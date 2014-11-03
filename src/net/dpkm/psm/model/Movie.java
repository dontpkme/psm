package net.dpkm.psm.model;

public class Movie {

	private String name;

	private String image;

	private String url;

	private int type;

	private int id;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getId() {
		System.out.println(Integer.parseInt(this.getUrl().split("=")[1]));
		return Integer.parseInt(this.getUrl().split("=")[1]);
	}

	public void setId(int id) {
		this.id = id;
	}
}