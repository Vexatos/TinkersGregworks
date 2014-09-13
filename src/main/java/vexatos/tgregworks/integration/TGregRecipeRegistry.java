package vexatos.tgregworks.integration;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tconstruct.library.crafting.ToolBuilder;
import tconstruct.library.tools.ToolCore;
import tconstruct.tools.TinkerTools;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.integration.recipe.TGregBowRecipe;
import vexatos.tgregworks.integration.recipe.TGregToolRecipe;
import vexatos.tgregworks.reference.PartTypes;

import java.util.HashMap;

/**
 * @author Vexatos
 */
public class TGregRecipeRegistry {

	private HashMap<PartTypes, ItemStack> partMap = new HashMap<PartTypes, ItemStack>();

	public void addRecipesForToolBuilder() {

		Item parts = TGregworks.toolParts;

		for(PartTypes p : PartTypes.values()) {
			partMap.put(p, new ItemStack(parts, 1, p.metaID));
		}

		addTGregToolRecipe(TinkerTools.pickaxe,		PartTypes.PickaxeHead,		PartTypes.ToolRod,		PartTypes.Binding							);
		addTGregToolRecipe(TinkerTools.shovel,		PartTypes.ShovelHead,		PartTypes.ToolRod													);
		addTGregToolRecipe(TinkerTools.hatchet,		PartTypes.AxeHead,			PartTypes.ToolRod													);
		addTGregToolRecipe(TinkerTools.mattock,		PartTypes.PickaxeHead,		PartTypes.ToolRod,		PartTypes.ShovelHead						);
		addTGregToolRecipe(TinkerTools.chisel,		PartTypes.ChiselHead,		PartTypes.ToolRod													);

		addTGregToolRecipe(TinkerTools.broadsword,	PartTypes.SwordBlade,		PartTypes.ToolRod,		PartTypes.LargeGuard						);
		addTGregToolRecipe(TinkerTools.longsword,	PartTypes.SwordBlade,		PartTypes.ToolRod,		PartTypes.MediumGuard						);
		addTGregToolRecipe(TinkerTools.rapier,		PartTypes.SwordBlade,		PartTypes.ToolRod,		PartTypes.Crossbar							);
		addTGregToolRecipe(TinkerTools.dagger,		PartTypes.KnifeBlade,		PartTypes.ToolRod,		PartTypes.Crossbar							);
		addTGregToolRecipe(TinkerTools.cutlass,		PartTypes.SwordBlade,		PartTypes.ToolRod,		PartTypes.FullGuard							);
		addTGregToolRecipe(TinkerTools.frypan,		PartTypes.FrypanHead,		PartTypes.ToolRod													);
		addTGregToolRecipe(TinkerTools.battlesign,	PartTypes.SignHead,			PartTypes.ToolRod													);

		addTGregToolRecipe(TinkerTools.scythe,		PartTypes.ScytheHead,		PartTypes.ToughRod,		PartTypes.ToughBind,	PartTypes.ToughRod	);
		addTGregToolRecipe(TinkerTools.lumberaxe,	PartTypes.LumberHead,		PartTypes.ToughRod,		PartTypes.LargePlate,	PartTypes.ToughBind	);
		addTGregToolRecipe(TinkerTools.cleaver,		PartTypes.LargeSwordBlade,	PartTypes.ToughRod,		PartTypes.LargePlate,	PartTypes.ToughRod	);
		addTGregToolRecipe(TinkerTools.excavator,	PartTypes.ExcavatorHead,	PartTypes.ToughRod,		PartTypes.LargePlate,	PartTypes.ToughBind	);
		addTGregToolRecipe(TinkerTools.hammer,		PartTypes.HammerHead,		PartTypes.ToughRod,		PartTypes.LargePlate,	PartTypes.LargePlate);
		addTGregToolRecipe(TinkerTools.battleaxe,	PartTypes.LumberHead,		PartTypes.ToughRod,		PartTypes.LumberHead,	PartTypes.ToughBind	);

		ToolBuilder.addCustomToolRecipe(new TGregBowRecipe(partMap.get(PartTypes.ToolRod), TinkerTools.bowstring, partMap.get(PartTypes.ToolRod), TinkerTools.shortbow));

		addTGregToolRecipe(TinkerTools.arrow, PartTypes.ArrowHead, PartTypes.ToolRod, TinkerTools.fletching);
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
