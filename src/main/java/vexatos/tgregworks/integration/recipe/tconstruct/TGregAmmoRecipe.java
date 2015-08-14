package vexatos.tgregworks.integration.recipe.tconstruct;

import net.minecraft.item.Item;
import tconstruct.library.tools.DynamicToolPart;
import tconstruct.library.tools.ToolCore;
import tconstruct.weaponry.TinkerWeaponry;
import vexatos.tgregworks.item.ItemTGregPart;

/**
 * @author Vexatos
 */
public class TGregAmmoRecipe extends TGregToolRecipe {

	public TGregAmmoRecipe(ItemTGregPart head, ItemTGregPart handle, Item fletching, ToolCore ammo) {
		super(head, handle, fletching, ammo);
	}

	public TGregAmmoRecipe(ItemTGregPart head, DynamicToolPart shaft, Item fletching, ToolCore ammo) {
		super(head, shaft, fletching, ammo);
	}

	@Override
	public boolean validHandle(Item input) {
		return TinkerWeaponry.partArrowShaft != null
			&& handleList.contains(TinkerWeaponry.partArrowShaft)
			&& input == TinkerWeaponry.partArrowShaft
			|| super.validHandle(input);
	}
}
