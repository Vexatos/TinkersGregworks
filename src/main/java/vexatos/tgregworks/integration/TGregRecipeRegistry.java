package vexatos.tgregworks.integration;

import cpw.mods.fml.common.registry.GameRegistry;
import gregapi.data.CS.OreDictToolNames;
import gregapi.data.MT;
import gregapi.data.OP;
import gregapi.data.RM;
import gregapi.oredict.OreDictManager;
import gregapi.oredict.OreDictMaterial;
import gregapi.recipes.Recipe.RecipeMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tconstruct.TConstruct;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.CastingRecipe;
import tconstruct.library.crafting.FluidType;
import tconstruct.library.crafting.LiquidCasting;
import tconstruct.library.crafting.PatternBuilder;
import tconstruct.library.crafting.ToolBuilder;
import tconstruct.library.tools.ToolCore;
import tconstruct.library.tools.ToolMaterial;
import tconstruct.library.util.IToolPart;
import tconstruct.smeltery.TinkerSmeltery;
import tconstruct.tools.TinkerTools;
import tconstruct.util.config.PHConstruct;
import tconstruct.weaponry.TinkerWeaponry;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.integration.recipe.tconstruct.TGregAlternateBoltRecipe;
import vexatos.tgregworks.integration.recipe.tconstruct.TGregAmmoRecipe;
import vexatos.tgregworks.integration.recipe.tconstruct.TGregBowRecipe;
import vexatos.tgregworks.integration.recipe.tconstruct.TGregFluidType;
import vexatos.tgregworks.integration.recipe.tconstruct.TGregToolRecipe;
import vexatos.tgregworks.item.ItemTGregPart;
import vexatos.tgregworks.reference.Config;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.TGregUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gregapi.data.CS.T;

/**
 * @author SlimeKnights, Vexatos
 */
public class TGregRecipeRegistry {

	private HashMap<PartTypes, ItemTGregPart> partMap = new HashMap<PartTypes, ItemTGregPart>();

	//public boolean addReverseSmelting = false;
	public boolean addShardToIngotSmelting = false;
	public boolean addIngotToShard = false;
	public boolean addShardToToolPart = false;
	public boolean addExtruderRecipes = false;
	//public boolean addSolidifierRecipes = false; TODO Fluid Solidifier Recipes
	public boolean addShardRepair = true;
	public boolean addIngotRepair = false;
	public boolean addGemToolPartRecipes = true;
	public boolean useNonGTFluidsForBolts = true;
	public boolean useNonGTToolRodsForBolts = true;
	public float energyMultiplier = 0F;

	public void addGregTechPartRecipes() {
		/*addReverseSmelting = TGregworks.config.get(Config.concat(Config.Category.Enable, Config.Category.Recipes), "reverseSmelting",
			true, "Enable smelting tool parts in an alloy smelter to get shards back").getBoolean(true);*/
		addShardToIngotSmelting = TGregworks.config.get(Config.concat(Config.Category.Enable, Config.Category.Recipes), "shardToIngotSmelting",
			true, "Enable smelting two shards into one ingot in an alloy smelter").getBoolean(true);
		addIngotToShard = TGregworks.config.get(Config.concat(Config.Category.Enable, Config.Category.Recipes), "ingotToShardRecipe",
			true, "Enable creating shards from ingots in the extruder (if extruder is enabled)").getBoolean(true);
		/*addShardToToolPart = TGregworks.config.get(Config.concat(Config.Category.Enable, Config.Category.Recipes), "shardToToolPartRecipe",
			true, "Enable creating tool parts from shards in the extruder (if extruder is enabled)").getBoolean(true);*/
		addExtruderRecipes = TGregworks.config.get(Config.concat(Config.Category.Enable, Config.Category.Recipes), "extruderRecipes",
			true, "Enable tool part recipes in the extruder").getBoolean(true);
		/*addSolidifierRecipes = TGregworks.config.get(Config.concat(Config.Category.Enable, Config.Category.Recipes), "solidifierRecipes", TODO Fluid Solidifier Recipes
			false, "Enable tool part recipes in the fluid solidifier").getBoolean(false);*/
		addGemToolPartRecipes = TGregworks.config.get(Config.concat(Config.Category.Enable, Config.Category.Recipes), "gemToolPartRecipes",
			true, "Enable recipes for tool parts made of gems").getBoolean(true);
		useNonGTFluidsForBolts = TGregworks.config.getBoolean("useNonGTFluidsForBolts", Config.concat(Config.Category.Enable, Config.Category.Recipes), true,
			"Register Fluid Solidifier recipes for bolts with non-GT fluids.");
		useNonGTToolRodsForBolts = TGregworks.config.getBoolean("useNonGTToolRodsForBolts", Config.concat(Config.Category.Enable, Config.Category.Recipes), true,
			"Register Fluid Solidifier recipes for bolts with tool rods from non-GT materials.");
		energyMultiplier = TGregworks.config.getFloat("energyUsageMultiplier", Config.concat(Config.Category.General),
			1F, 0F, 4500F, "Energy usage multiplier for the extruder and solidifier. Base EU/t is either 30 or 120");

		//Make sure eu/t isn't 0 or the higher end materials eu/t does not exceed ultimate voltage
		if(energyMultiplier < 0 || (120 * energyMultiplier) > 524288) {
			TGregworks.log.error("Invalid energy multiplier found in config: " + energyMultiplier + ". Reverting back to 1.");
			energyMultiplier = 1;
		}
		for(OreDictMaterial m : TGregworks.registry.toolMaterials) {
			final int powerRequired = getPowerRequired(m);
			for(PartTypes p : PartTypes.VALUES) {
				ItemStack input = TGregUtils.newItemStack(m, p, 1);
				ItemStack pattern = p.getPatternItem();
				if(pattern != null) {
					int price = p.getPrice();
					//GregTech_API.sRecipeAdder.addAlloySmelterRecipe(GT_OreDictUnificator.get(OrePrefixes.ingot, m, p.price), p.pattern, input, 80 * p.price, 30);
					ItemStack stack = OP.ingot.mat(m,
						price % 2 != 0 ? (price / 2) + 1 : MathHelper.ceiling_double_int(price / 2D));
					if(addGemToolPartRecipes && stack == null) {
						stack = OP.gem.mat(m,
							price % 2 != 0 ? (price / 2) + 1 : MathHelper.ceiling_double_int(price / 2D));
					}
					if(stack != null) {
						if(addExtruderRecipes) {
							RM.Extruder.addRecipe2(T, powerRequired, Math.max(80, m.mToolDurability * price),
								stack.copy(), pattern.copy(), input.copy());
						}
						/*if(addSolidifierRecipes) {
							FluidStack molten = m.liquid((CS.U / 2) * p.getPrice(), false);
							if(molten != null && molten.getFluid() != null) {
								RA.addFluidSolidifierRecipe(pattern.copy(), molten, input.copy(), Math.max(80, m.mToolDurability * price),
									powerRequired); TODO Fluid Solidifier recipe
							}
							//GregTech_API.sRecipeAdder.addAlloySmelterRecipe(getChunk(m, p.price), p.pattern, input, 80 * p.price, 30);
						}*/
						stack = getChunk(m, price);
						if(stack != null) {
							if(addExtruderRecipes && addShardToToolPart) {
								RM.Extruder.addRecipe2(T, powerRequired, 80 + (m.mToolDurability * price),
									stack.copy(), pattern.copy(), input.copy());
							}
							/*if(addReverseSmelting) { Handled by GT 6 recycling
								RA.addAlloySmelterRecipe(input.copy(), new ItemStack(TGregworks.shardCast, 0, 0), stack.copy(), 80 + (m.mToolDurability * price),
									powerRequired);
							}*/
						}
					}
				}
			}
			ItemStack stack = getChunk(m, 2);
			ItemStack ingotStack = OP.ingot.mat(m, 1);
			if(addGemToolPartRecipes && ingotStack == null) {
				ingotStack = OP.gem.mat(m, 1);
			}
			if(stack != null && ingotStack != null) {
				if(addExtruderRecipes && addIngotToShard) {
					RM.Extruder.addRecipe2(T, powerRequired, Math.max(160, m.mToolDurability),
						ingotStack, new ItemStack(TGregworks.shardCast, 0, 0), stack);
				}
				/*if(addShardToIngotSmelting) { Handled by GT 6 recycling
					RecipeMap.sAlloySmelterRecipes.addRecipe2(T, powerRequired, Math.max(160, m.mToolDurability),
						stack.copy(), new ItemStack(MetalPatterns.ingot.getPatternItem(), 0, MetalPatterns.ingot.ordinal()), ingotStack.copy());
				}*/
			}
		}

		if(TGregworks.config.get(Config.concat(Config.Category.Enable, Config.Category.Recipes), "tinkersconstructcastrecipe", true, "Enable the Shard Cast recipe using Tinkers' Construct shards").getBoolean(true)) {
			ItemStack brassstack = OP.plate.mat(MT.Brass, 1);
			if(TinkerTools.toolShard != null) {
			/*ArrayList list = new ArrayList();
			TinkerTools.toolShard.getSubItems(TinkerTools.toolShard, TinkerTools.toolShard.getCreativeTab(), list);
			for(Object o : list) {
				if(o instanceof ItemStack) {
					GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes.add(new GT_Recipe(alustack, (ItemStack) o, 30, 800, new ItemStack(TGregworks.shardCast, 1, 0)));
				}
			}*/
				if(TinkerTools.blankPattern != null) {
					RM.Extruder.addRecipe2(T, Math.round(30 * energyMultiplier), 800, new ItemStack(TinkerTools.blankPattern, 1, 1),
						new ItemStack(TinkerTools.toolShard, 1, TinkerTools.MaterialID.Obsidian), new ItemStack(TGregworks.shardCast, 1, 0));
					RM.Extruder.addRecipe2(T, Math.round(30 * energyMultiplier), 800, new ItemStack(TinkerTools.blankPattern, 1, 2),
						new ItemStack(TinkerTools.toolShard, 1, TinkerTools.MaterialID.Obsidian), new ItemStack(TGregworks.shardCast, 1, 0));
				}
				if(brassstack != null) {
					RM.Extruder.addRecipe2(T, Math.round(30 * energyMultiplier), 800, brassstack,
						new ItemStack(TinkerTools.toolShard, 1, TinkerTools.MaterialID.Obsidian), new ItemStack(TGregworks.shardCast, 1, 0));
				}
			}
		}
		if(TGregworks.config.get(Config.concat(Config.Category.Enable, Config.Category.Recipes), "gregtechcastrecipe", true, "Enable the GregTech style Shard Cast recipe").getBoolean(true)) {
			GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(TGregworks.shardCast, 1, 0),
				" CH",
				" PF",
				"   ",
				'C', OreDictToolNames.hammer, 'H', OreDictToolNames.wirecutter, 'F', OreDictToolNames.file, 'P', "plateBrass"
			));
		}
	}

	private int getPowerRequired(OreDictMaterial m) {
		return Math.round(m.mToolQuality < 3 ? (30 * energyMultiplier) : (120 * energyMultiplier));
	}

	private ItemStack getChunk(OreDictMaterial m, int amount) {
		return TGregUtils.newItemStack(m, PartTypes.Chunk, amount);
	}

	public void registerRepairMaterials() {
		addShardRepair = TGregworks.config.getBoolean("addShardRepair", Config.concat(Config.Category.Enable, Config.Category.Recipes), true, "Allow repairing TGregworks tools with shards");
		addIngotRepair = TGregworks.config.getBoolean("addIngotRepair", Config.concat(Config.Category.Enable, Config.Category.Recipes), false, "Allow repairing TGregworks tools with ingots");
		for(OreDictMaterial m : TGregworks.registry.toolMaterials) {
			Integer matID = TGregworks.registry.matIDs.get(m);
			if(matID != null) {
				ToolMaterial mat = TConstructRegistry.getMaterial(matID);
				if(mat != null) {
					if(addShardRepair) {
						ItemStack shard = TGregUtils.newItemStack(m, PartTypes.Chunk, 1);
						if(PatternBuilder.instance.materialSets.containsKey(mat.materialName)) {
							PatternBuilder.instance.registerMaterial(shard, 1, mat.materialName);
						} else {
							ItemStack rod = TGregUtils.newItemStack(m, PartTypes.ToolRod, 1);

							// register the material
							PatternBuilder.instance.registerFullMaterial(shard, 1, mat.materialName, shard, rod, matID);
						}
						TGregworks.repair.registerShardRepairMaterial(m, 1);
					}
					if(addIngotRepair) {
						List<ItemStack> ingots = OreDictManager.getOres(OP.ingot, m, false);
						if(!ingots.isEmpty()) {
							TGregworks.repair.registerOreDictRepairMaterial(m, OP.ingot.dat(m).toString(), 2);
						} else if(addGemToolPartRecipes) {
							ingots.addAll(OreDictManager.getOres(OP.gem, m, false));
							TGregworks.repair.registerOreDictRepairMaterial(m, OP.gem.dat(m).toString(), 2);
						}
						for(ItemStack ingot : ingots) {
							if(ingot != null && ingot.getItem() != null) {
								if(PatternBuilder.instance.materialSets.containsKey(mat.materialName)) {
									PatternBuilder.instance.registerMaterial(ingot, 1, mat.materialName);
								} else {
									ItemStack rod = TGregUtils.newItemStack(m, PartTypes.ToolRod, 1);

									// register the material
									PatternBuilder.instance.registerFullMaterial(ingot, 2, mat.materialName, ingot, rod, matID);
								}
							}
						}
					}
				}
			}
		}
	}

	public void registerBoltRecipes() {
		if(!TConstruct.pulsar.isPulseLoaded("Tinkers' Weaponry")) {
			return;
		}
		if(PHConstruct.alternativeBoltRecipe) {
			GameRegistry.addRecipe(new TGregAlternateBoltRecipe());
		}
		if(!TConstruct.pulsar.isPulseLoaded("Tinkers' Smeltery")) {
			return;
		}
		LiquidCasting tb = TConstructRegistry.getTableCasting();

		// any fluid that is a toolpart material can be used
		for(Map.Entry<String, FluidType> fluidEntry : FluidType.fluidTypes.entrySet()) {
			if(!fluidEntry.getValue().isToolpart) {
				continue;
			}
			FluidStack liquid = new FluidStack(fluidEntry.getValue().fluid, TConstruct.ingotLiquidValue);
			int arrowheadMaterialID;
			if(fluidEntry.getValue() instanceof TGregFluidType) {
				arrowheadMaterialID = ((TGregFluidType) fluidEntry.getValue()).matID;
			} else {
				// get a casting recipe for it D:
				CastingRecipe recipe = tb.getCastingRecipe(liquid, new ItemStack(TinkerSmeltery.metalPattern, 1, 2)); // pickaxe

				if(recipe == null) {
					continue;
				} else {
					arrowheadMaterialID = recipe.getResult().getItemDamage();
				}
			}
			for(Integer toolRodMaterialID : TConstructRegistry.toolMaterials.keySet()) {
				ItemStack toolRod;
				if(TGregworks.registry.materialIDMap.containsKey(toolRodMaterialID)) {
					toolRod = TGregUtils.newItemStack(TGregworks.registry.materialIDMap.get(toolRodMaterialID), PartTypes.ToolRod, 1);
				} else {
					toolRod = new ItemStack(TinkerTools.toolRod, 1, toolRodMaterialID);
					if(((IToolPart) TinkerTools.toolRod).getMaterialID(toolRod) == -1) {
						continue;
					}
				}
				if(!TGregworks.registry.materialIDMap.containsKey(toolRodMaterialID) &&
					!(fluidEntry.getValue() instanceof TGregFluidType)) {
					continue;
				}
				if((useNonGTToolRodsForBolts || TGregworks.registry.materialIDMap.containsKey(toolRodMaterialID)) &&
					(useNonGTFluidsForBolts || fluidEntry.getValue() instanceof TGregFluidType)) {
					this.addBoltRecipe(toolRod, liquid.copy(), toolRodMaterialID, arrowheadMaterialID);
				}
			}
		}

		// Remove broken dynamically added recipes.
		ArrayList<CastingRecipe> castingRecipes = TConstructRegistry.getTableCasting().getCastingRecipes();
		ArrayList<CastingRecipe> toRemove = new ArrayList<CastingRecipe>();
		for(CastingRecipe cr : castingRecipes) {
			if(cr != null && cr.cast != null && cr.cast.getItem() == TinkerTools.toolRod
				&& TGregworks.registry.materialIDMap.containsKey(((IToolPart) TinkerTools.toolRod).getMaterialID(cr.cast))) {
				toRemove.add(cr);
			}
		}
		castingRecipes.removeAll(toRemove);
	}

	private int getPowerRequired(ToolMaterial toolMaterial) {
		return Math.round(toolMaterial.harvestLevel < 3 ? (30 * energyMultiplier) : (120 * energyMultiplier));
	}

	private void addBoltRecipe(ItemStack toolRod, FluidStack fluid, int toolRodMaterialID, int arrowheadMaterialID) {
		ToolMaterial toolRodMaterial = TConstructRegistry.toolMaterials.get(toolRodMaterialID);
		ToolMaterial arrowheadMaterial = TConstructRegistry.toolMaterials.get(arrowheadMaterialID);
		if(toolRodMaterial != null && arrowheadMaterial != null) {
			/*RA.addFluidSolidifierRecipe( TODO Fluid solidifier recipes
				toolRod,
				fluid,
				DualMaterialToolPart.createDualMaterial(TinkerWeaponry.partBolt, toolRodMaterialID, arrowheadMaterialID),
				80 + (toolRodMaterial.durability + arrowheadMaterial.durability) * 2,
				Math.max(getPowerRequired(toolRodMaterial), getPowerRequired(arrowheadMaterial))
			);*/
		}
	}

	public void addRecipesForToolBuilder() {

		for(PartTypes p : PartTypes.VALUES) {
			partMap.put(p, TGregworks.registry.toolParts.get(p));
		}

		addTGregToolRecipe(TinkerTools.pickaxe, PartTypes.PickaxeHead, PartTypes.ToolRod, PartTypes.Binding);
		addTGregToolRecipe(TinkerTools.shovel, PartTypes.ShovelHead, PartTypes.ToolRod);
		addTGregToolRecipe(TinkerTools.hatchet, PartTypes.AxeHead, PartTypes.ToolRod);
		addTGregToolRecipe(TinkerTools.mattock, PartTypes.AxeHead, PartTypes.ToolRod, PartTypes.ShovelHead);
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
