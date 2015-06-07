package vexatos.tgregworks.integration;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import tconstruct.TConstruct;
import tconstruct.library.crafting.ToolBuilder;
import tconstruct.library.tools.ToolCore;
import tconstruct.tools.TinkerTools;
import tconstruct.weaponry.TinkerWeaponry;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.integration.recipe.TGregAmmoRecipe;
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
				if(p.pattern != null) {
					//GregTech_API.sRecipeAdder.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, m, p.price), p.pattern, input, 80 * p.price, 30);
					ItemStack stack = GT_OreDictUnificator.get(OrePrefixes.ingot, m,
						p.price % 2 != 0 ? (p.price / 2) + 1 : MathHelper.ceiling_double_int(p.price / 2D));
					if(stack != null) {
						GT_Values.RA.addExtruderRecipe(stack, p.pattern, input, Math.max(80, m.mDurability * p.price), m.mToolQuality < 3 ? 30 : 120);
						//GregTech_API.sRecipeAdder.addAlloySmelterRecipe(getChunk(m, p.price), p.pattern, input, 80 * p.price, 30);
						stack = getChunk(m, p.price);
						if(stack != null) {
							GT_Values.RA.addExtruderRecipe(stack, p.pattern, input, 80 + (m.mDurability * p.price), m.mToolQuality < 3 ? 30 : 120);
						}
					}
				}
			}
			ItemStack stack = getChunk(m, 2);
			ItemStack ingotStack = GT_OreDictUnificator.get(OrePrefixes.ingot, m, 1);
			if(stack != null && ingotStack != null) {
				GT_Values.RA.addExtruderRecipe(ingotStack, new ItemStack(TGregworks.shardCast, 0, 0), stack, Math.max(160, m.mDurability), m.mToolQuality < 3 ? 30 : 120);
			}
		}

		ItemStack brassstack = GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Brass, 1);
		if(TinkerTools.toolShard != null) {
			/*ArrayList list = new ArrayList();
			TinkerTools.toolShard.getSubItems(TinkerTools.toolShard, TinkerTools.toolShard.getCreativeTab(), list);
			for(Object o : list) {
				if(o instanceof ItemStack) {
					GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes.add(new GT_Recipe(alustack, (ItemStack) o, 30, 800, new ItemStack(TGregworks.shardCast, 1, 0)));
				}
			}*/
			if(TinkerTools.blankPattern != null) {
				GT_Values.RA.addExtruderRecipe(new ItemStack(TinkerTools.blankPattern, 1, 1),
					new ItemStack(TinkerTools.toolShard, 1, TinkerTools.MaterialID.Obsidian), new ItemStack(TGregworks.shardCast, 1, 0), 800, 30);
				GT_Values.RA.addExtruderRecipe(new ItemStack(TinkerTools.blankPattern, 1, 2),
					new ItemStack(TinkerTools.toolShard, 1, TinkerTools.MaterialID.Obsidian), new ItemStack(TGregworks.shardCast, 1, 0), 800, 30);
			}
			if(brassstack != null) {
				GT_Values.RA.addExtruderRecipe(brassstack,
					new ItemStack(TinkerTools.toolShard, 1, TinkerTools.MaterialID.Obsidian), new ItemStack(TGregworks.shardCast, 1, 0), 800, 30);
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

		addTGregToolRecipe(TinkerTools.pickaxe, PartTypes.PickaxeHead, PartTypes.ToolRod, PartTypes.Binding);
		addTGregToolRecipe(TinkerTools.shovel, PartTypes.ShovelHead, PartTypes.ToolRod);
		addTGregToolRecipe(TinkerTools.hatchet, PartTypes.AxeHead, PartTypes.ToolRod);
		addTGregToolRecipe(TinkerTools.mattock, PartTypes.PickaxeHead, PartTypes.ToolRod, PartTypes.ShovelHead);
		addTGregToolRecipe(TinkerTools.chisel, PartTypes.ChiselHead, PartTypes.ToolRod);

		addTGregToolRecipe(TinkerTools.broadsword, PartTypes.SwordBlade, PartTypes.ToolRod, PartTypes.LargeGuard);
		addTGregToolRecipe(TinkerTools.longsword, PartTypes.SwordBlade, PartTypes.ToolRod, PartTypes.MediumGuard);
		addTGregToolRecipe(TinkerTools.rapier, PartTypes.SwordBlade, PartTypes.ToolRod, PartTypes.Crossbar);
		addTGregToolRecipe(TinkerTools.dagger, PartTypes.KnifeBlade, PartTypes.ToolRod, PartTypes.Crossbar);
		addTGregToolRecipe(TinkerTools.cutlass, PartTypes.SwordBlade, PartTypes.ToolRod, PartTypes.FullGuard);
		addTGregToolRecipe(TinkerTools.frypan, PartTypes.FrypanHead, PartTypes.ToolRod);
		addTGregToolRecipe(TinkerTools.battlesign, PartTypes.SignHead, PartTypes.ToolRod);

		addTGregToolRecipe(TinkerTools.scythe, PartTypes.ScytheHead, PartTypes.ToughRod, PartTypes.ToughBind, PartTypes.ToughRod);
		addTGregToolRecipe(TinkerTools.lumberaxe, PartTypes.LumberHead, PartTypes.ToughRod, PartTypes.LargePlate, PartTypes.ToughBind);
		addTGregToolRecipe(TinkerTools.cleaver, PartTypes.LargeSwordBlade, PartTypes.ToughRod, PartTypes.LargePlate, PartTypes.ToughRod);
		addTGregToolRecipe(TinkerTools.excavator, PartTypes.ExcavatorHead, PartTypes.ToughRod, PartTypes.LargePlate, PartTypes.ToughBind);
		addTGregToolRecipe(TinkerTools.hammer, PartTypes.HammerHead, PartTypes.ToughRod, PartTypes.LargePlate, PartTypes.LargePlate);
		addTGregToolRecipe(TinkerTools.battleaxe, PartTypes.LumberHead, PartTypes.ToughRod, PartTypes.LumberHead, PartTypes.ToughBind);

		if(TConstruct.pulsar.isPulseLoaded("Tinkers' Weaponry")) {
			ToolBuilder.addCustomToolRecipe(new TGregBowRecipe(partMap.get(PartTypes.BowLimb), TinkerWeaponry.bowstring, partMap.get(PartTypes.BowLimb),
				TinkerWeaponry.shortbow));
			ToolBuilder.addCustomToolRecipe(new TGregBowRecipe(partMap.get(PartTypes.BowLimb), TinkerWeaponry.bowstring, partMap.get(PartTypes.BowLimb),
				partMap.get(PartTypes.LargePlate), TinkerWeaponry.longbow));
			ToolBuilder.addCustomToolRecipe(new TGregBowRecipe(partMap.get(PartTypes.CrossbowLimb), partMap.get(PartTypes.CrossbowBody), TinkerWeaponry.bowstring,
				partMap.get(PartTypes.ToughBind), TinkerWeaponry.crossbow));

			ToolBuilder.addCustomToolRecipe(new TGregAmmoRecipe(partMap.get(PartTypes.ArrowHead), partMap.get(PartTypes.ToolRod), TinkerWeaponry.fletching,
				TinkerWeaponry.arrowAmmo));
			ToolBuilder.addCustomToolRecipe(new TGregToolRecipe(partMap.get(PartTypes.Shuriken), partMap.get(PartTypes.Shuriken),
				partMap.get(PartTypes.Shuriken), partMap.get(PartTypes.Shuriken), TinkerWeaponry.shuriken));
			addTGregToolRecipe(TinkerWeaponry.throwingknife, PartTypes.KnifeBlade, PartTypes.ToolRod);
			addTGregToolRecipe(TinkerWeaponry.javelin, PartTypes.ArrowHead, PartTypes.ToughRod, PartTypes.ToughRod);
		}
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
