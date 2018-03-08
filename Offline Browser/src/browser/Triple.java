package browser;

public class Triple {
	String url;
	int depth;

	Triple(String url, int depth){
		this.url = url;
		this.depth = depth;
	}
	
	String getUrl() {
		return url;
	}
	
	int getDepth() {
		return depth;
	}


}
