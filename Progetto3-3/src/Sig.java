
public class Sig {
	private String yn;
	private int IDn;
	private String timestamp;
	private int n;
	private LinkingInformation li;
	private int IDnext;
	private String SHV0;
	private String[] superHashValues;
	
	public Sig(String yn, int IDn, String timestamp, int n, LinkingInformation li) {
		this.yn = yn;
		this.IDn = IDn;
		this.setTimeStamp(timestamp);
		this.n = n;
		this.li = li;
	}
	
	public Sig(Sig s, int idnext) {
		this.yn = s.yn;
		this.IDnext = idnext;
		this.IDn = s.IDn;
		this.timestamp = s.timestamp;
		this.n = s.n;
		this.li = s.li;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimeStamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String toString() {
		return "(" 
				 + "Marca: " + this.hashCode() + "," + "IDnext: " + this.IDnext + ")";
	}
}
