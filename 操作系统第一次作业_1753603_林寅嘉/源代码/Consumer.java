package assignment1;

import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
/**
 * ������
 * @author linyi
 *
 */
public class Consumer implements Runnable{

	private int cur_s_number;//��ǰ�Ŀ��òֿ�����
	private JProgressBar[] warehouse;//�ֿ�
	private JTextArea textArea;//����������Ϣ
	private String name;//���̵߳�����
	private JLabel[] ware_count;//ÿ���������ڵ���Ʒ����
	private boolean[] stop;//�Ƿ�ֹͣ��ǰ�̵߳ı�־
	
	//���캯��
	public Consumer(int cur_s_number, JProgressBar[] warehouse, JTextArea textArea, String name, JLabel[] ware_count, boolean[] stop) {
		this.cur_s_number = cur_s_number;
		this.warehouse = warehouse;
		this.textArea = textArea;
		this.name = name;
		this.ware_count = ware_count;
		this.stop = stop;
	}

	@Override
	public void run() {
		while(!stop[0]) {
			StringBuffer info = new StringBuffer();
			//����������������ֹ�����̶߳������
			synchronized(warehouse) {
				//�ж��Ƿ���Ҫ������ǰ�߳�
				if(stop[0]) break;
				//�������һ�������������
				int store_number = Math.max(new Random().nextInt(cur_s_number + 1), 1);
				int current_number = warehouse[store_number].getValue();
				//��ǰ������Ϊ�գ�ֹͣ���ѣ�֪ͨ����
				if(current_number == 0) {
					info.append("������Ϣ\n").append("��ǰ������ : ").append(this.name);
					info.append(" �ֿ�").append(store_number).append("�ѿգ��޷�����\n\n");
					textArea.append(info.toString());
					textArea.setCaretPosition(textArea.getDocument().getLength());
					
					try {
						warehouse.wait();//�����Ի���������
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					//��ʼ����
					int count = Math.max((int)(Math.random()*10), 1);
					current_number -= count;//�������Ҫ���ѵ�����
					
					info.append("������Ϣ\n").append("��ǰ������ : ").append(this.name);
					info.append(" ���Ѳֿ�").append(store_number).append("��").append(count).append("����Ʒ\n\n");
					textArea.append(info.toString());
					textArea.setCaretPosition(textArea.getDocument().getLength());
					//�жϵ�ǰ�����Ƿ�С��0
					if(current_number <= 0) {
						warehouse[store_number].setValue(0);
						ware_count[store_number].setText("��ǰ��������:0");
					} else {
						warehouse[store_number].setValue(current_number);
						ware_count[store_number].setText("��ǰ��������:" + current_number);
					}
				}
				//��ǰ�̳߳��л���������������1s
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//�������,֪ͨ����
				warehouse.notifyAll();	
			}	
			//��ǰ�߳�����1s
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
