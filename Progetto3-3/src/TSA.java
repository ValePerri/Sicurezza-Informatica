import java.security.Timestamp;
import java.util.Date;
import java.text.SimpleDateFormat;

public class TSA {
	
	public Sig signature(String yn, int IDn, int n, LinkingInformation li) {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		Sig s = new Sig(yn, IDn, timeStamp, n, li);
		return s;
	}
}
