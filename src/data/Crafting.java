package data;

import java.util.HashMap;
import java.util.Map;

import static data.Resource.*;

public abstract class Crafting {
	
	public static final Map<Resource, Recipe> recipes = new HashMap<>();
	
	private static void addRecipe(Recipe recipe) {
		recipes.put(recipe.getResult(), recipe);
	}
	
	static {
		
		addRecipe(new Recipe(bread, new ResourceStack[] {
				new ResourceStack(wheat, 2)
		}));
		
		addRecipe(new Recipe(steel, new ResourceStack[] {
				new ResourceStack(iron, 1)
		}));
		
		addRecipe(new Recipe(plastic, new ResourceStack[] {
				new ResourceStack(petrol, 1)
		}));
		
		addRecipe(new Recipe(circuit, new ResourceStack[] {
				new ResourceStack(plastic, 3),
				new ResourceStack(copper, 1),
				new ResourceStack(gold, 1)
		}));
		
		addRecipe(new Recipe(car, new ResourceStack[] {
				new ResourceStack(steel, 10),
				new ResourceStack(circuit, 1),
				new ResourceStack(petrol, 5)
		}));
		
		addRecipe(new Recipe(phone, new ResourceStack[] {
				new ResourceStack(steel, 3),
				new ResourceStack(circuit, 5),
				new ResourceStack(plastic, 3),
				new ResourceStack(copper, 1)
		}));
		
	}
	
}
