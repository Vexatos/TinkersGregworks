package vexatos.tgregworks.reference;

/**
 * @author Vexatos
 */
public class Config {
	public static class Category {

		public static final String
			OreDictMaterial = "materials",
			Enable = "enable",
			Recipes = "recipe",
			Global = "global",
			General = "general";
	}

	public static final String
		Durability = "durability",
		MiningSpeed = "miningspeed",
		Attack = "attack",
		HandleModifier = "handlemodifier";

	public static final String
		StoneboundLevel = Category.OreDictMaterial + ".stoneboundlevel",
		ReinforcedLevel = Category.OreDictMaterial + ".reinforcedlevel";

	public static final String
		BowDrawSpeed = "bowdrawspeed",
		BowFlightSpeed = "bowflightspeed";

	public static final String
		ArrowMass = "arrowmass",
		ArrowBreakChance = "arrowfragility";

	public static String onMaterial(String key) {
		return concat(Category.OreDictMaterial, key);
	}

	public static String concat(String first, String... keys) {
		String result = first;
		for(String key : keys) {
			result += "." + key;
		}
		return result;
	}
}
