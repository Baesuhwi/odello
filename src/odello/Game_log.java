package odello;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

public class Game_log extends JFrame {
	String id1;
	String id2;
	odelloDB odlDB;
	ArrayList<DBData> list;
	JTable table;
	
	public Game_log(String id1, String id2) {
		setTitle("게임 결과");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(550, 250);
		setLocationRelativeTo(null); // 중앙 위치
		
		odlDB = new odelloDB();
		list = odlDB.rank();
		
		Vector<String> column = new Vector<>();//열 이름 생성
		column.add("ranking");
		column.add("id");
		column.add("win");
		column.add("defeat");
		column.add("date");
		
		Vector<Object> dataSet = new Vector<>();//행의 데이터를 저장
		for(DBData d : list) {
			Vector row = new Vector();
			row.add(d.getRank() + "등");
			row.add(d.getId() + "님");
			row.add(d.getWin() + "승");
			row.add(d.getDefeat() + "패");
			row.add(d.getTime());
			dataSet.add(row);
		}
		
		table = new JTable(dataSet,column);
		JScrollPane scrollList = new JScrollPane(table);
		add(scrollList);
		
		
		setVisible(true);
	}
	

}
