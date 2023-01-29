package vexatos.tgregworks.integration.recipe.tconstruct;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.oredict.RecipeSorter;

import tconstruct.library.tools.DualMaterialToolPart;
import tconstruct.library.util.IToolPart;
import tconstruct.weaponry.TinkerWeaponry;
import vexatos.tgregworks.item.ItemTGregPart;
import vexatos.tgregworks.reference.Mods;
import vexatos.tgregworks.reference.PartTypes;

/**
 * @author SlimeKnights, Vexatos
 */
public class TGregAlternateBoltRecipe implements IRecipe {

    static {
        // register the recipe with the recipesorter
        RecipeSorter.register(
                Mods.TGregworks + ":part",
                TGregAlternateBoltRecipe.class,
                RecipeSorter.Category.SHAPELESS,
                "");
    }

    protected ItemStack outputPart;

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        outputPart = null;
        ItemStack rod = null;
        ItemStack head = null;

        for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++) {
            ItemStack slot = inventoryCrafting.getStackInSlot(i);
            // empty slot
            if (slot == null) {
                continue;
            }

            // is it the tool?
            if (isEqualType(PartTypes.ArrowHead, slot.getItem())) {
                // only one arrowhead
                if (head != null) {
                    return false;
                }

                head = slot;
            } else if (isEqualType(PartTypes.ToolRod, slot.getItem())) {
                // only one rod
                if (rod != null) {
                    return false;
                }

                rod = slot;
            } else {
                // unknown object
                return false;
            }
        }

        if (rod == null || head == null) {
            return false;
        }

        // craft the bolt
        int mat1 = ((IToolPart) rod.getItem()).getMaterialID(rod);
        int mat2 = ((IToolPart) head.getItem()).getMaterialID(head);

        outputPart = DualMaterialToolPart.createDualMaterial(TinkerWeaponry.partBolt, mat1, mat2);

        return true;
    }

    protected boolean isEqualType(PartTypes part, Item input) {
        if (input instanceof ItemTGregPart) {
            return part == ((ItemTGregPart) input).getType();
        }
        return input == part.getCounterpart();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventory) {
        return outputPart;
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return outputPart;
    }
}
