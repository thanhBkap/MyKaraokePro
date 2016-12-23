package vn.com.model;

public class BaiHat {
	private int maSo;
	private String tenBaiHat;
	private String caSi;
	private boolean thich;
	public int getMaSo() {
		return maSo;
	}
	public void setMaSo(int maSo) {
		this.maSo = maSo;
	}
	public String getTenBaiHat() {
		return tenBaiHat;
	}
	public void setTenBaiHat(String tenBaiHat) {
		this.tenBaiHat = tenBaiHat;
	}
	public String getCaSi() {
		return caSi;
	}
	public void setCaSi(String caSi) {
		this.caSi = caSi;
	}
	public boolean isThich() {
		return thich;
	}
	public void setThich(boolean thich) {
		this.thich = thich;
	}
	public BaiHat(int maSo, String tenBaiHat, String caSi, boolean thich) {
		super();
		this.maSo = maSo;
		this.tenBaiHat = tenBaiHat;
		this.caSi = caSi;
		this.thich = thich;
	}
	public BaiHat() {
		super();
	}
}
