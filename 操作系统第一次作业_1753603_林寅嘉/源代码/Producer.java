package assignment1;

import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

/**
 *  ������
 * @author linyi
 *
 */

public class Producer implements Runnable{
	
	private int PRE_MAX_STORE;//ÿ����������Ԫ����������
	private int cur_s_number;//��ǰ�Ŀ��òֿ�����
	private JProgressBar[] warehouse;//�ֿ�
	private JTextArea textArea;//����������Ϣ
	private String name;//���̵߳�����
	private JLabel[] ware_count;//ÿ���������ڵ���Ʒ����
	private boolean[] stop;//�Ƿ�ֹͣ��ǰ�̵߳ı�־
	
	//���캯��
	public Producer(int pRE_MAX_STORE, int cur_s_number, JProgressBar[] warehouse, JTextArea textArea, String name, JLabel[] ware_count, boolean[] stop) {
		PRE_MAX_STORE = pRE_MAX_STORE;
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
			//����������������ֹ�����̶߳������
			synchronized(warehouse) {
				//�ж��Ƿ���Ҫ������ǰ�߳�
				if(stop[0]) return;
				StringBuffer info = new StringBuffer();//��ӡ����Ϣ
				//�������һ�������������
				int store_number = Math.max(new Random().nextInt(cur_s_number + 1) , 1);
				int current_number = warehouse[store_number].getValue();
				//��ǰ������������ֹͣ����,֪ͨ����
				if(current_number == PRE_MAX_STORE) {
					info.append("������Ϣ\n").append("��ǰ������ : ").append(this.name);
					info.append(" �ֿ�").append(store_number).append("�������޷���������\n\n");
					textArea.append(info.toString());
					textArea.setCaretPosition(textArea.getDocument().getLength());
					
					try {
						warehouse.wait();//�����Ի���������
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					//��ǰ������δ������ʼ����
					int count = Math.max((int)(Math.random()*10), 1);
					current_number += count;//�������Ҫ����������
					
					info.append("������Ϣ\n").append("��ǰ������ : ").append(this.name);
					info.append(" ����").append(count).append("����Ʒ����ֿ�").append(store_number).append("\n\n");
					textArea.append(info.toString());
					textArea.setCaretPosition(textArea.getDocument().getLength());//�ı����Զ����µ���Ͷ�
					
					//�жϵ�ǰ�����Ƿ�����������
					if(current_number >= PRE_MAX_STORE) {
						warehouse[store_number].setValue(PRE_MAX_STORE);
						ware_count[store_number].setText("��ǰ��������:100");
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
				//������ϣ�֪ͨ����
				warehouse.notifyAll();
			}	
			//��ǰ�߳�����1s
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
