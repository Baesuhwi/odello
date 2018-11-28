package odello;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Odello extends JFrame implements ActionListener {
	private MainProcess main;
	int turn = 0; // �� ���� ����
	int tablecnt = 0; // ���̺����� �ִ� �� ���� ī��Ʈ ����
	int aim_i, aim_j; // Ŭ�������� �߽����� ����� ��ǥ ���� ����
	int set_i = 0, set_j = 0; // ����� ��ǥ ��ǥ ����
	int usr1cnt = 0, usr2cnt = 0; // ������,�Ͼᵹ �� ���� ī��Ʈ ����
	static int newGame = 0; // �޼����ڽ��� ���� �� ���� ����
	boolean chkNothing = false; // �� ���� �ִ��� ������ Ȯ�� ����
	int table[][] = new int[8][8]; // ���̺� 2���� �迭
	
	JMenuBar mb; // �޴���
	JMenu mnu;
	JMenuItem mnunew;
	JMenuItem mnuend;
	ImageIcon white = new ImageIcon("white.jpg"); // �Ͼᵹ �̹���
	ImageIcon black = new ImageIcon("black.jpg"); // ������ �̹���
	ImageIcon bg = new ImageIcon("bg.jpg"); // ��׶��� �̹���
	JButton jb[][] = new JButton[8][8]; // ������ ������ ��ư

	private String id1;
	private String id2;

	Game_log gamelog;
	LoginView loginview;
	odelloDB odlDB;

	int wincnt = 0;// �¸� ī��Ʈ
	int defeatcnt = 0;// �й� ī��Ʈ
	int wincnt2 = 0;// �¸� ī��Ʈ
	int defeatcnt2 = 0;// �й� ī��Ʈ

	public Odello(String id1, String id2) {
		this.id1 = id1;
		this.id2 = id2;

		setTitle("Game");
		setSize(380, 150);
		setResizable(false);
		setLocationRelativeTo(null); // �߾� ��ġ
		setLocation(600, 200); // �߾� ��ġ
		mb = new JMenuBar(); // �޴��� ������ �޴������� ����,
		mnu = new JMenu("����"); // ���콺�׼� �̺�Ʈ ���
		mnunew = new JMenuItem("������");// �޴� ������
		mnuend = new JMenuItem("������");// �޴� ������
		mnu.add(mnunew);
		mnu.add(mnuend);
		mb.add(mnu);
		setJMenuBar(mb);
		mnunew.addActionListener(this);
		mnuend.addActionListener(this);
		Container ct = getContentPane(); // Container ����
		ct.setLayout(new GridLayout(8, 8)); // 9*8 GridLayout����

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				jb[i][j] = new JButton(bg); // ��ư�迭�� ��ư ���� bg�����ܻ���
				ct.add(jb[i][j]); // ĭ ���� ��� ����
				jb[i][j].addActionListener(this); // ��ư Ŭ�� �̺�Ʈ ���
				jb[i][j].setLocation(i, j);
			}
		}

		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				table[i][j] = 9; // ��� ���̺� �ʱ�ȭ
			}

		jb[3][3].setIcon(black); // �������� ���̺� �ʱ�ȭ
		jb[4][4].setIcon(black);
		jb[3][4].setIcon(white);
		jb[4][3].setIcon(white);

		table[3][3] = 0;
		table[4][4] = 0;
		table[3][4] = 1;
		table[4][3] = 1;

		tablecnt = 4; // ī��Ʈ ����\
		this.setSize(600, 680);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == mnunew) { // ������ ��ư Ŭ���� �߻�
			Odello jbt = new Odello(id1, id2);
			dispose(); // �޴��ٿ��� ������ Ŭ���� �����Ӹ� ����
			jbt.setTitle("������");
			jbt.setSize(640, 640);
			jbt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jbt.setVisible(true);
		} else if (ae.getSource() == mnuend) { // ���� ��ư Ŭ���� ���α׷� ����
			dispose();
			gamelog = new Game_log(id1, id2);
		} else {
			int x = 0, y = 0, value;

			for (int i = 0; i < 8; i++)
				for (int j = 0; j < 8; j++) {
					if (ae.getSource() == jb[i][j]) { // ���� ��ư�� ��ǥ ���
						x = i;
						y = j;
					}
				}

			if (table[x][y] == 9) { // �� ĭ�� ���
				if (turn == 0)
					value = 1;
				else
					value = 0;
				if (chkReverse(x, y, value)) { // ���� 8ĭ�� �ڽ� �ݴ� ã�� ��ȯ
					tablecnt++; // ������ ī��Ʈ ����
					dpTable(); // table[x][y] ���̺� display

					if (turn == 0) // �� �ٲٱ�
						turn = 1;
					else if (turn == 1)
						turn = 0;

					if (tablecnt == 64) { // ���� ���� Ȯ��
						if (chkNum() == "draw")
							newGame = JOptionPane.showConfirmDialog(null, "������ = " + usr1cnt / 2 + // �޼��� �ڽ�
									", �Ͼᵹ = " + usr2cnt / 2 + ", �����ϴ�  \n " + "���ο� ������ �Ͻðڽ��ϱ�?", "Ȯ��",
									JOptionPane.YES_NO_OPTION);
						else {
							if (chkNum().equals("������")) {
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
											"������ = " + usr1cnt / 2 + ", �Ͼᵹ = " + usr2cnt / 2 + ", " + chkNum()
													+ "�� �̰���ϴ� \n" + "���ο� ������ �Ͻðڽ��ϱ�?",
											"Ȯ��", JOptionPane.YES_NO_OPTION);
						}
						if (newGame == JOptionPane.NO_OPTION) { // NO��ư Ŭ����
							dispose();
							gamelog = new Game_log(id1, id2);
						} else if (newGame == JOptionPane.YES_OPTION) { // YES��ưŬ����

							Odello jbt = new Odello(id1, id2); // �ٽ� ����
							dispose();
							jbt.setTitle("������");
							jbt.setSize(640, 640);
							jbt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							jbt.setVisible(true);

						}
					}
					chkNothing(); // �Ѱ��� �ִ��� Ȯ�� �ϴ� �Լ�;
					chkNothing = false;
				}
			}
		}
	}

	public void dpTable() { // GUI ��� â�� �ѷ��ֱ�
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

		for (i = 0; table[set_i][set_j] != turn; i++) { // �ڽŰ� �ݴ뵹�� ���
			set_i = set_i + aim_i; // ��ǥ ������ ���������� ���� ��ǥ ĭ set
			set_j = set_j + aim_j;

			if (set_i < 0 || set_i > 7) // ���̺� ����°��
				return 1;
			if (set_j < 0 || set_j > 7)
				return 1;
			if (table[set_i][set_j] == 9) // �� ������ ���
				return 1;
		}
		for (j = 0; j < i; j++) { // ������ ���� �Ųٷ� ���� �ڽ��� ���� ä���
			set_i = set_i - aim_i;
			set_j = set_j - aim_j;
			if (!chkNothing) // chkNothing �Լ� ����ô� �������� �ʴ´�
				table[set_i][set_j] = turn;
		}
		if (!chkNothing)
			table[x][y] = turn; // ������ ó�� Ŭ���� ������ �ڽ��� ���� ä���.
		return 0; // �������� ����
	}

	public boolean chkReverse(int x, int y, int dol) {
		int i, j;
		int lmt_i, lmt_j;
		boolean stat = false; // ������ true ��ȯ

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
				if (table[lmt_i][lmt_j] == dol) { // �ݴ뵹 �� ���
					set_i = lmt_i;
					set_j = lmt_j;
					aim_i = i;
					aim_j = j;
					if (reverse(x, y) == 0) // reverse�Լ� ȣ��
						stat = true; // ������ ���� true
				} else
					continue;
			}
		}
		return stat; // �ٲܵ��� ���� ��� stat = false
	}

	public String chkNum() { // �� �� ī��Ʈ
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
			return usr1cnt > usr2cnt ? "������" : "�Ͼᵹ";
	}

	public void chkNothing() { // �� ���� �ִ��� Ȯ�� �ϴ� �Լ�
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
		if (possible == 0) { // �� ���� ���� ��� ���� �� �ѱ��
			if (turn == 0)
				turn = 1;
			else
				turn = 0;
		}
	}
//	public static void main(String[] args) throws IOException {
//
//		JFrame.setDefaultLookAndFeelDecorated(true); // �������� ������â ǥ��
//		Odello jbt = new Odello();
//		jbt.setTitle("������");
//		jbt.setSize(640, 640);
//		jbt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		jbt.setVisible(true);
//	}
}