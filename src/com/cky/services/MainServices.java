package com.cky.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;

import com.cky.domain.Player;

/**
 * 主要的逻辑业务，需要注意的是：此文件并非Android Service，单纯用于实现业务逻辑。
 * 
 * @author 陈恺垣
 * @version 1.0
 */
public class MainServices {
	// 该ArrayList需要保证Player的grade从小到大且不超过3个
	private static ArrayList<Player> rankings = null;
	// 上下文
	private Context context = null;

	public MainServices(Context context) {
		super();
		this.context = context;
	}

	/**
	 * 读取排行榜信息
	 * 
	 * @param context
	 * @return 如果ranking.dat文件存在，则读取文件并返回存有记录的ArrayList<Player>。如果文件不存在就返回null
	 */
	public ArrayList<Player> loadRanking() {
		// 文件/data/data/com.cky.reactionTest/ranking.dat
		File file = new File(context.getFilesDir(), "ranking.dat");

		// 如果文件存在就读文件
		if (file.exists()) {
			rankings = new ArrayList<Player>();
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));
				// 按行读取文件并实例化对应的Player类，将这些类存储在集合中
				int ranking = 0;
				String name = "";
				while ((name = reader.readLine()) != null) {
					ranking++;
					long grade = Long.parseLong(reader.readLine());
					String dateTime = reader.readLine();
					Player player = new Player(ranking, name, grade, dateTime);
					rankings.add(player);
				}

				return rankings;
			} catch (FileNotFoundException e) {
				// 文件未找到时返回null
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// 文件IO出错时返回null
				e.printStackTrace();
				return null;
			} finally {
				// 关闭IO流
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				reader = null;
			}
		} else {
			// 如果文件不存在就返回null
			return rankings;
		}

	}

	/**
	 * 写排行榜信息到文件中
	 * 
	 * @param ranking
	 *            排行榜信息
	 * @return 如果写入成功返回true失败放那会false
	 */
	public boolean writeRanking(ArrayList<Player> rankings) {
		// 文件/data/data/com.cky.reactionTest/ranking.dat
		File file = new File(context.getFilesDir(), "ranking.dat");

		// 如果文件存在就删除文件
		if (file.exists()) {
			file.delete();
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			// 将每个Player对象的姓名，反应时间，产生的时间日期按行写入到文件ranking.dat中
			for (int i = 0; i < rankings.size(); i++) {
				Player player = rankings.get(i);
				writer.write(player.getName() + "\n");
				writer.write(player.getGrade() + "\n");
				writer.write(player.getDateTime() + "\n");
			}

			writer.flush();
			return true;
		} catch (FileNotFoundException e) {
			// 文件未找到时返回null
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// 文件IO出错时返回null
			e.printStackTrace();
			return false;
		} finally {
			// 关闭IO流
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer = null;
		}
	}

	/**
	 * 将传入的时间与排行榜中的记录进行比较。如果比某条记录要快，则返回true。如果比任意一条记录都慢则返回false。
	 * 
	 * @param time
	 *            待比较的时间
	 * @return 如果比某条记录要快，则返回true。如果比任意一条记录都慢则返回false。
	 */
	public boolean betterResult(long time) {
		// 集合存在即进行比较
		if (rankings != null) {
			// 集合小于3时，产生的合法记录都是需要添加到集合中
			if (rankings.size() < 3) {
				return true;
			} else {
				// 集合中有比该反应时间长的就返回true
				for (Player i : rankings) {
					if (i.getGrade() > time) {

						return true;
					}
				}
				// 集合没中有比该反应时间长的就返回false
				return false;
			}
		} else {
			// 集合不存在直接返回 true
			return true;
		}
	}

	/**
	 * 返回排行榜信息
	 * 
	 * @return 排行榜信息
	 */
	public ArrayList<Player> getRankings() {
		return rankings;
	}

	/**
	 * 向集合中添加Player，此方法保证集合中的Player的反应时间与下标同按升序排列
	 * 
	 * @param player
	 *            添加的Player
	 */
	public void addPlayer(Player player) {
		// 如果集合存在就向现有集合添加
		if (rankings != null) {
			rankings.add(player);
			// 对集合中的Player按照反应时间升序排列
			for (int i = 0; i < rankings.size(); i++) {
				for (int j = i + 1; j < rankings.size(); j++) {
					if (rankings.get(i).getGrade() > rankings.get(j).getGrade()) {
						Player swap = rankings.get(i);
						rankings.set(i, rankings.get(j));
						rankings.set(j, swap);
					}
				}
				rankings.get(i).setRanking(i + 1);
			}
			// 删除下标大于2的元素，保证集合中最多只有三个元素
			for (int i = rankings.size() - 1; i >= 3; i--) {
				rankings.remove(i);
			}
		} else {
			// 如果集合不存在就实例化一个，并添加
			rankings = new ArrayList<Player>();
			rankings.add(player);
		}
		// 将新的集合保存到文件
		writeRanking(rankings);
	}
}
