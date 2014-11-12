package net.dpkm.psm.model;

public class Article {

	/**
	 * 是否被M文
	 */
	private boolean marked;

	/**
	 * 文章標題
	 */
	private String title;

	/**
	 * 作者ID
	 */
	private String author;

	/**
	 * 發文日期
	 */
	private String date;

	/**
	 * 推文數
	 */
	private String nrec;

	/**
	 * 網址
	 */
	private String link;

	/**
	 * 分數權重
	 */
	private Float weight;

	public Article(String title, String author, String date, boolean marked,
			String nrec, String link) {
		this.title = title;
		this.author = author;
		this.date = date;
		this.marked = marked;
		this.nrec = nrec;
		this.link = link;
		this.weight = calculateWeightByTitle(title, nrec);
	}

	private Float calculateWeightByTitle(String title, String nrec) {
		title = title.trim();
		Float weight = null;
		if (title.startsWith("[") && title.indexOf("]") != -1) {
			String label = title.substring(1, title.indexOf("]")).trim();
			// System.out.println(label);

			if (nrec.indexOf("X") != -1)
				return null;
			if (label.indexOf("普雷") != -1 || label.indexOf("普無雷") != -1
					|| label.indexOf("普有雷") != -1 || label.indexOf("普微雷") != -1)
				return 0f;
			else if (label.indexOf("負雷") != -1 || label.indexOf("負無雷") != -1
					|| label.indexOf("負微雷") != -1 || label.indexOf("負有雷") != -1
					|| label.indexOf("不好雷") != -1 || label.indexOf("惡雷") != -1
					|| label.indexOf("爛雷") != -1)
				weight = -1f;
			else if (label.indexOf("好雷") != -1 || label.indexOf("好無雷") != -1
					|| label.indexOf("好微雷") != -1 || label.indexOf("好有雷") != -1)
				weight = 1f;

			if (weight != null) {
				if (label.indexOf("超") != -1 || label.indexOf("極") != -1
						|| label.indexOf("神") != -1 || label.indexOf("大") != -1
						|| label.indexOf("特") != -1 || label.indexOf("讚") != -1
						|| label.indexOf("很") != -1 || label.indexOf("爆") != -1
						|| label.indexOf("巨") != -1)
					weight *= 1.5f;

				if ((label.indexOf("微") != -1 && label.indexOf("微雷") == -1)
						|| label.indexOf("普") != -1)
					weight *= 0.5f;
			}
		}

		return weight;
	}

	public void setWeight(Float weight) {
		this.weight = weight;
	}

	public boolean isMarked() {
		return marked;
	}

	public void setMarked(boolean marked) {
		this.marked = marked;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNrec() {
		return nrec;
	}

	public void setNrec(String nrec) {
		this.nrec = nrec;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Float getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return "title: " + title + "\n" + "author: " + author + "\n" + "date: "
				+ date + "\n" + "marked: " + marked + "\n" + "nrec: " + nrec
				+ "\n" + "link: " + link + "\n" + "weight: " + weight;
	}
}