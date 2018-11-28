package odello;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Odello extends JFrame implements ActionListener {
	private MainProcess main;
	int turn = 0; // 턴 저장 변수
	int tablecnt = 0; // 테이블위에 있는 돌 개수 카운트 변수
	int aim_i, aim_j; // 클릭지점을 중심으로 계산할 목표 방향 변수
	int set_i = 0, set_j = 0; // 계산할 목표 좌표 변수
	int usr1cnt = 0, usr2cnt = 0; // 검은돌,하얀돌 의 개수 카운트 변수
	static int newGame = 0; // 메세지박스의 리턴 값 저장 변수
	boolean chkNothing = false; // 둘 곳이 있는지 없는지 확인 변수
	int table[][] = new int[8][8]; // 테이블 2차원 배열
	
	JMenuBar mb; // 메뉴바
	JMenu mnu;
	JMenuItem mnunew;
	JMenuItem mnuend;
	ImageIcon white = new ImageIcon("white.jpg"); // 하얀돌 이미지
	ImageIcon black = new ImageIcon("black.jpg"); // 검은돌 이미지
	ImageIcon bg = new ImageIcon("bg.jpg"); // 백그라운드 이미지
	JButton jb[][] = new JButton[8][8]; // 실제로 보여질 버튼

	private String id1;
	private String id2;

	Game_log gamelog;
	LoginView loginview;
	odelloDB odlDB;

	int wincnt = 0;// 승리 카운트
	int defeatcnt = 0;// 패배 카운트
	int wincnt2 = 0;// 승리 카운트
	int defeatcnt2 = 0;// 패배 카운트

	public Odello(String id1, String id2) {
		this.id1 = id1;
		this.id2 = id2;

		setTitle("Game");
		setSize(380, 150);
		setResizable(false);
		setLocationRelativeTo(null); // 중앙 위치
		setLocation(600, 200); // 중앙 위치
		mb = new JMenuBar(); // 메뉴바 생성과 메뉴아이템 엮기,
		mnu = new JMenu("선택"); // 마우스액션 이벤트 등록
		mnunew = new JMenuItem("새게임");// 메뉴 새게임
		mnuend = new JMenuItem("끝내기");// 메뉴 끝내기
		mnu.add(mnunew);
		mnu.add(mnuend);
		mb.add(mnu);
		setJMenuBar(mb);
		mnunew.addActionListener(this);
		mnuend.addActionListener(this);
		Container ct = getContentPane(); // Container 생성
		ct.setLayout(new GridLayout(8, 8)); // 9*8 GridLayout생성

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				jb[i][j] = new JButton(bg); // 버튼배열에 버튼 생성 bg아이콘삽입
				ct.add(jb[i][j]); // 칸 마다 배경 삽입
				jb[i][j].addActionListener(this); // 버튼 클릭 이벤트 등록
				jb[i][j].setLocation(i, j);
			}
		}

		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				table[i][j] = 9; // 계산 테이블 초기화
			}

		jb[3][3].setIcon(black); // 보여지는 테이블 초기화
		jb[4][4].setIcon(black);
		jb[3][4].setIcon(white);
		jb[4][3].setIcon(white);

		table[3][3] = 0;
		table[4][4] = 0;
		table[3][4] = 1;
		table[4][3] = 1;

		tablecnt = 4; // 카운트 증가\
		this.setSize(600, 680);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == mnunew) { // 새게임 버튼 클릭시 발생
			Odello jbt = new Odello(id1, id2);
			dispose(); // 메뉴바에서 새게임 클릭시 프레임만 종료
			jbt.setTitle("오델로");
			jbt.setSize(640, 640);
			jbt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jbt.setVisible(true);
		} else if (ae.getSource() == mnuend) { // 종료 버튼 클릭시 프로그램 종료
			dispose();
			gamelog = new Game_log(id1, id2);
		} else {
			int x = 0, y = 0, value;

			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 8; j++) {
					if (ae.getSource() == jb[i][j]) { // 누른 버튼의 좌표 얻기
						x = i;
						y = j;
					}
				}

			if (table[x][y] == 9) { // 빈 칸일 경우
				if (turn == 0)
					value = 1;
				else
					value = 0;
				if (chkReverse(x, y, value)) { // 주위 8칸에 자신 반댓돌 찾고 변환
					tablecnt++; // 성공시 카운트 증가
					dpTable(); // table[x][y] 테이블 display

					if (turn == 0) // 턴 바꾸기
						turn = 1;
					else if (turn == 1)
						turn = 0;

					if (tablecnt == 64) { // 게임 종료 확인
						if (chkNum() == "draw")
							newGame = JOptionPane.showConfirmDialog(null, "검은돌 = " + usr1cnt / 2 + // 메세지 박스
									", 하얀돌 = " + usr2cnt / 2 + ", 비겼습니다  \n " + "새로운 게임을 하시겠습니까?", "확인",
									JOptionPane.YES_NO_OPTION);
						else {
							if (chkNum().equals("검은돌")) {
								odlDB = new odelloDB();
								odlDB.winlog(id1);
								odlDB.defeatlog(id2);
							} else {
								odlDB = new odelloDB();
								odlDB.winlog(id2);
								odlDB.defeatlog(id1);
							}
							newGame = JOptionPane
									.showConfirmDialog(null,
											"검은돌 = " + usr1cnt / 2 + ", 하얀돌 = " + usr2cnt / 2 + ", " + chkNum()
													+ "이 이겼습니다 \n" + "새로운 게임을 하시겠습니까?",
											"확인", JOptionPane.YES_NO_OPTION);
						}
						if (newGame == JOptionPane.NO_OPTION) { // NO버튼 클릭시
							dispose();
							gamelog = new Game_log(id1, id2);
						} else if (newGame == JOptionPane.YES_OPTION) { // YES버튼클릭시

							Odello jbt = new Odello(id1, id2); // 다시 시작
							dispose();
							jbt.setTitle("오델로");
							jbt.setSize(640, 640);
							jbt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							jbt.setVisible(true);

						}
					}
					chkNothing(); // 둘곳이 있는지 확인 하는 함수;
					chkNothing = false;
				}
			}
		}
	}

	public void dpTable() { // GUI 결과 창에 뿌려주기
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (table[i][j] == 0)
					jb[i][j].setIcon(black);
				else if (table[i][j] == 1)
					jb[i][j].setIcon(white);
				else
					jb[i][j].setIcon(bg);
			}
		}
	}

	public int reverse(int x, int y) {
		int i, j;

		for (i = 0; table[set_i][set_j] != turn; i++) { // 자신과 반대돌일 경우
			set_i = set_i + aim_i; // 목표 방향의 일직선으로 다음 목표 칸 set
			set_j = set_j + aim_j;

			if (set_i < 0 || set_i > 7) // 테이블 벗어나는경우
				return 1;
			if (set_j < 0 || set_j > 7)
				return 1;
			if (table[set_i][set_j] == 9) // 빈 공간일 경우
				return 1;
		}
		for (j = 0; j < i; j++) { // 지나온 길을 거꾸로 가며 자신의 돌로 채운다
			set_i = set_i - aim_i;
			set_j = set_j - aim_j;
			if (!chkNothing) // chkNothing 함수 실행시는 실행하지 않는다
				table[set_i][set_j] = turn;
		}
		if (!chkNothing)
			table[x][y] = turn; // 마지막 처음 클릭한 지점에 자신의 돌로 채운다.
		return 0; // 정상종료 리턴
	}

	public boolean chkReverse(int x, int y, int dol) {
		int i, j;
		int lmt_i, lmt_j;
		boolean stat = false; // 성공시 true 반환

		for (i = -1; i <= 1; i++) {
			for (j = -1; j <= 1; j++) {
				lmt_i = x + i;
				lmt_j = y + j;

				if (lmt_i < 0 || lmt_i > 7)
					continue;
				if (lmt_j < 0 || lmt_j > 7)
					continue;
				if (i == 0 && j == 0)
					continue;
				if (table[lmt_i][lmt_j] == dol) { // 반대돌 일 경우
					set_i = lmt_i;
					set_j = lmt_j;
					aim_i = i;
					aim_j = j;
					if (reverse(x, y) == 0) // reverse함수 호출
						stat = true; // 성공시 상태 true
				} else
					continue;
			}
		}
		return stat; // 바꿀돌이 없을 경우 stat = false
	}

	public String chkNum() { // 돌 별 카운트
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (table[i][j] == 0)
					usr1cnt++;
				else if (table[i][j] == 1)
					usr2cnt++;
			}
		}
		if (usr1cnt == usr2cnt)
			return "draw";
		else
			return usr1cnt > usr2cnt ? "검은돌" : "하얀돌";
	}

	public void chkNothing() { // 둘 곳이 있는지 확인 하는 함수
		chkNothing = true;
		int possible = 0;
		int chk_value = 0;

		if (turn == 0)
			chk_value = 1;
		else
			chk_value = 0;

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (table[i][j] == 9) {
					if (chkReverse(i, j, chk_value))
						possible++;
				}
			}
		}
		if (possible == 0) { // 둘 곳이 없을 경우 재차 턴 넘기기
			if (turn == 0)
				turn = 1;
			else
				turn = 0;
		}
	}
//	public static void main(String[] args) throws IOException {
//
//		JFrame.setDefaultLookAndFeelDecorated(true); // 스윙만의 윈도우창 표현
//		Odello jbt = new Odello();
//		jbt.setTitle("오델로");
//		jbt.setSize(640, 640);
//		jbt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		jbt.setVisible(true);
//	}
}