package odello;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class LoginView extends JFrame {

	private MainProcess main;
	private Odello odl;
	private odelloDB odlDB;
	private JButton btnLogin;
	private JButton btnInit;
	private JPasswordField passText;
	private JTextField userText;
	private boolean bloginCheck;
	private static int count = 0;

	private static String id1;
	private static String id2;

	public LoginView() {
		setTitle("Login");// 로그인창 만들기
		setSize(380, 150);
		setResizable(false);
		// setLocation(800, 450);
		setLocationRelativeTo(null); // 중앙 위치
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// panel
		JPanel panel = new JPanel();// 패널 만들기
		placeLoginPanel(panel);

		// add
		add(panel);// 패널 추가

		// visible
		setVisible(true);
	}

	public void placeLoginPanel(JPanel panel) {
		panel.setLayout(null);
		JLabel userLabel = new JLabel("USER");// 로그인창 - 유저
		userLabel.setBounds(10, 10, 80, 25);
		panel.add(userLabel);

		JLabel passLabel = new JLabel("PW");// 로그인창 - 비밀번호
		passLabel.setBounds(10, 40, 80, 25);
		panel.add(passLabel);

		userText = new JTextField(20);// 텍스트
		userText.setBounds(100, 10, 160, 25);// 위치와 크기 지정
		panel.add(userText);

		passText = new JPasswordField(20);// 텍스트
		passText.setBounds(100, 40, 160, 25);// 위치와 크기 지정
		panel.add(passText);

		passText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				isLoginCheck();
			}
		});

		// 버튼 생성
		btnInit = new JButton("초기화");// 리셋 버튼 생성
		btnInit.setBounds(10, 80, 100, 25);
		panel.add(btnInit);
		btnInit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				userText.setText("");
				passText.setText("");
			}
		});

		btnLogin = new JButton("로그인");
		btnLogin.setBounds(130, 80, 100, 25);
		panel.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isLoginCheck() == false) {
					JOptionPane.showMessageDialog(null, "로그인 실패!");
					userText.setText("");
					passText.setText("");
				} else {// 로그인 성공했을때
					if (count == 0) {// user1 id 저장
						id1 = userText.getText();
						JOptionPane.showMessageDialog(null, "로그인 성공!");
						count++;
					} else if (count > 0) {// user2 id 저장
						if(!userText.getText().equals(id1)) {
							JOptionPane.showMessageDialog(null, "로그인 성공!");
							id2 = userText.getText();
							odl = new Odello(id1, id2);
							count = 0;
							dispose();
						}else
							JOptionPane.showMessageDialog(null, "아이디가 중복됩니다!");
					}
				}
			}
		});

		btnLogin = new JButton("회원가입");
		btnLogin.setBounds(250, 80, 100, 25);
		panel.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				memberInsert(0, 0);
			}
		});

	}

	// 멤버 확인
	public boolean isLoginCheck() {
		String id = userText.getText(); // 입력한 id값 불러오기
		char[] pw = passText.getPassword(); // 입력한 passwd값 불러오기
		String pwStr = String.valueOf(pw);
		odelloDB db = new odelloDB();
		db.makeConnection(); // db연결
		return db.selectmember(id, pwStr);
	}

	// 멤버 추가
	public boolean memberInsert(int win, int defeat) {
		String id = userText.getText(); // 입력한 id값 불러오기
		char[] pw = passText.getPassword(); // 입력한 passwd값 불러오기
		String pwStr = String.valueOf(pw); // char[] 를 str로 바꾸기.
		odelloDB db = new odelloDB();
		db.makeConnection(); // db연결
		return db.insertmember(id, pwStr, win, defeat); // db에 id,pw 저장하기
	}

	// mainProcess와 연동
	public void setMain(MainProcess main) {
		this.main = main;
	}

}
