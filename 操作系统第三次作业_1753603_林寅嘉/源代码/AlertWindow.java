package assignment3;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class AlertWindow {

	protected JFrame frame;
	private int error_type;
	
	public AlertWindow(int error) {
		this.error_type = error;
		initialize();
	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("\u8B66\u544A");
		frame.setBounds(100, 100, 333, 269);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel alert = new JLabel("");
		alert.setBounds(100, 94, 164, 42);
		
		//���ݲ�ͬ�Ĵ�����Ϣ���ز�ͬ�ľ��洰��
		switch(error_type) {
		case(0):
			alert.setText("����txt�ļ�ʧ��");
			break;
		case(1):
			alert.setText("�����ļ���ʧ��");
			break;
		case(2):
			alert.setText("��ʽ���ļ�ʧ��");
			break;
		case(3):
			alert.setText("ɾ���ļ�ʧ��");
			break;
		case(4):
			alert.setText("д���ļ�ʧ�ܣ��ռ䲻��");
		break;
		default:
			break;
		}
		frame.getContentPane().add(alert);
	}
}