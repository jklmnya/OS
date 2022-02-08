package assignment3;

import java.io.Serializable;
import java.util.ArrayList;

public class FCB implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String fileName;//�ļ���
	private String fileType;//�ļ�����
	private String foundTime;//����ʱ��
	private String location;//�ļ���ַ
	private int startFat;//FAT���е���ʼ����
	private long size;//�ļ���С
	private FCB fatherFile;//�ϼ��ļ�
	private ArrayList<FCB> subFils;//�¼��ļ�

	public FCB() {
	}
	
	public FCB(String fileName, String fileType, String foundTime, String location, int startFat, long size,
			FCB fatherFile, ArrayList<FCB> subFils) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.foundTime = foundTime;
		this.location = location;
		this.startFat = startFat;
		this.size = size;
		this.fatherFile = fatherFile;
		this.subFils = subFils;
	}
	
	public FCB(String location, ArrayList<FCB> subFils) {
		this.location = location;
		this.subFils = subFils;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public int getStartFat() {
		return startFat;
	}

	public void setStartFat(int startFat) {
		this.startFat = startFat;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public FCB getFatherFile() {
		return fatherFile;
	}

	public void setFatherFile(FCB fatherFile) {
		this.fatherFile = fatherFile;
	}

	public ArrayList<FCB> getSubFils() {
		return subFils;
	}

	public void setSubFils(ArrayList<FCB> subFils) {
		this.subFils = subFils;
	}

	public String getFoundTime() {
		return foundTime;
	}

	public void setFoundTime(String foundTime) {
		this.foundTime = foundTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}