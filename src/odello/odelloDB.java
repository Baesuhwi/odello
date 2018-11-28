package odello;

import java.sql.*;
import java.util.*;

import javax.swing.JOptionPane;

public class odelloDB {
	Odello odello;
	Connection conn;
	Statement stmt;
	ResultSet rs;

	public int win, defeat;
	public String id;
	
	LoginView loginview;

	public static Connection makeConnection() {
		String url = "jdbc:oracle:thin:@net.yjc.ac.kr:1521:orcl";
		String id = "s1501137";
		String password = "p1501137";
		Connection con = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection(url, id, password);
			System.out.println("����̹� ���� ����.");
		} catch (ClassNotFoundException e) {
			// e.printStackTrace(); ������ ��Ÿ���ִ� �κ����� �����ð����� �ʿ���� �ּ�
			System.out.println("����̹��� ã�� �� �����ϴ�.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}

//	public ArrayList<>
	
	//�α��� ó�� �κ�
	public boolean selectmember(String id, String pw) {
		// DBCP�� �̿��� �������id�� �̿��ؼ� �˻�
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
//			String str = String.valueOf(pw);
			conn = makeConnection();
			String sql = "select * from game where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
//			this.stmt = this.conn.createStatement();

			if (rs.next()) {
				String dbPassword = rs.getString("pw");
				if (dbPassword.equals(pw)) {
					return true;// �α��� ����
				} else {
					JOptionPane.showMessageDialog(null, "��� Ʋ��");
					return false;// ��й�ȣ ����
				}
			} else {
				JOptionPane.showMessageDialog(null, "���̵� ����!");
				return false;// ���̵� ����
			}
			
		} catch (SQLException e) {
			System.out.println("Statement��ü ���� �ȉ�" + e);
		}
		return false;

	}
	
	
	
	// ȸ������ ó�� �κ�
	public boolean insertmember(String id, String passwd,
			int win, int defeat) {
		String pw = passwd;

		Timestamp time = new Timestamp(System.currentTimeMillis());

		int result = 0;
		// DBCP�� �̿��� �������id�� �̿��ؼ� �˻�
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = makeConnection();
			// sql������ �ۼ�

			String sql = "insert into game values(?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setInt(3, win);
			pstmt.setInt(4, defeat);
			pstmt.setTimestamp(5, time);

			// System.out.println(sql); // Ȯ�ο�
			result = pstmt.executeUpdate();
			JOptionPane.showMessageDialog(null, "ȸ������ ����!");
			conn.close(); // ���� ����
		} catch (SQLException e) {
			System.out.println("Statement��ü ������ ������ �ֽ��ϴ� 5: " + e);
			JOptionPane.showMessageDialog(null, "���̵� �ߺ��˴ϴ�!");
		}
		return false;
	}
	
	//���� �̰����� ó�� �޼ҵ�
	public boolean winlog(String id) {
		int result = 0;
		// DBCP�� �̿��� �������id�� �̿��ؼ� �˻�
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = makeConnection();
			String sql = "update game set win = "
					+ "(select win+1 from game where id = ?) where id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, id);
			// System.out.println(sql); // Ȯ�ο�
			result = pstmt.executeUpdate();
			conn.close(); // ���� ����
		} catch (SQLException e) {
			System.out.println("Statement��ü ������ ������ �ֽ��ϴ� 55: " + e);
		}
		return false;
	}
	
	//���� ������ ó�� �޼ҵ�
		public boolean defeatlog(String id) {
			int result = 0;
			// DBCP�� �̿��� �������id�� �̿��ؼ� �˻�
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				conn = makeConnection();
				String sql = "update game set defeat = "
						+ "(select defeat+1 from game where id = ?) where id = ?";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);
				pstmt.setString(2, id);
				// System.out.println(sql); // Ȯ�ο�
				result = pstmt.executeUpdate();
				conn.close(); // ���� ����
			} catch (SQLException e) {
				System.out.println("Statement��ü ������ ������ �ֽ��ϴ� 55: " + e);
			}
			return false;
		}
		
		//���� �������� ó�� �κ�
		public ArrayList<DBData> selectLog(String id1, String id2){
			ArrayList<DBData> list = null;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			System.out.println("1");
			try {
				conn = makeConnection();
				System.out.println("2");
				list = new ArrayList<>();
				String sql = "select * from game where id=? or id=?";
				System.out.println("3");
				pstmt = conn.prepareStatement(sql);
				System.out.println("4");
				pstmt.setString(1, id1);
				System.out.println("5");
				pstmt.setString(2, id2);
				rs = pstmt.executeQuery();
				System.out.println("6");
				
				while(rs.next()) {
					DBData d = new DBData();
					d.setId(rs.getString("id"));
					d.setWin(rs.getInt("win"));
					d.setDefeat(rs.getInt("defeat"));
					d.setTime(rs.getTimestamp("reg_date"));
					list.add(d);
				}
			}catch(Exception e) {
				System.out.println("sql���� ������ �ֽ��ϴ�.");
			}
			return list;
		}
		
		public ArrayList<DBData> rank(){
			ArrayList<DBData> list = null;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				conn = makeConnection();
				list = new ArrayList<>();
				String sql = "select rownum as rank, id, win, defeat, reg_date from (select id,win,defeat,reg_date from game order by win desc) where rownum <= 5";
				pstmt = conn.prepareStatement(sql);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					DBData d = new DBData();
					d.setRank(rs.getInt("rank"));
					d.setId(rs.getString("id"));
					d.setWin(rs.getInt("win"));
					d.setDefeat(rs.getInt("defeat"));
					d.setTime(rs.getTimestamp("reg_date"));
					list.add(d);
				}
			}catch(Exception e) {
				System.out.println("sql���� ������ �ֽ��ϴ�." + e.getMessage());
			}
			return list;
		}
}
