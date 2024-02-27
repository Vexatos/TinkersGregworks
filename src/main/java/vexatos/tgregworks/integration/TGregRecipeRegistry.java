package vexatos.tgregworks.integration;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import tconstruct.TConstruct;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.*;
import tconstruct.library.tools.DualMaterialToolPart;
import tconstruct.library.tools.ToolCore;
import tconstruct.library.tools.ToolMaterial;
import tconstruct.library.util.IToolPart;
import tconstruct.smeltery.TinkerSmeltery;
import tconstruct.tools.TinkerTools;
import tconstruct.util.config.PHConstruct;
import tconstruct.weaponry.TinkerWeaponry;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.integration.recipe.tconstruct.*;
import vexatos.tgregworks.integration.smeltery.CastLegacy;
import vexatos.tgregworks.item.ItemTGregPart;
import vexatos.tgregworks.reference.Config;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.reference.Pattern.MetalPatterns;
import vexatos.tgregworks.util.TGregUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static gregtech.api.recipe.RecipeCategories.alloySmelterRecycling;
import static gregtech.api.recipe.RecipeCategories.ticBoltMolding;
import static gregtech.api.recipe.RecipeCategories.ticPartExtruding;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;

/**
 * @author SlimeKnights, Vexatos
 */
public class TGregRecipeRegistry {

	private final HashMap<PartTypes, ItemTGregPart> partMap = new HashMap<>();

	public boolean addReverseSmelting = false;
	public boolean addShardToIngotSmelting = false;
	public boolean addIngotToShard = false;
	public boolean addMoltenToShard = false;
	public boolean addShardToToolPart = false;
	public boolean addExtruderRecipes = false;
	public boolean addSolidifierRecipes = false;
	public boolean addFluidExtractorRecipes = false;
	public boolean addShardExtractorRecipes = false;
	public boolean addShardRepair = true;
	public boolean addIngotRepair = false;
	public boolean addGemToolPartRecipes = true;
	public boolean addCastExtruderRecipes = true;
	public boolean addCastSolidifierRecipes = false;
	public boolean useNonGTFluidsForBolts = true;
	public boolean useNonGTToolRodsForBolts = true;
	public float energyMultiplier = 0F;

	public void addGregTechPartRecipes() {
		addGemToolPartRecipes = TGregworks.config.getBoolean("gemToolPartRecipes", Config.concat(Config.Category.Enable, Config.Category.Recipes),
			true, "Enable recipes for tool parts made of gems");
		energyMultiplier = TGregworks.config.getFloat("energyUsageMultiplier", Config.concat(Config.Category.General),
			1F, 0F, 4500F, "Energy usage multiplier for the extruder and solidifier. Base EU/t is either 30 or 120");

		addReverseSmelting = TGregworks.config.getBoolean("reverseSmelting", Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.AlloySmelter),
			true, "Enable smelting tool parts in an alloy smelter to get shards back");
		addShardToIngotSmelting = TGregworks.config.getBoolean("shardToIngotSmelting", Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.AlloySmelter),
			true, "Enable smelting two shards into one ingot in an alloy smelter");

		addExtruderRecipes = TGregworks.config.getBoolean("extruderRecipes", Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Extruder),
			true, "Enable tool part recipes in the extruder");
		addShardToToolPart = TGregworks.config.getBoolean("shardToToolPartRecipe", Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Extruder),
			true, "Enable creating tool parts from shards in the extruder (if 'extruderRecipes' is enabled)");
		addIngotToShard = TGregworks.config.getBoolean("ingotToShardRecipe", Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Extruder),
			true, "Enable creating shards from ingots in the extruder");
		addCastExtruderRecipes = TGregworks.config.getBoolean("castExtruderRecipes", Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Extruder),
			CastLegacy.metalPattern != null, "Enable creating tool part casts in the extruder");

		addSolidifierRecipes = TGregworks.config.getBoolean("solidifierRecipes", Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Solidifier),
			false, "Enable tool part recipes in the fluid solidifier");
		addMoltenToShard = TGregworks.config.getBoolean("moltenToShardRecipe", Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Solidifier),
			false, "Enable creating shards from molten material in the fluid solidifier");
		addCastSolidifierRecipes = TGregworks.config.getBoolean("castSolidifierRecipes", Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Solidifier),
			false, "Enable creating tool part casts in the fluid solidifier");
		useNonGTFluidsForBolts = TGregworks.config.getBoolean("useNonGTFluidsForBolts", Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Solidifier), true,
			"Register Fluid Solidifier recipes for bolts with non-GT fluids.");
		useNonGTToolRodsForBolts = TGregworks.config.getBoolean("useNonGTToolRodsForBolts", Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Solidifier), true,
			"Register Fluid Solidifier recipes for bolts with tool rods from non-GT materials.");

		addFluidExtractorRecipes = TGregworks.config.getBoolean("fluidExtractorRecipes", Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Extractor),
			false, "Enable extracting the molten material out of tool parts in the fluid extractor");
		addShardExtractorRecipes = TGregworks.config.getBoolean("shardExtractorRecipes", Config.concat(Config.Category.Enable, Config.Category.Recipes, Config.Category.Extractor),
			false, "Enable extracting the molten material out of shards in the fluid extractor");

		//Make sure eu/t isn't 0 or the higher end materials eu/t does not exceed ultimate voltage
		if(energyMultiplier < 0 || (120 * energyMultiplier) > 524288) {
			TGregworks.log.error("Invalid energy multiplier found in config: " + energyMultiplier + ". Reverting back to 1.");
			energyMultiplier = 1;
		}
		for(Materials m : TGregworks.registry.toolMaterials) {
			final int powerRequired = getPowerRequired(m);
			for(PartTypes p : PartTypes.VALUES) {
				ItemStack input = TGregUtils.newItemStack(m, p, 1);
				ItemStack pattern = p.getPatternItem();
                if (pattern == null) continue;
                int price = p.getPrice();
                int itemAmount = price % 2 != 0 ? (price / 2) + 1 : MathHelper.ceiling_double_int(price / 2D);
                ItemStack stack = GT_OreDictUnificator.get(OrePrefixes.ingot, m, itemAmount);
                if (addGemToolPartRecipes && stack == null) {
                    stack = GT_OreDictUnificator.get(OrePrefixes.gem, m, itemAmount);
                }
                if (stack == null) continue;
                if (addExtruderRecipes) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(stack.copy(), pattern.copy())
                        .itemOutputs(input.copy())
                        .duration(Math.max(80, m.mDurability * price))
                        .eut(powerRequired)
                        .recipeCategory(ticPartExtruding)
                        .addTo(extruderRecipes);
                }
                {
                    FluidStack molten = m.getMolten((GT_Values.L / 2) * p.getPrice());
                    if (molten != null && molten.getFluid() != null) {
                        if (addSolidifierRecipes) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(pattern.copy())
                                .fluidInputs(molten.copy())
                                .itemOutputs(input.copy())
                                .duration(Math.max(80, m.mDurability * price))
                                .eut(powerRequired)
                                .addTo(fluidSolidifierRecipes);
                        }
                        if (addFluidExtractorRecipes) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(input.copy())
                                .fluidOutputs(molten.copy())
                                .duration(Math.max(80, m.mDurability * price))
                                .eut(powerRequired)
                                .addTo(fluidExtractionRecipes);
                        }
                    }
                }
                stack = getChunk(m, price);
                if (addExtruderRecipes && addShardToToolPart) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(stack.copy(), pattern.copy())
                        .itemOutputs(input.copy())
                        .duration(80 + (m.mDurability * price))
                        .eut(powerRequired)
                        .recipeCategory(ticPartExtruding)
                        .addTo(extruderRecipes);
                }
                if (addReverseSmelting) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(input.copy(), new ItemStack(TGregworks.shardCast, 0, 0))
                        .itemOutputs(stack.copy())
                        .duration(80 + (m.mDurability * price))
                        .eut(powerRequired)
                        .recipeCategory(alloySmelterRecycling)
                        .addTo(alloySmelterRecipes);
                }
            }
			ItemStack stack = getChunk(m, 2);
			ItemStack ingotStack = GT_OreDictUnificator.get(OrePrefixes.ingot, m, 1);
			if(addGemToolPartRecipes && ingotStack == null) {
				ingotStack = GT_OreDictUnificator.get(OrePrefixes.gem, m, 1);
			}
            if (ingotStack == null) continue;
            if (addIngotToShard) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(ingotStack, new ItemStack(TGregworks.shardCast, 0, 0))
                    .itemOutputs(stack.copy())
                    .duration(Math.max(160, m.mDurability))
                    .eut(powerRequired)
                    .recipeCategory(ticPartExtruding)
                    .addTo(extruderRecipes);
            }
            ItemStack halfStack = stack.copy();
            halfStack.stackSize = 1;
            FluidStack molten = m.getMolten(GT_Values.L / 2);
            if (molten != null && molten.getFluid() != null) {
                if (addMoltenToShard) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(new ItemStack(TGregworks.shardCast, 0, 0))
                        .fluidInputs(molten.copy())
                        .itemOutputs(halfStack.copy())
                        .duration(Math.max(160, m.mDurability))
                        .eut(powerRequired)
                        .addTo(fluidSolidifierRecipes);
                }
                if (addShardExtractorRecipes) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(halfStack.copy())
                        .fluidOutputs(molten.copy())
                        .duration(Math.max(160, m.mDurability))
                        .eut(powerRequired)
                        .addTo(fluidExtractionRecipes);
                }
            }
            if (addShardToIngotSmelting) {
                GT_Values.RA.stdBuilder()
                    .itemInputs(stack.copy(), new ItemStack(MetalPatterns.ingot.getPatternItem(), 0, MetalPatterns.ingot.ordinal()))
                    .itemOutputs(ingotStack.copy())
                    .duration(Math.max(160, m.mDurability))
                    .eut(powerRequired)
                    .recipeCategory(alloySmelterRecycling)
                    .addTo(alloySmelterRecipes);
            }
        }

		if(TGregworks.config.getBoolean("tinkersconstructcastrecipe", Config.concat(Config.Category.Enable, Config.Category.Recipes), true, "Enable the Shard Cast recipe using Tinkers' Construct shards")) {
			ItemStack brassstack = GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Brass, 1);
			if(TinkerTools.toolShard != null) {
				if(TinkerTools.blankPattern != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(new ItemStack(TinkerTools.blankPattern, 1, 1),
                            new ItemStack(TinkerTools.toolShard, 1, TinkerTools.MaterialID.Obsidian))
                        .itemOutputs(new ItemStack(TGregworks.shardCast, 1, 0))
                        .duration(800)
                        .eut(Math.round(30 * energyMultiplier))
                        .recipeCategory(ticPartExtruding)
                        .addTo(extruderRecipes);
                    GT_Values.RA.stdBuilder()
                        .itemInputs(new ItemStack(TinkerTools.blankPattern, 1, 2),
                            new ItemStack(TinkerTools.toolShard, 1, TinkerTools.MaterialID.Obsidian))
                        .itemOutputs(new ItemStack(TGregworks.shardCast, 1, 0))
                        .duration(800)
                        .eut(Math.round(30 * energyMultiplier))
                        .recipeCategory(ticPartExtruding)
                        .addTo(extruderRecipes);
				}
				if(brassstack != null && !addCastExtruderRecipes) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(brassstack, new ItemStack(TinkerTools.toolShard, 1, TinkerTools.MaterialID.Obsidian))
                        .itemOutputs(new ItemStack(TGregworks.shardCast, 1, 0))
                        .duration(800)
                        .eut(Math.round(30 * energyMultiplier))
                        .recipeCategory(ticPartExtruding)
                        .addTo(extruderRecipes);
				}
			}
		}
		if(TGregworks.config.getBoolean("gregtechcastrecipe", Config.concat(Config.Category.Enable, Config.Category.Recipes), true, "Enable the GregTech style Shard Cast recipe")) {
			GameRegistry.addRecipe(new ShapedOreRecipe(
				new ItemStack(TGregworks.shardCast, 1, 0),
				" CH",
				" PF",
				"   ",
				'C', ToolDictNames.craftingToolHardHammer.name(), 'H', ToolDictNames.craftingToolKnife.name(), 'F', ToolDictNames.craftingToolFile.name(), 'P', "plateBrass"
			));
		}
	}

	private int getPowerRequired(Materials m) {
		return Math.round(m.mToolQuality < 3 ? (30 * energyMultiplier) : (120 * energyMultiplier));
	}

    @Nonnull
	private ItemStack getChunk(Materials m, int amount) {
		return TGregUtils.newItemStack(m, PartTypes.Chunk, amount);
	}

	public void registerRepairMaterials() {
		addShardRepair = TGregworks.config.getBoolean("addShardRepair", Config.concat(Config.Category.Enable, Config.Category.Recipes), true, "Allow repairing TGregworks tools with shards");
		addIngotRepair = TGregworks.config.getBoolean("addIngotRepair", Config.concat(Config.Category.Enable, Config.Category.Recipes), false, "Allow repairing TGregworks tools with ingots");
		for(Materials m : TGregworks.registry.toolMaterials) {
			Integer matID = TGregworks.registry.matIDs.get(m);
            if (matID == null) continue;
            ToolMaterial mat = TConstructRegistry.getMaterial(matID);
            if (mat == null) continue;
            if (addShardRepair) {
                ItemStack shard = TGregUtils.newItemStack(m, PartTypes.Chunk, 1);
                if (PatternBuilder.instance.materialSets.containsKey(mat.materialName)) {
                    PatternBuilder.instance.registerMaterial(shard, 1, mat.materialName);
                } else {
                    ItemStack rod = TGregUtils.newItemStack(m, PartTypes.ToolRod, 1);

                    // register the material
                    PatternBuilder.instance.registerFullMaterial(shard, 1, mat.materialName, shard, rod, matID);
                }
                TGregworks.repair.registerShardRepairMaterial(m, 1);
            }
            if (addIngotRepair) {
                ArrayList<ItemStack> ingots = GT_OreDictUnificator.getOres(OrePrefixes.ingot, m);
                if (!ingots.isEmpty()) {
                    TGregworks.repair.registerOreDictRepairMaterial(m, OrePrefixes.ingot.get(m).toString(), 2);
                } else if (addGemToolPartRecipes) {
                    ingots.addAll(GT_OreDictUnificator.getOres(OrePrefixes.gem, m));
                    TGregworks.repair.registerOreDictRepairMaterial(m, OrePrefixes.gem.get(m).toString(), 2);
                }
                for (ItemStack ingot : ingots) {
                    if (ingot == null || ingot.getItem() == null) continue;
                    if (PatternBuilder.instance.materialSets.containsKey(mat.materialName)) {
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
		ArrayList<CastingRecipe> toRemove = new ArrayList<>();
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
            GT_Values.RA.stdBuilder()
                .itemInputs(toolRod)
                .fluidInputs(fluid)
                .itemOutputs(DualMaterialToolPart.createDualMaterial(TinkerWeaponry.partBolt, toolRodMaterialID, arrowheadMaterialID))
                .duration(80 + (toolRodMaterial.durability + arrowheadMaterial.durability) * 2)
                .eut(Math.max(getPowerRequired(toolRodMaterial), getPowerRequired(arrowheadMaterial)))
                .recipeCategory(ticBoltMolding)
                .addTo(fluidSolidifierRecipes);
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

			{
				TGregAmmoRecipe arrowRecipe = new TGregAmmoRecipe(partMap.get(PartTypes.ArrowHead), partMap.get(PartTypes.ToolRod), TinkerWeaponry.fletching,
					TinkerWeaponry.arrowAmmo);
				arrowRecipe.addHandleItem(TinkerWeaponry.partArrowShaft);
				ToolBuilder.addCustomToolRecipe(arrowRecipe);
			}
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

	public void registerCastRecipes() {
		Materials[] castingMaterials = new Materials[] { Materials.Brass, Materials.Gold };

		if(addCastExtruderRecipes) {
			for(PartTypes p : PartTypes.VALUES) {
				ItemStack stack = p.getPatternItem();
				if(stack != null && stack.getItem() != null) {
					for(Materials m : castingMaterials) {
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, m, 1), new ItemStack(p.getCounterpart(), 0, Short.MAX_VALUE))
                            .itemOutputs(stack.copy())
                            .duration(800)
                            .eut(getPowerRequired(m))
                            .recipeCategory(ticPartExtruding)
                            .addTo(extruderRecipes);
                        GT_Values.RA.stdBuilder()
                            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, m, 1), new ItemStack(TGregworks.registry.toolParts.get(p), 0, Short.MAX_VALUE))
                            .itemOutputs(stack.copy())
                            .duration(800)
                            .eut(getPowerRequired(m))
                            .recipeCategory(ticPartExtruding)
                            .addTo(extruderRecipes);
					}
				}
			}
			for(Materials m : castingMaterials) {
				FluidStack molten = m.getMolten(GT_Values.L);
				if(molten != null && molten.getFluid() != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(GT_OreDictUnificator.get(OrePrefixes.plate, m, 1), new ItemStack(TGregworks.registry.toolParts.get(PartTypes.Chunk), 0, Short.MAX_VALUE))
                        .itemOutputs(new ItemStack(TGregworks.shardCast, 1, 0))
                        .duration(800)
                        .eut(getPowerRequired(m))
                        .recipeCategory(ticPartExtruding)
                        .addTo(extruderRecipes);
				}
			}
		}
		if(addCastSolidifierRecipes) {
			for(PartTypes p : PartTypes.VALUES) {
				ItemStack stack = p.getPatternItem();
				if(stack != null && stack.getItem() != null && p.getCounterpart() != null) {
					stack.stackSize = 1;
					for(Materials m : castingMaterials) {
						FluidStack molten = m.getMolten(GT_Values.L);
						if(molten != null && molten.getFluid() != null) {
                            GT_Values.RA.stdBuilder()
                                .itemInputs(new ItemStack(p.getCounterpart(), 0, Short.MAX_VALUE))
                                .fluidInputs(molten.copy())
                                .itemOutputs(stack.copy())
                                .duration(800)
                                .eut(getPowerRequired(m))
                                .addTo(fluidSolidifierRecipes);
                            GT_Values.RA.stdBuilder()
                                .itemInputs(new ItemStack(TGregworks.registry.toolParts.get(p), 0, Short.MAX_VALUE))
                                .fluidInputs(molten.copy())
                                .itemOutputs(stack.copy())
                                .duration(800)
                                .eut(getPowerRequired(m))
                                .addTo(fluidSolidifierRecipes);
						}
					}
				}
			}
			for(Materials m : castingMaterials) {
				FluidStack molten = m.getMolten(GT_Values.L);
				if(molten != null && molten.getFluid() != null) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(new ItemStack(TGregworks.registry.toolParts.get(PartTypes.Chunk), 0, Short.MAX_VALUE))
                        .fluidInputs(molten.copy())
                        .itemOutputs(new ItemStack(TGregworks.shardCast, 1, 0))
                        .duration(800)
                        .eut(getPowerRequired(m))
                        .addTo(fluidSolidifierRecipes);
				}
			}
		}
	}
}
