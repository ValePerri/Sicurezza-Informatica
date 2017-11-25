
public class LinkingInformation {
	private int n;
	private int IDnprev;
	private String ynprev;
	private String hashprevLI;
	private String timestampprev;
	
	public LinkingInformation(String timestamp, int n, int IDnprev, String ynprev, LinkingInformation hashprevLI) {
		if(n==0) {
			this.timestampprev = timestamp;
			this.n = n;
			this.IDnprev = 0;
			this.ynprev = "0";
			this.hashprevLI = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashprevLI.toString());
		} else {
		this.n = n;
		this.IDnprev = IDnprev;
		this.ynprev = ynprev;
		this.hashprevLI = org.apache.commons.codec.digest.DigestUtils.sha256Hex(hashprevLI.toString());
		}
	}
	
	public String getHashprevLI() {
		return this.hashprevLI;
	}
	
	public LinkingInformation(String timestamp, int n, String ynprev, int IDnprev) {
		// TODO Auto-generated constructor stub
		this.timestampprev = timestamp;
		this.n = n;
		this.ynprev = ynprev;
		this.IDnprev = IDnprev;
		this.hashprevLI = "0";
	}

	public String toString() {
		return this.n + " " + this.timestampprev + " " + this.IDnprev + " " + this.ynprev + " " + this.hashprevLI;
	}
}
