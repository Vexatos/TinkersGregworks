package vexatos.tgregworks.integration.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tconstruct.library.tools.ToolCore;
import tconstruct.tools.TinkerTools;
import vexatos.tgregworks.reference.PartTypes;

/**
 * @author Vexatos
 */
public class TGregBowRecipe extends TGregToolRecipe {

	private Item bowstring = TinkerTools.bowstring;

	public TGregBowRecipe(ItemStack head, ItemStack handle, ToolCore tool) {
		super(head, handle, tool);
	}

	public TGregBowRecipe(ItemStack head, ItemStack handle, ItemStack accessory, ToolCore tool) {
		super(head, handle, accessory, tool);
	}

	public TGregBowRecipe(ItemStack head, ItemStack handle, ItemStack accessory, ItemStack extra, ToolCore tool) {
		super(head, handle, accessory, extra, tool);
	}

	public TGregBowRecipe(ItemStack head, Item bowstring, ItemStack accessory, ToolCore shortbow) {
		this(head, new ItemStack(bowstring), accessory, shortbow);
	}

	@Override
	public boolean validHead(ItemStack input) {
		for(ItemStack part : newHeadList) {
			if((part.getItem() == input.getItem()) && (part.getItemDamage() == input.getItemDamage())) {
				return true;
			}
			if(input.getItem() == PartTypes.getFromID(part.getItemDamage()).counterpart) {
				return true;
			}
			if(toolRod != null && part.getItem() == toolRod && (input.getItem() == Items.stick || input.getItem() == Items.bone)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validAccessory(ItemStack input) {
		if(input == null) {
			return newAccessoryList.size() < 1;
		}
		for(ItemStack part : newAccessoryList) {
			if((part.getItem() == input.getItem()) && (part.getItemDamage() == input.getItemDamage())) {
				return true;
			}
			if(input.getItem() == PartTypes.getFromID(part.getItemDamage()).counterpart){
				return true;
			}
			if(bowstring != null && part.getItem() == bowstring && input.getItem() == bowstring) {
				return true;
			}
			if(toolRod != null && part.getItem() == toolRod && (input.getItem() == Items.stick || input.getItem() == Items.bone)) {
				return true;
			}
		}
		return false;
	}
}
