package vexatos.tgregworks.reference;

import net.minecraft.item.Item;
import tconstruct.smeltery.TinkerSmeltery;
import tconstruct.weaponry.TinkerWeaponry;

/**
 * @author Vexatos
 */
public interface Pattern {

	public static enum MetalPatterns implements Pattern {
		ingot,
		rod,
		pickaxe,
		shovel,
		axe,
		swordblade,
		largeguard,
		mediumguard,
		crossbar,
		binding,
		frypan, sign,
		knifeblade,
		chisel,
		largerod,
		toughbinding,
		largeplate,
		broadaxe,
		scythe,
		excavator,
		largeblade,
		hammerhead,
		fullguard,
		nothing1,
		nothing2,
		arrowhead,
		gem,
		nugget;

		public Item getPatternItem() {
			return TinkerSmeltery.metalPattern;
		}
	}

	public static enum WeaponryPatterns implements Pattern {
		shuriken,
		crossbowlimb,
		crossbowbody,
		bowlimb;

		public Item getPatternItem() {
			return TinkerWeaponry.metalPattern;
		}
	}

	public Item getPatternItem();

	public String name();

	public int ordinal();

}
