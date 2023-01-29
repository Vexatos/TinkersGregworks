package vexatos.tgregworks.integration.recipe.tconstruct;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

import tconstruct.library.tools.ToolCore;
import tconstruct.weaponry.TinkerWeaponry;
import vexatos.tgregworks.item.ItemTGregPart;

/**
 * @author Vexatos
 */
public class TGregBowRecipe extends TGregToolRecipe {

    private Item bowstring = TinkerWeaponry.bowstring;

    public TGregBowRecipe(ItemTGregPart head, ItemTGregPart handle, ToolCore tool) {
        super(head, handle, tool);
    }

    public TGregBowRecipe(ItemTGregPart head, ItemTGregPart handle, ItemTGregPart accessory, ToolCore tool) {
        super(head, handle, accessory, tool);
    }

    public TGregBowRecipe(ItemTGregPart head, ItemTGregPart handle, ItemTGregPart accessory, ItemTGregPart extra,
            ToolCore tool) {
        super(head, handle, accessory, extra, tool);
    }

    public TGregBowRecipe(ItemTGregPart head, Item bowstring, ItemTGregPart accessory, ToolCore shortbow) {
        super(head, bowstring, accessory, shortbow);
    }

    public TGregBowRecipe(ItemTGregPart head, ItemTGregPart handle, Item bowstring, ItemTGregPart extra,
            ToolCore shortbow) {
        super(head, handle, bowstring, extra, shortbow);
    }

    public TGregBowRecipe(ItemTGregPart head, Item bowstring, ItemTGregPart accessory, ItemTGregPart extra,
            ToolCore shortbow) {
        super(head, bowstring, accessory, extra, shortbow);
    }

    @Override
    public boolean validHead(Item input) {
        for (ItemTGregPart part : newHeadList) {
            if (toolRod != null && part.getType().getCounterpart() == toolRod
                    && (input == Items.stick || input == Items.bone)) {
                return true;
            }
        }
        return super.validHead(input);
    }

    @Override
    public boolean validHandle(Item input) {
        return bowstring != null && handleList.contains(bowstring) && input == bowstring || super.validHandle(input);
    }

    @Override
    public boolean validAccessory(Item input) {
        if (input == null) {
            return accessoryList.size() < 1;
        }
        return bowstring != null && accessoryList.contains(bowstring) && input == bowstring
                || super.validAccessory(input);
    }
}
