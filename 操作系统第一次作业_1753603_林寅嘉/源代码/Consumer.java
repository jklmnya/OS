package assignment1;

import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
/**
 * 消费者
 * @author linyi
 *
 */
public class Consumer implements Runnable{

	private int cur_s_number;//当前的可用仓库数量
	private JProgressBar[] warehouse;//仓库
	private JTextArea textArea;//生产消费信息
	private String name;//该线程的名字
	private JLabel[] ware_count;//每个缓存区内的物品数量
	private boolean[] stop;//是否停止当前线程的标志
	
	//构造函数
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
			//给缓存区加锁，防止其他线程对其访问
			synchronized(warehouse) {
				//判断是否需要结束当前线程
				if(stop[0]) break;
				//随机产生一个缓存区的序号
				int store_number = Math.max(new Random().nextInt(cur_s_number + 1), 1);
				int current_number = warehouse[store_number].getValue();
				//当前缓存区为空，停止消费，通知生产
				if(current_number == 0) {
					info.append("消费消息\n").append("当前消费者 : ").append(this.name);
					info.append(" 仓库").append(store_number).append("已空，无法消费\n\n");
					textArea.append(info.toString());
					textArea.setCaretPosition(textArea.getDocument().getLength());
					
					try {
						warehouse.wait();//放弃对缓存区的锁
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					//开始消费
					int count = Math.max((int)(Math.random()*10), 1);
					current_number -= count;//随机产生要消费的数量
					
					info.append("消费消息\n").append("当前消费者 : ").append(this.name);
					info.append(" 消费仓库").append(store_number).append("中").append(count).append("件商品\n\n");
					textArea.append(info.toString());
					textArea.setCaretPosition(textArea.getDocument().getLength());
					//判断当前数量是否小于0
					if(current_number <= 0) {
						warehouse[store_number].setValue(0);
						ware_count[store_number].setText("当前货物数量:0");
					} else {
						warehouse[store_number].setValue(current_number);
						ware_count[store_number].setText("当前货物数量:" + current_number);
					}
				}
				//当前线程持有缓存区的锁，休眠1s
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//消费完毕,通知生产
				warehouse.notifyAll();	
			}	
			//当前线程休眠1s
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
