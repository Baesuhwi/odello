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
			System.out.println("드라이버 적재 성공.");
		} catch (ClassNotFoundException e) {
			// e.printStackTrace(); 오류를 나타내주는 부분으로 수업시간에는 필요없어 주석
			System.out.println("드라이버를 찾을 수 없습니다.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}

//	public ArrayList<>
	
	//로그인 처리 부분
	public boolean selectmember(String id, String pw) {
		// DBCP를 이용해 사용자의id를 이용해서 검색
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
					return true;// 로그인 성공
				} else {
					JOptionPane.showMessageDialog(null, "비번 틀림");
					return false;// 비밀번호 오류
				}
			} else {
				JOptionPane.showMessageDialog(null, "아이디 없음!");
				return false;// 아이디 없음
			}
			
		} catch (SQLException e) {
			System.out.println("Statement객체 생성 안됌" + e);
		}
		return false;

	}
	
	
	
	// 회원가입 처리 부분
	public boolean insertmember(String id, String passwd,
			int win, int defeat) {
		String pw = passwd;

		Timestamp time = new Timestamp(System.currentTimeMillis());

		int result = 0;
		// DBCP를 이용해 사용자의id를 이용해서 검색
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = makeConnection();
			// sql문장을 작성

			String sql = "insert into game values(?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			pstmt.setInt(3, win);
			pstmt.setInt(4, defeat);
			pstmt.setTimestamp(5, time);

			// System.out.println(sql); // 확인용
			result = pstmt.executeUpdate();
			JOptionPane.showMessageDialog(null, "회원가입 성공!");
			conn.close(); // 연결 종료
		} catch (SQLException e) {
			System.out.println("Statement객체 생성에 문제가 있습니다 5: " + e);
			JOptionPane.showMessageDialog(null, "아이디가 중복됩니다!");
		}
		return false;
	}
	
	//누가 이겼을때 처리 메소드
	public boolean winlog(String id) {
		int result = 0;
		// DBCP를 이용해 사용자의id를 이용해서 검색
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
			// System.out.println(sql); // 확인용
			result = pstmt.executeUpdate();
			conn.close(); // 연결 종료
		} catch (SQLException e) {
			System.out.println("Statement객체 생성에 문제가 있습니다 55: " + e);
		}
		return false;
	}
	
	//누가 졌을때 처리 메소드
		public boolean defeatlog(String id) {
			int result = 0;
			// DBCP를 이용해 사용자의id를 이용해서 검색
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
				// System.out.println(sql); // 확인용
				result = pstmt.executeUpdate();
				conn.close(); // 연결 종료
			} catch (SQLException e) {
				System.out.println("Statement객체 생성에 문제가 있습니다 55: " + e);
			}
			return false;
		}
		
		//게임 끝났을때 처리 부분
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
				System.out.println("sql문에 문제가 있습니다.");
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
				System.out.println("sql문에 문제가 있습니다." + e.getMessage());
			}
			return list;
		}
}
