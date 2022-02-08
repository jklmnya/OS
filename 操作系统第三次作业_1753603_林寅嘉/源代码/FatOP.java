package assignment3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FatOP {

	//��Fat��Ϣд���ļ�
	public static void writeFat(int[] Fat) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File("Fat.txt")));
		for(int i = 0;i<Fat.length;i++) {
			bw.write(Fat[i]+" ");
		}
		bw.write(FileOP.Number+"");
		bw.flush();
		bw.close();
	}

	//��ȡFat�����Ϣ
	public static void readFat(int[] Fat) throws IOException {
		File file = new File("Fat.txt");
		if(file.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String info = br.readLine();
			String[] temps = info.split(" ");
			int len = Fat.length;
			for(int i = 0;i<len;i++) {
				Fat[i] = Integer.parseInt(temps[i]);
			}
			FileOP.Number = Integer.parseInt(temps[len]);
			br.close();
		}
	}
	
	//��txt�ļ���д��ʱ������Fat, -1����txt�ļ��Ľ�����־
	public static boolean startFat(int[] Fat, long size, int startFat) {
		int neededBlock = (int) ((size == 0)?1:Math.ceil((double)size/255));
		int len = Fat.length;
		boolean isEnough = false;
		List<Integer> list = new ArrayList<>();
		for(int i = 0;i<len;i++) {
			if(Fat[i] == 0) {
				list.add(i);
			}
			if(list.size() == neededBlock) {
				isEnough = true;
				break;
			}
		}
		if(isEnough) {
			Fat[startFat] = list.get(0);
			int index = neededBlock - 1;
			for(int i = 1;i<index;i++) {
				Fat[list.get(i)] = list.get(i+1);
			}
			Fat[list.get(index)] = -1;
			return true;
		} else {
			return false;
		}
	}
	
	//�½�txtʱ������Fat, ����-2ʱ�������ʧ��
	public static int getStartFat(int[] Fat) {
		int len = Fat.length;
		for(int i = 0;i<len;i++) {
			if(Fat[i] == 0) {
				Fat[i] = -1;
				return i;
			}
		}
		return -2;
	}
	
	//��ʽ��txt�ļ�����Fat
	public static void cleanFileFat(int[] Fat, int startFat) {
		if(Fat[startFat] == -1) return;//����ļ�Ϊ�գ���ֱ�ӷ���
		int nextFat = Fat[startFat];//��һ���������
		int copyNextFat = -1;
		while(Fat[nextFat] != -1) {
			copyNextFat = nextFat;
			nextFat = Fat[Fat[nextFat]];
			Fat[copyNextFat] = 0;
		}
		Fat[nextFat] = 0;
		Fat[startFat] = -1;
	}
	
	//ɾ��txt�ļ�ʱ����Fat
	public static void deleteFileFat(int[] Fat, int startFat) {
		if(Fat[startFat] == -1) {
			Fat[startFat] = 0;//����ļ�Ϊ�գ���ֱ�ӷ���
			return;
		}
		int nextFat = Fat[startFat];//��һ���������
		int copyNextFat = -1;
		while(Fat[nextFat] != -1) {
			copyNextFat = nextFat;
			nextFat = Fat[Fat[nextFat]];
			Fat[copyNextFat] = 0;
		}
		Fat[nextFat] = 0;
		Fat[startFat] = 0;
	}	
}