package crawler;

public class Triple {
	String url;
	Page parent;
	int depth;

	Triple(String url, int depth){
		this.url = url;
		this.parent = null;
		this.depth = depth;
	}
	
	Triple(String url, Page link, int depth){
		this.url = url;
		this.depth = depth;
		this.parent = link;
	}
	
	public Page getPage() {
		return parent;
	}
	
	String getUrl() {
		return url;
	}
	
	int getDepth() {
		return depth;
	}


}
