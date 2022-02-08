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
		
		//根据不同的错误信息返回不同的警告窗口
		switch(error_type) {
		case(0):
			alert.setText("创建txt文件失败");
			break;
		case(1):
			alert.setText("创建文件夹失败");
			break;
		case(2):
			alert.setText("格式化文件失败");
			break;
		case(3):
			alert.setText("删除文件失败");
			break;
		case(4):
			alert.setText("写入文件失败，空间不足");
		break;
		default:
			break;
		}
		frame.getContentPane().add(alert);
	}
}