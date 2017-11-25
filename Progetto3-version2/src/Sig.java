
public class Sig {
	private String yn;
	private int IDn;
	private String timestamp;
	private int n;
	private LinkingInformation li;
	
	public Sig(String yn, int IDn, String timestamp, int n, LinkingInformation li) {
		this.yn = yn;
		this.IDn = IDn;
		this.setTimeStamp(timestamp);
		this.n = n;
		this.li = li;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimeStamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
