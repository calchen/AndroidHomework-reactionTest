package com.cky.domain;

/**
 * 玩家类，记录玩家的相关信息。
 * 
 * @author 陈恺垣
 * @version 1.0
 */
public class Player {
	/**
	 * 排名
	 */
	private int ranking;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 反应时间
	 */
	private long grade;
	/**
	 * 记录日期时间
	 */
	private String dateTime;

	/**
	 * Player 的构造函数
	 * 
	 * @param ranking
	 *            排名
	 * @param name
	 *            姓名
	 * @param grade
	 *            反应时间
	 * @param dateTime
	 *            产生的时间日期
	 */
	public Player(int ranking, String name, long grade, String dateTime) {
		this.ranking = ranking;
		this.name = name;
		this.grade = grade;
		this.dateTime = dateTime;
	}

	/**
	 * Player 的构造函数
	 * 
	 * @param name
	 *            姓名
	 * @param grade
	 *            反应时间
	 * @param dateTime
	 *            产生的时间日期
	 */
	public Player(String name, long grade, String dateTime) {
		super();
		this.name = name;
		this.grade = grade;
		this.dateTime = dateTime;
	}

	/**
	 * 获取排名
	 * 
	 * @return 排名
	 */
	public int getRanking() {
		return ranking;
	}

	/**
	 * 设置排名
	 * 
	 * @param ranking
	 *            排名
	 */
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}

	/**
	 * 获取姓名
	 * 
	 * @return 姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置姓名
	 * 
	 * @param name
	 *            姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取反应时间
	 * 
	 * @return 反应时间
	 */
	public long getGrade() {
		return grade;
	}

	/**
	 * 设置反应时间
	 * 
	 * @param grade
	 *            反应时间
	 */
	public void setGrade(long grade) {
		this.grade = grade;
	}

	/**
	 * 获取该用户产生的时间日期
	 * 
	 * @return 时间题日期
	 */
	public String getDateTime() {
		return dateTime;
	}

	/**
	 * 设置该用户产生的时间日期
	 * 
	 * @param dateTime
	 *            时间日期
	 */
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
}
