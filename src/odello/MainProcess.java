package odello;

import javax.swing.*;

public class MainProcess {
	LoginView loginView;
	Odello odello;
	
	public static void main(String[] args) {
		//���� Ŭ���� ����
		MainProcess main = new MainProcess();
		main.loginView = new LoginView();//�α���â ���̱�
		main.loginView.setMain(main);//�α���â���� ���� Ŭ���� ������
//		JOptionPane.showMessageDialog(null, "���!");
		
	}
}
