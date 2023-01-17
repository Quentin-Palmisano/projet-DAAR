package data;

public class Recipe {
	
	private final Resource result;
	private final ResourceStack[] ingredients;
	
	public Recipe(Resource result, ResourceStack[] ingredients) {
		this.result = result;
		this.ingredients = ingredients;
	}

	public Resource getResult() {
		return result;
	}

	public ResourceStack[] getIngredients() {
		return ingredients;
	}
	
	public String getInfo() {
		String result = "";
		for(ResourceStack ing : ingredients) {
			Resource r = ing.getResource();
			int c = ing.getCount();
			result += String.valueOf(c) + " " + r.name() + " & ";
		}
		result = result.substring(0, result.length()-3);
		//System.out.println(result);
		return result;
	}
	
}
