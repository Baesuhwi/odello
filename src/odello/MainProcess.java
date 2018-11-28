package odello;

import javax.swing.*;

public class MainProcess {
	LoginView loginView;
	Odello odello;
	
	public static void main(String[] args) {
		//메인 클래스 실행
		MainProcess main = new MainProcess();
		main.loginView = new LoginView();//로그인창 보이기
		main.loginView.setMain(main);//로그인창에게 메인 클래스 보내기
//		JOptionPane.showMessageDialog(null, "경고!");
		
	}
}
