package data;

public enum Resource {
	wheat,
	bread,
	iron,
	steel,
	copper,
	gold,
	petrol,
	plastic,
	circuit,
	car,
	phone;
	
	public int getID() {
		return ordinal();
	}
	
	public static Resource get(int id) throws Exception {
		for(var res : values()) {
			if(res.ordinal() == id) {
				return res;
			}
		}
		throw new Exception("Resource id incorrect : " + id);
	}
	
}
