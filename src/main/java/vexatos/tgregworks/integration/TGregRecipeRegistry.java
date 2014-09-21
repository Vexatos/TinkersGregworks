package vexatos.tgregworks.integration;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tconstruct.library.crafting.ToolBuilder;
import tconstruct.library.tools.ToolCore;
import tconstruct.tools.TinkerTools;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.integration.recipe.TGregBowRecipe;
import vexatos.tgregworks.integration.recipe.TGregToolRecipe;
import vexatos.tgregworks.item.ItemTGregPart;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.TGregUtils;

import java.util.HashMap;

/**
 * @author SlimeKnights, Vexatos
 */
public class TGregRecipeRegistry {

	private HashMap<PartTypes, ItemTGregPart> partMap = new HashMap<PartTypes, ItemTGregPart>();

	public void addGregTechPartRecipes() {
		for(Materials m : TGregworks.registry.toolMaterials) {
			for(PartTypes p : PartTypes.values()) {
				ItemStack input = new ItemStack(TGregworks.registry.toolParts.get(p));
				NBTTagCompound data = TGregUtils.getTagCompound(input);
				data.setString("material", m.name());
				input.setTagCompound(data);
				GregTech_API.sRecipeAdder.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, m, p.price), p.pattern, input, 80 * p.price, 30);
				GregTech_API.sRecipeAdder.addAlloySmelterRecipe(getChunk(m, p.price), p.pattern, input, 80 * p.price, 30);
			}
		}
	}

	private ItemStack getChunk(Materials m, int amount) {
		ItemStack stack = new ItemStack(TGregworks.registry.toolParts.get(PartTypes.Chunk), amount, 0);
		NBTTagCompound data = TGregUtils.getTagCompound(stack);
		data.setString("material", m.name());
		return stack;
	}

	public void addRecipesForToolBuilder() {

		for(PartTypes p : PartTypes.values()) {
			partMap.put(p, TGregworks.registry.toolParts.get(p));
		}

		addTGregToolRecipe(TGregRegistry.pickaxe, PartTypes.PickaxeHead, PartTypes.ToolRod, PartTypes.Binding);
		addTGregToolRecipe(TGregRegistry.shovel, PartTypes.ShovelHead, PartTypes.ToolRod);
		addTGregToolRecipe(TGregRegistry.hatchet, PartTypes.AxeHead, PartTypes.ToolRod);
		addTGregToolRecipe(TGregRegistry.mattock, PartTypes.PickaxeHead, PartTypes.ToolRod, PartTypes.ShovelHead);
		addTGregToolRecipe(TGregRegistry.chisel, PartTypes.ChiselHead, PartTypes.ToolRod);

		addTGregToolRecipe(TGregRegistry.broadsword, PartTypes.SwordBlade, PartTypes.ToolRod, PartTypes.LargeGuard);
		addTGregToolRecipe(TGregRegistry.longsword, PartTypes.SwordBlade, PartTypes.ToolRod, PartTypes.MediumGuard);
		addTGregToolRecipe(TGregRegistry.rapier, PartTypes.SwordBlade, PartTypes.ToolRod, PartTypes.Crossbar);
		addTGregToolRecipe(TGregRegistry.dagger, PartTypes.KnifeBlade, PartTypes.ToolRod, PartTypes.Crossbar);
		addTGregToolRecipe(TGregRegistry.cutlass, PartTypes.SwordBlade, PartTypes.ToolRod, PartTypes.FullGuard);
		addTGregToolRecipe(TGregRegistry.frypan, PartTypes.FrypanHead, PartTypes.ToolRod);
		addTGregToolRecipe(TGregRegistry.battlesign, PartTypes.SignHead, PartTypes.ToolRod);

		addTGregToolRecipe(TGregRegistry.scythe, PartTypes.ScytheHead, PartTypes.ToughRod, PartTypes.ToughBind, PartTypes.ToughRod);
		addTGregToolRecipe(TGregRegistry.lumberaxe, PartTypes.LumberHead, PartTypes.ToughRod, PartTypes.LargePlate, PartTypes.ToughBind);
		addTGregToolRecipe(TGregRegistry.cleaver, PartTypes.LargeSwordBlade, PartTypes.ToughRod, PartTypes.LargePlate, PartTypes.ToughRod);
		addTGregToolRecipe(TGregRegistry.excavator, PartTypes.ExcavatorHead, PartTypes.ToughRod, PartTypes.LargePlate, PartTypes.ToughBind);
		addTGregToolRecipe(TGregRegistry.hammer, PartTypes.HammerHead, PartTypes.ToughRod, PartTypes.LargePlate, PartTypes.LargePlate);
		addTGregToolRecipe(TGregRegistry.battleaxe, PartTypes.LumberHead, PartTypes.ToughRod, PartTypes.LumberHead, PartTypes.ToughBind);

		ToolBuilder.addCustomToolRecipe(new TGregBowRecipe(partMap.get(PartTypes.ToolRod), TinkerTools.bowstring, partMap.get(PartTypes.ToolRod), TGregRegistry.shortbow));

		addTGregToolRecipe(TGregRegistry.arrow, PartTypes.ArrowHead, PartTypes.ToolRod, TinkerTools.fletching);
	}

	private void addTGregToolRecipe(ToolCore arrow, PartTypes arrowHead, PartTypes toolRod, Item fletching) {
		ToolBuilder.addCustomToolRecipe(new TGregToolRecipe(partMap.get(arrowHead), partMap.get(toolRod), fletching, arrow));
	}

	private void addTGregToolRecipe(ToolCore output, PartTypes head, PartTypes handle) {
		ToolBuilder.addCustomToolRecipe(new TGregToolRecipe(partMap.get(head), partMap.get(handle), output));
	}

	private void addTGregToolRecipe(ToolCore output, PartTypes head, PartTypes handle, PartTypes accessory) {
		ToolBuilder.addCustomToolRecipe(new TGregToolRecipe(partMap.get(head), partMap.get(handle), partMap.get(accessory), output));
	}

	private void addTGregToolRecipe(ToolCore output, PartTypes head, PartTypes handle, PartTypes accessory, PartTypes extra) {
		ToolBuilder.addCustomToolRecipe(new TGregToolRecipe(partMap.get(head), partMap.get(handle), partMap.get(accessory), partMap.get(extra), output));
	}
}
