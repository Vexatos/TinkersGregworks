package vexatos.tgregworks.integration.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import tconstruct.library.tools.ToolCore;
import tconstruct.tools.TinkerTools;
import vexatos.tgregworks.item.ItemTGregPart;

/**
 * @author Vexatos
 */
public class TGregBowRecipe extends TGregToolRecipe {

	private Item bowstring = TinkerTools.bowstring;

	public TGregBowRecipe(ItemTGregPart head, ItemTGregPart handle, ToolCore tool) {
		super(head, handle, tool);
	}

	public TGregBowRecipe(ItemTGregPart head, ItemTGregPart handle, ItemTGregPart accessory, ToolCore tool) {
		super(head, handle, accessory, tool);
	}

	public TGregBowRecipe(ItemTGregPart head, ItemTGregPart handle, ItemTGregPart accessory, ItemTGregPart extra, ToolCore tool) {
		super(head, handle, accessory, extra, tool);
	}

	public TGregBowRecipe(ItemTGregPart head, Item bowstring, ItemTGregPart accessory, ToolCore shortbow) {
		super(head, bowstring, accessory, shortbow);
	}

	@Override
	public boolean validHead(Item input) {
		for(ItemTGregPart part : newHeadList) {
			if((part == input) && isEqualType(part, input)) {
				return true;
			}
			if(input == part.getType().counterpart) {
				return true;
			}
			if(toolRod != null && part.getType().counterpart == toolRod && (input == Items.stick || input == Items.bone)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validHandle(Item input) {

		for(ItemTGregPart part : newHandleList) {
			if((part == input) && isEqualType(part, input)) {
				return true;
			}
			if(input == part.getType().counterpart) {
				return true;
			}
			if(bowstring != null && handleList.contains(bowstring) && input == bowstring) {
				return true;
			}
			if(toolRod != null && part.getType().counterpart == toolRod && (input == Items.stick || input == Items.bone)) {
				return true;
			}
		}
		return false;
	}
}
