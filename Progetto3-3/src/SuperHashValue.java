public class SuperHashValue {
	private int dim;
	private String[] superHashValues;
	private int count = 0;
	
	public SuperHashValue(String seed, int dim) {
		this.dim = dim;
		this.superHashValues = new String[dim];
		this.superHashValues[0] = org.apache.commons.codec.digest.DigestUtils.sha256Hex(seed);
	}
	
	public String computeSHV(MerkleTree mt) {
		this.count += 1;
		String shv_1 = this.superHashValues[count-1];
		String hv_i = mt.rootHashValue();
		String result = org.apache.commons.codec.digest.DigestUtils.sha256Hex(shv_1 + hv_i);
		this.superHashValues[this.count] = result;
		return result;
	}
	
	public String[] getSuperHashValues() {
		return this.superHashValues;
	}
}
