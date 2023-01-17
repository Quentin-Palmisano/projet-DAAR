package data;

public class ResourceStack {

	private int count;
	private final Resource resource;
	
	public ResourceStack(Resource resource, int count) {
		this.count = count;
		this.resource = resource;
	}

	public void add(int n) {
		count += n;
	}
	
	public void substract(int n) {
		count -= n;
	}
	
	public int getCount() {
		return count;
	}

	public Resource getResource() {
		return resource;
	}
	
}
