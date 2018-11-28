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
		setTitle("Login");// �α���â �����
		setSize(380, 150);
		setResizable(false);
		// setLocation(800, 450);
		setLocationRelativeTo(null); // �߾� ��ġ
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// panel
		JPanel panel = new JPanel();// �г� �����
		placeLoginPanel(panel);

		// add
		add(panel);// �г� �߰�

		// visible
		setVisible(true);
	}

	public void placeLoginPanel(JPanel panel) {
		panel.setLayout(null);
		JLabel userLabel = new JLabel("USER");// �α���â - ����
		userLabel.setBounds(10, 10, 80, 25);
		panel.add(userLabel);

		JLabel passLabel = new JLabel("PW");// �α���â - ��й�ȣ
		passLabel.setBounds(10, 40, 80, 25);
		panel.add(passLabel);

		userText = new JTextField(20);// �ؽ�Ʈ
		userText.setBounds(100, 10, 160, 25);// ��ġ�� ũ�� ����
		panel.add(userText);

		passText = new JPasswordField(20);// �ؽ�Ʈ
		passText.setBounds(100, 40, 160, 25);// ��ġ�� ũ�� ����
		panel.add(passText);

		passText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				isLoginCheck();
			}
		});

		// ��ư ����
		btnInit = new JButton("�ʱ�ȭ");// ���� ��ư ����
		btnInit.setBounds(10, 80, 100, 25);
		panel.add(btnInit);
		btnInit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				userText.setText("");
				passText.setText("");
			}
		});

		btnLogin = new JButton("�α���");
		btnLogin.setBounds(130, 80, 100, 25);
		panel.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isLoginCheck() == false) {
					JOptionPane.showMessageDialog(null, "�α��� ����!");
					userText.setText("");
					passText.setText("");
				} else {// �α��� ����������
					if (count == 0) {// user1 id ����
						id1 = userText.getText();
						JOptionPane.showMessageDialog(null, "�α��� ����!");
						count++;
					} else if (count > 0) {// user2 id ����
						if(!userText.getText().equals(id1)) {
							JOptionPane.showMessageDialog(null, "�α��� ����!");
							id2 = userText.getText();
							odl = new Odello(id1, id2);
							count = 0;
							dispose();
						}else
							JOptionPane.showMessageDialog(null, "���̵� �ߺ��˴ϴ�!");
					}
				}
			}
		});

		btnLogin = new JButton("ȸ������");
		btnLogin.setBounds(250, 80, 100, 25);
		panel.add(btnLogin);
		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				memberInsert(0, 0);
			}
		});

	}

	// ��� Ȯ��
	public boolean isLoginCheck() {
		String id = userText.getText(); // �Է��� id�� �ҷ�����
		char[] pw = passText.getPassword(); // �Է��� passwd�� �ҷ�����
		String pwStr = String.valueOf(pw);
		odelloDB db = new odelloDB();
		db.makeConnection(); // db����
		return db.selectmember(id, pwStr);
	}

	// ��� �߰�
	public boolean memberInsert(int win, int defeat) {
		String id = userText.getText(); // �Է��� id�� �ҷ�����
		char[] pw = passText.getPassword(); // �Է��� passwd�� �ҷ�����
		String pwStr = String.valueOf(pw); // char[] �� str�� �ٲٱ�.
		odelloDB db = new odelloDB();
		db.makeConnection(); // db����
		return db.insertmember(id, pwStr, win, defeat); // db�� id,pw �����ϱ�
	}

	// mainProcess�� ����
	public void setMain(MainProcess main) {
		this.main = main;
	}

}
