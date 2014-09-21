package vexatos.tgregworks.integration;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.ModifyBuilder;
import tconstruct.library.tools.ToolCore;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.item.ItemTGregPart;
import vexatos.tgregworks.item.entity.TGregDaggerEntity;
import vexatos.tgregworks.modifiers.TGregBatteryModifier;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.tools.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Vexatos
 */
public class TGregRegistry {

	private int latestAvailableNumber = 300;

	public ArrayList<Materials> toolMaterials = new ArrayList<Materials>();
	public ArrayList<String> toolMaterialNames = new ArrayList<String>();
	public HashMap<Materials, Integer> matIDs = new HashMap<Materials, Integer>();

	private int getLatestAvailableNumber() {
		for(int i = latestAvailableNumber; true; i++) {
			if(!TConstructRegistry.toolMaterials.containsKey(i)) {
				latestAvailableNumber = i + 1;
				return i;
			}
		}
	}

	public void registerToolParts() {
		TGregworks.log.info("Registering TGregworks tool parts.");
		for(Materials m : Materials.values()) {
			if(((m.mTypes & 64) == 64) && !doesMaterialExist(m) && Arrays.asList(GregTech_API.sGeneratedMaterials).contains(m)) {
				toolMaterials.add(m);
			}
		}
		for(Materials m : toolMaterials) {
			toolMaterialNames.add(m.mDefaultLocalName);
			int matID = getLatestAvailableNumber();
			TConstructRegistry.addToolMaterial(matID, m.name(), m.mDefaultLocalName, m.mToolQuality, m.mDurability, (int) (m.mToolSpeed * 100F), (int) m.mToolQuality, (float) m.mToolQuality - 0.5F, getReinforcedLevel(m), getStoneboundLevel(m), "", (m.getRGBA()[0] << 16) | (m.getRGBA()[1] << 8) | (m.getRGBA()[2]));
			TConstructRegistry.addBowMaterial(matID, m.mDurability, (int) (m.mToolQuality * 10F), ((float) m.mToolQuality) - 0.5F);
			TConstructRegistry.addArrowMaterial(matID, (float) (((double) m.getMass()) / 10F), 0.2F, 99F);
			matIDs.put(m, matID);
		}

		ItemTGregPart.toolMaterialNames = toolMaterialNames;
		ItemTGregPart.matIDs = matIDs;
	}

	public static ToolCore pickaxe;
	public static ToolCore shovel;
	public static ToolCore hatchet;
	public static ToolCore broadsword;
	public static ToolCore longsword;
	public static ToolCore rapier;
	public static ToolCore dagger;
	public static ToolCore cutlass;
	public static ToolCore frypan;
	public static ToolCore battlesign;
	public static ToolCore chisel;
	public static ToolCore mattock;
	public static ToolCore scythe;
	public static ToolCore lumberaxe;
	public static ToolCore cleaver;
	public static ToolCore hammer;
	public static ToolCore excavator;
	public static ToolCore battleaxe;
	public static ToolCore shortbow;
	public static ToolCore arrow;
	public static TGregBatteryModifier batteryModifier;

	public void registerTools() {
		TGregRegistry.pickaxe = new Pickaxe();
		TGregRegistry.shovel = new Shovel();
		TGregRegistry.hatchet = new Hatchet();
		TGregRegistry.broadsword = new Broadsword();
		TGregRegistry.longsword = new Longsword();
		TGregRegistry.rapier = new Rapier();
		TGregRegistry.dagger = new Dagger();
		TGregRegistry.cutlass = new Cutlass();

		TGregRegistry.frypan = new FryingPan();
		TGregRegistry.battlesign = new BattleSign();
		TGregRegistry.mattock = new Mattock();
		TGregRegistry.chisel = new Chisel();

		TGregRegistry.lumberaxe = new LumberAxe();
		TGregRegistry.cleaver = new Cleaver();
		TGregRegistry.scythe = new Scythe();
		TGregRegistry.excavator = new Excavator();
		TGregRegistry.hammer = new Hammer();
		TGregRegistry.battleaxe = new Battleaxe();

		TGregRegistry.shortbow = new Shortbow();
		TGregRegistry.arrow = new Arrow();

		Item[] tools = { TGregRegistry.pickaxe, TGregRegistry.shovel, TGregRegistry.hatchet, TGregRegistry.broadsword, TGregRegistry.longsword, TGregRegistry.rapier, TGregRegistry.dagger,
			TGregRegistry.cutlass, TGregRegistry.frypan, TGregRegistry.battlesign, TGregRegistry.mattock, TGregRegistry.chisel, TGregRegistry.lumberaxe, TGregRegistry.cleaver, TGregRegistry.scythe,
			TGregRegistry.excavator, TGregRegistry.hammer, TGregRegistry.battleaxe, TGregRegistry.shortbow, TGregRegistry.arrow };
		String[] toolStrings = { "pickaxe", "shovel", "hatchet", "broadsword", "longsword", "rapier", "dagger", "cutlass", "frypan", "battlesign", "mattock", "chisel", "lumberaxe", "cleaver",
			"scythe", "excavator", "hammer", "battleaxe", "shortbow", "arrow" };

		for(int i = 0; i < tools.length; i++) {
			GameRegistry.registerItem(tools[i], toolStrings[i]); // totally not stolen from TiC
			TConstructRegistry.addItemToDirectory(toolStrings[i], tools[i]);
		}

		EntityRegistry.registerModEntity(TGregDaggerEntity.class, "Dagger", 1, TGregworks.instance, 32, 5, true);

		TGregRegistry.batteryModifier = new TGregBatteryModifier();

		registerModifierRecipe(Materials.Basic);
		registerModifierRecipe(Materials.Advanced);
		registerModifierRecipe(Materials.Elite);
		registerModifierRecipe(Materials.Master);
		registerModifierRecipe(Materials.Ultimate);

		registerModifierRecipe(ItemList.ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Primitive, 1));

		TGregRegistry.batteryModifier.singleuse.add(ItemList.ZPM.get(1));
		ModifyBuilder.registerModifier(TGregRegistry.batteryModifier);

		modifierCharges.put(10000L, 200);
		modifierCharges.put(100000L, 400);
		modifierCharges.put(1000000L, 2000);
		modifierCharges.put(10000000L, 10000);
		modifierCharges.put(100000000L, 50000);
		modifierCharges.put(1000000000000L, 31250000);
	}

	public HashMap<Long, Integer> modifierCharges = new HashMap<Long, Integer>();

	private static void registerModifierRecipe(Materials material) {
		TGregRegistry.batteryModifier.batteries.put(GT_OreDictUnificator.get(OrePrefixes.battery, material, 1),
			GT_OreDictUnificator.get(OrePrefixes.circuit, material, 1));
	}

	private static void registerModifierRecipe(ItemStack stack1, ItemStack stack2) {
		TGregRegistry.batteryModifier.batteries.put(stack1, stack2);
	}

	private List<Materials> stonebound1Mats = Arrays.asList(Materials.Titanium, Materials.Tungsten);
	private List<Materials> spiny1Mats = Arrays.asList(Materials.Uranium, Materials.Uranium235);

	private float getStoneboundLevel(Materials m) {
		if(stonebound1Mats.contains(m)) {
			return 1;
		}
		if(spiny1Mats.contains(m)) {
			return -1;
		}
		return 0;
	}

	private List<Materials> reinforced1Mats = Arrays.asList(Materials.SteelMagnetic, Materials.BlackSteel, Materials.BlueSteel, Materials.Titanium, Materials.DamascusSteel, Materials.StainlessSteel, Materials.RedSteel, Materials.MeteoricSteel, Materials.TungstenSteel);
	private List<Materials> reinforced2Mats = Arrays.asList(Materials.Osmium, Materials.Iridium);

	private int getReinforcedLevel(Materials m) {
		if(reinforced1Mats.contains(m)) {
			return 1;
		}
		if(reinforced2Mats.contains(m)) {
			return 2;
		}
		if(m == Materials.Osmiridium) {
			return 3;
		}
		return 0;
	}

	private boolean doesMaterialExist(Materials m) {
		return TConstructRegistry.toolMaterialStrings.containsKey(m.name());
		// && Arrays.asList(GregTech_API.sGeneratedMaterials).contains(m);
	}

	public HashMap<PartTypes, ItemTGregPart> toolParts = new HashMap<PartTypes, ItemTGregPart>();

	public void registerItems() {
		for(PartTypes p : PartTypes.values()) {
			ItemTGregPart item = new ItemTGregPart(p);
			toolParts.put(p, item);
			GameRegistry.registerItem(item, "tGregToolPart" + item.getType().name());
		}
	}
}
