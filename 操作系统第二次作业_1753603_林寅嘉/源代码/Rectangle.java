package assignment2;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Rectangle extends JPanel{

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Integer> startLoc;
	private ArrayList<Integer> endLoc;

	public Rectangle() {
		startLoc = new ArrayList<>();
		endLoc = new ArrayList<>();
	}
	
	public void addLoc(int start, int end) {
		startLoc.add(start);
		endLoc.add(end);
	}
	
	public void removeLoc(int start, int end) {
		startLoc.remove((Object)start);
		endLoc.remove((Object)end);
	}
	
	public void clear() {
		startLoc.clear();
		endLoc.clear();
	}
	
	@Override
	//绘制内存使用情况
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.GRAY);
		int size = startLoc.size();
		for(int i = 0;i<size;i++) {
			System.out.println(startLoc.get(i) + " " + endLoc.get(i));
			int start = startLoc.get(i);
			int length = endLoc.get(i) - start;
			g.fillRect(start, 0, length, 20);
		}
		System.out.println();
	}
}
