package vexatos.tgregworks.integration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import net.minecraftforge.common.config.Property;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.FluidType;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.integration.recipe.tconstruct.TGregFluidType;
import vexatos.tgregworks.item.ItemTGregPart;
import vexatos.tgregworks.reference.Config;
import vexatos.tgregworks.reference.PartTypes;

/**
 * @author Vexatos
 */
public class TGregRegistry {

    private int latestAvailableNumber = 1500;
    private boolean addMaterialsAnyway = false;

    public ArrayList<Materials> toolMaterials = new ArrayList<Materials>();
    public ArrayList<String> toolMaterialNames = new ArrayList<String>();
    public HashMap<Materials, Integer> matIDs = new HashMap<Materials, Integer>();
    public HashMap<Integer, Materials> materialIDMap = new HashMap<Integer, Materials>();

    private int getLatestAvailableNumber() {
        for (int i = latestAvailableNumber; i < 16383; i++) {
            if (!TConstructRegistry.toolMaterials.containsKey(i) && !configIDs.contains(i)) {
                latestAvailableNumber = i + 1;
                return i;
            }
        }
        throw new RuntimeException("TConstruct tool material registry ran out of IDs!");
    }

    public final HashMap<Materials, Property> configProps = new HashMap<>();
    public final ArrayList<Integer> configIDs = new ArrayList<>();

    public int getMaterialID(Materials m) {
        Property configProp = configProps.get(m);
        if (configProp == null) {
            configProp = TGregworks.config.get(Config.onMaterial(Config.MaterialID), m.mName, 0, null, 0, 30000);
        }
        final int configID = configProp.getInt();
        if (configID > 0) {
            return configID;
        }

        final int newID = getLatestAvailableNumber();
        configProp.set(newID);
        return newID;
    }

    public TGregRegistry() {
        latestAvailableNumber = TGregworks.config.getInt(
                "materialIDRangeStart",
                Config.Category.General,
                1500,
                300,
                15000,
                "The lowest ID for TGregworks materials. Only material IDs higher than this will be used, and only if the ID has not been registered before. Other mods might not check if the material ID is already in use and thus crash, if the crash occurs with a TGregworks material, changing this number may fix it.");
        addMaterialsAnyway = TGregworks.config.getBoolean(
                "addMaterialsAnyway",
                Config.Category.General,
                false,
                "Register Materials even if a material with the same name already exists. May override any material with the same name added by other mods.");
    }

    public void registerToolParts() {
        TGregworks.log.info("Registering TGregworks tool parts.");
        List<Materials> gtMaterials = Arrays.asList(GregTech_API.sGeneratedMaterials);
        for (Materials m : Materials.values()) {
            if (((m.mTypes & 64) == 64) && !doesMaterialExist(m)
                    && gtMaterials.contains(m)
                    && TGregworks.config.get(Config.Category.Enable, m.mName, true).getBoolean(true)) {
                toolMaterials.add(m);
                Property configProp = TGregworks.config
                        .get(Config.onMaterial(Config.MaterialID), m.mName, 0, null, 0, 100000);
                configProps.put(m, configProp);
                configIDs.add(configProp.getInt());
            }
        }
        for (Materials m : toolMaterials) {
            toolMaterialNames.add(m.mDefaultLocalName);
            int matID = getMaterialID(m);
            addToolMaterial(matID, m);
            addBowMaterial(matID, m);
            addArrowMaterial(matID, m);
            matIDs.put(m, matID);
            materialIDMap.put(matID, m);
        }
        configProps.clear();
        configIDs.clear();

        ItemTGregPart.toolMaterialNames = toolMaterialNames;
    }

    public void addToolMaterial(int matID, Materials m) {
        TConstructRegistry.addToolMaterial(
                matID,
                m.mName,
                m.mLocalizedName,
                m.mToolQuality,
                (int) (m.mDurability * getGlobalMultiplier(Config.Durability) * getMultiplier(m, Config.Durability)), // Durability
                (int) (m.mToolSpeed * 100F
                        * getGlobalMultiplier(Config.MiningSpeed)
                        * getMultiplier(m, Config.MiningSpeed)), // Mining speed
                (int) (m.mToolQuality * getGlobalMultiplier(Config.Attack) * getMultiplier(m, Config.Attack)), // Attack
                (sanitizeToolQuality(m.mToolQuality) - 0.5F) * getGlobalMultiplier(Config.HandleModifier)
                        * getMultiplier(m, Config.HandleModifier), // Handle Modifier
                getReinforcedLevel(m),
                getStoneboundLevel(m),
                "",
                (m.getRGBA()[0] << 16) | (m.getRGBA()[1] << 8) | (m.getRGBA()[2]));
    }

    public void addBowMaterial(int matID, Materials m) {
        TConstructRegistry.addBowMaterial(
                matID,
                (int) (sanitizeToolQuality(m.mToolQuality) * 10F
                        * getGlobalMultiplier(Config.BowDrawSpeed)
                        * getMultiplier(m, Config.BowDrawSpeed)),
                (sanitizeToolQuality(m.mToolQuality) - 0.5F) * getGlobalMultiplier(Config.BowFlightSpeed)
                        * getMultiplier(m, Config.BowFlightSpeed));
    }

    public void addArrowMaterial(int matID, Materials m) {
        TConstructRegistry.addArrowMaterial(
                matID,
                (float) ((((double) m.getMass()) / 10F) * getGlobalMultiplier(Config.ArrowMass)
                        * getMultiplier(m, Config.ArrowMass)),
                getGlobalMultiplier(Config.ArrowBreakChance, 0.9) * getMultiplier(m, Config.ArrowBreakChance));
    }

    /**
     * {@link Materials#mToolQuality} may be 0, which can be problematic for some stats.
     */
    private static int sanitizeToolQuality(int toolQuality) {
        return Math.max(toolQuality, 1);
    }

    public HashMap<Materials, TGregFluidType> toolMaterialFluidTypes = new HashMap<Materials, TGregFluidType>();

    public void registerFluids() {
        for (Materials m : toolMaterials) {
            if (m.mStandardMoltenFluid != null) {
                TGregFluidType fluidType = new TGregFluidType(
                        m,
                        m.mStandardMoltenFluid.getBlock(),
                        0,
                        m.mStandardMoltenFluid.getTemperature(),
                        m.mStandardMoltenFluid,
                        true,
                        matIDs.get(m));

                toolMaterialFluidTypes.put(m, fluidType);
                FluidType.registerFluidType(m.mStandardMoltenFluid.getName(), fluidType);
            }
        }
    }

    public float getMultiplier(Materials m, String key) {
        return (float) TGregworks.config.get(Config.onMaterial(key), m.mName, 1.0, null, 0, 10000).getDouble(1.0);
    }

    private final HashMap<String, Float> globalMultipliers = new HashMap<String, Float>();

    public float getGlobalMultiplier(String key) {
        return getGlobalMultiplier(key, 1.0);
    }

    public float getGlobalMultiplier(String key, double def) {
        Float multiplier = globalMultipliers.get(key);
        if (multiplier == null) {
            multiplier = (float) TGregworks.config.get(Config.Category.Global, key, def, null, 0, 10000).getDouble(def);
            globalMultipliers.put(key, multiplier);
        }
        return multiplier;
    }

    /*
     * public static ToolCore pickaxe; public static ToolCore shovel; public static ToolCore hatchet; public static
     * ToolCore broadsword; public static ToolCore longsword; public static ToolCore rapier; public static ToolCore
     * dagger; public static ToolCore cutlass; public static ToolCore frypan; public static ToolCore battlesign; public
     * static ToolCore chisel; public static ToolCore mattock; public static ToolCore scythe; public static ToolCore
     * lumberaxe; public static ToolCore cleaver; public static ToolCore hammer; public static ToolCore excavator;
     * public static ToolCore battleaxe; public static ToolCore shortbow; public static ToolCore arrow;
     */
    // public static TGregBatteryModifier batteryModifier;

    /*
     * public void registerTools() { TGregRegistry.pickaxe = new Pickaxe(); TGregRegistry.shovel = new Shovel();
     * TGregRegistry.hatchet = new Hatchet(); TGregRegistry.broadsword = new Broadsword(); TGregRegistry.longsword = new
     * Longsword(); TGregRegistry.rapier = new Rapier(); TGregRegistry.dagger = new Dagger(); TGregRegistry.cutlass =
     * new Cutlass(); TGregRegistry.frypan = new FryingPan(); TGregRegistry.battlesign = new BattleSign();
     * TGregRegistry.mattock = new Mattock(); TGregRegistry.chisel = new Chisel(); TGregRegistry.lumberaxe = new
     * LumberAxe(); TGregRegistry.cleaver = new Cleaver(); TGregRegistry.scythe = new Scythe(); TGregRegistry.excavator
     * = new Excavator(); TGregRegistry.hammer = new Hammer(); TGregRegistry.battleaxe = new Battleaxe();
     * TGregRegistry.shortbow = new Shortbow(); TGregRegistry.arrow = new Arrow(); Item[] tools = {
     * TGregRegistry.pickaxe, TGregRegistry.shovel, TGregRegistry.hatchet, TGregRegistry.broadsword,
     * TGregRegistry.longsword, TGregRegistry.rapier, TGregRegistry.dagger, TGregRegistry.cutlass, TGregRegistry.frypan,
     * TGregRegistry.battlesign, TGregRegistry.mattock, TGregRegistry.chisel, TGregRegistry.lumberaxe,
     * TGregRegistry.cleaver, TGregRegistry.scythe, TGregRegistry.excavator, TGregRegistry.hammer,
     * TGregRegistry.battleaxe, TGregRegistry.shortbow, TGregRegistry.arrow }; String[] toolStrings = { "pickaxe",
     * "shovel", "hatchet", "broadsword", "longsword", "rapier", "dagger", "cutlass", "frypan", "battlesign", "mattock",
     * "chisel", "lumberaxe", "cleaver", "scythe", "excavator", "hammer", "battleaxe", "shortbow", "arrow" }; for(int i
     * = 0; i < tools.length; i++) { GameRegistry.registerItem(tools[i], toolStrings[i]); // totally not stolen from TiC
     * TConstructRegistry.addItemToDirectory(toolStrings[i], tools[i]); }
     * EntityRegistry.registerModEntity(TGregDaggerEntity.class, "Dagger", 1, TGregworks.instance, 32, 5, true);
     * TGregRegistry.batteryModifier = new TGregBatteryModifier(); registerModifierRecipe(Materials.Basic);
     * registerModifierRecipe(Materials.Advanced); registerModifierRecipe(Materials.Elite);
     * registerModifierRecipe(Materials.Master); registerModifierRecipe(Materials.Ultimate);
     * registerModifierRecipe(ItemList.ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Primitive,
     * 1)); TGregRegistry.batteryModifier.singleuse.add(ItemList.ZPM.get(1));
     * ModifyBuilder.registerModifier(TGregRegistry.batteryModifier); modifierCharges.put(10000L, 200);
     * modifierCharges.put(100000L, 400); modifierCharges.put(1000000L, 2000); modifierCharges.put(10000000L, 10000);
     * modifierCharges.put(100000000L, 50000); modifierCharges.put(1000000000000L, 31250000); } public HashMap<Long,
     * Integer> modifierCharges = new HashMap<Long, Integer>(); private static void registerModifierRecipe(Materials
     * material) { TGregRegistry.batteryModifier.batteries.put(GT_OreDictUnificator.get(OrePrefixes.battery, material,
     * 1), GT_OreDictUnificator.get(OrePrefixes.circuit, material, 1)); } private static void
     * registerModifierRecipe(ItemStack stack1, ItemStack stack2) { TGregRegistry.batteryModifier.batteries.put(stack1,
     * stack2); }
     */

    private List<Materials> stonebound1Mats = Arrays.asList(Materials.Titanium, Materials.Tungsten);
    private List<Materials> spiny1Mats = Arrays.asList(Materials.Uranium, Materials.Uranium235);

    public float getStoneboundLevel(Materials m) {
        return (float) TGregworks.config.get(
                Config.StoneboundLevel,
                m.mName,
                stonebound1Mats.contains(m) ? 1 : spiny1Mats.contains(m) ? -1 : 0,
                null,
                -3,
                3).getInt(stonebound1Mats.contains(m) ? 1 : spiny1Mats.contains(m) ? -1 : 0);
    }

    private List<Materials> reinforced1Mats = Arrays.asList(
            Materials.SteelMagnetic,
            Materials.BlackSteel,
            Materials.BlueSteel,
            Materials.Titanium,
            Materials.DamascusSteel,
            Materials.StainlessSteel,
            Materials.RedSteel,
            Materials.MeteoricSteel,
            Materials.TungstenSteel);
    private List<Materials> reinforced2Mats = Arrays.asList(Materials.Osmium, Materials.Iridium);

    public int getReinforcedLevel(Materials m) {
        return TGregworks.config.get(
                Config.ReinforcedLevel,
                m.mName,
                reinforced1Mats.contains(m) ? 1 : reinforced2Mats.contains(m) ? 2 : m == Materials.Osmiridium ? 3 : 0,
                null,
                0,
                3).getInt(
                        reinforced1Mats.contains(m) ? 1
                                : reinforced2Mats.contains(m) ? 2 : m == Materials.Osmiridium ? 3 : 0);
    }

    private boolean doesMaterialExist(Materials m) {
        return !addMaterialsAnyway && TConstructRegistry.toolMaterialStrings.containsKey(m.mName);
        // && Arrays.asList(GregTech_API.sGeneratedMaterials).contains(m);
    }

    public HashMap<PartTypes, ItemTGregPart> toolParts = new HashMap<PartTypes, ItemTGregPart>();

    public void registerItems() {
        for (PartTypes p : PartTypes.VALUES) {
            ItemTGregPart item = new ItemTGregPart(p);
            toolParts.put(p, item);
            GameRegistry.registerItem(item, "tGregToolPart" + item.getType().name());
        }
    }

    public void registerModifiers() {
        // ModifyBuilder.registerModifier(new ModifierTGregRepair());
    }
}
