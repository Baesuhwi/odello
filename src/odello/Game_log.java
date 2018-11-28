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
		setTitle("���� ���");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(550, 250);
		setLocationRelativeTo(null); // �߾� ��ġ
		
		odlDB = new odelloDB();
		list = odlDB.rank();
		
		Vector<String> column = new Vector<>();//�� �̸� ����
		column.add("ranking");
		column.add("id");
		column.add("win");
		column.add("defeat");
		column.add("date");
		
		Vector<Object> dataSet = new Vector<>();//���� �����͸� ����
		for(DBData d : list) {
			Vector row = new Vector();
			row.add(d.getRank() + "��");
			row.add(d.getId() + "��");
			row.add(d.getWin() + "��");
			row.add(d.getDefeat() + "��");
			row.add(d.getTime());
			dataSet.add(row);
		}
		
		table = new JTable(dataSet,column);
		JScrollPane scrollList = new JScrollPane(table);
		add(scrollList);
		
		
		setVisible(true);
	}
	

}
