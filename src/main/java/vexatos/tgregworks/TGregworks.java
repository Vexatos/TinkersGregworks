package vexatos.tgregworks;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tconstruct.TConstruct;
import tconstruct.library.TConstructCreativeTab;
import tconstruct.library.crafting.ModifyBuilder;
import tconstruct.tools.items.Pattern;
import vexatos.tgregworks.integration.TGregRecipeRegistry;
import vexatos.tgregworks.integration.TGregRegistry;
import vexatos.tgregworks.integration.TGregRepairRegistry;
import vexatos.tgregworks.integration.iguanatweakstconstruct.IntegrationITT;
import vexatos.tgregworks.integration.modifiers.ModTGregRepair;
import vexatos.tgregworks.integration.smeltery.CastLegacy;
import vexatos.tgregworks.integration.tictooltips.IntegrationTiCTooltips;
import vexatos.tgregworks.item.ShardPattern;
import vexatos.tgregworks.proxy.CommonProxy;
import vexatos.tgregworks.reference.Config;
import vexatos.tgregworks.reference.Mods;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.TGregUtils;

/**
 * @author Vexatos
 */
@Mod(modid = Mods.TGregworks, name = Mods.TGregworks_NAME, version = "GRADLETOKEN_VERSION",
	dependencies = "required-after:" + Mods.TConstruct + "@[1.7.10-1.8.6b.build977,);"
		+ "required-after:" + Mods.GregTech + "@[MC1710];"
		+ "before:" + Mods.TiCTooltips + "@[1.2.4,)")
public class TGregworks {

	public static Logger log = LogManager.getLogger(Mods.TGregworks);
	public static Configuration config;

	@Mod.Instance(value = Mods.TGregworks)
	public static TGregworks instance;

	@SidedProxy(clientSide = "vexatos.tgregworks.proxy.ClientProxy", serverSide = "vexatos.tgregworks.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static TGregRegistry registry;
	public static TGregRecipeRegistry recipes;
	public static TGregRepairRegistry repair;

	public static ShardPattern shardCast;

	public static IntegrationTiCTooltips ticTooltips;
	public static IntegrationITT iguanatweakstconstruct;
	//public static IntegrationTinkersTailor tinkersTailor;

	public static TConstructCreativeTab tab = new TConstructCreativeTab("tabTGregworks");

	public TGregworks() {
		if(Loader.isModLoaded(Mods.TConstruct)) {
			log.info("TConstruct detected, initializing Intergalactical integration...");
		}
		if(GregTech_API.VERSION < 509) {
			throw new RuntimeException("Your GregTech version is too old for this edition of the mod! It only works with GregTech 5 Unofficial. Please use the main version of TGregworks or switch to GT5U.");
		}
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		registry = new TGregRegistry();
		registry.registerItems();
		registry.registerToolParts();
		registry.registerModifiers();
		recipes = new TGregRecipeRegistry();
		repair = new TGregRepairRegistry();

		config.setCategoryComment(Config.Category.Global, "Values between 0.0 and 10000.0 are allowed. Will be directly multiplied with the internally calculated value. Applies to all materials.");
		config.setCategoryComment(Config.onMaterial(Config.Durability), "Values between 0.0 and 10000.0 are allowed. Will be directly multiplied with the internally calculated value.");
		config.setCategoryComment(Config.onMaterial(Config.MiningSpeed), "Values between 0.0 and 10000.0 are allowed. Will be directly multiplied with the internally calculated value.");
		config.setCategoryComment(Config.onMaterial(Config.Attack), "Values between 0.0 and 10000.0 are allowed. Will be directly multiplied with the internally calculated value.");
		config.setCategoryComment(Config.onMaterial(Config.HandleModifier), "Values between 0.0 and 10000.0 are allowed. Will be directly multiplied with the internally calculated value.");
		config.setCategoryComment(Config.onMaterial(Config.MaterialID), "Values between 0 and 100000 are allowed. The ID of each individual material. Setting an ID to 0 will regenerate it. When changing materialIDRangeStart, you might want to delete this category so the tool IDs can be regenerated.");
		config.setCategoryComment(Config.StoneboundLevel, "Values between -3 and 3 are allowed. Positive Values give the Stonebound effect, negative values give Jagged. "
			+ "Keep in mind that neither 'Stonebound' nor 'Jagged' will actually appear on the tool's item tooltip, due to technical limitations.");
		config.setCategoryComment(Config.ReinforcedLevel, "Values between 0 and 3 are allowed. Gives the according level of Reinforced.");
		config.setCategoryComment(Config.onMaterial(Config.BowDrawSpeed), "Values between 0.0 and 10000.0 are allowed. Will be directly multiplied with the internally calculated value.");
		config.setCategoryComment(Config.onMaterial(Config.BowFlightSpeed), "Values between 0.0 and 10000.0 are allowed. Will be directly multiplied with the internally calculated value.");
		config.setCategoryComment(Config.onMaterial(Config.ArrowMass), "Values between 0.0 and 10000.0 are allowed. Will be directly multiplied with the internally calculated value.");
		config.setCategoryComment(Config.onMaterial(Config.ArrowBreakChance), "Values between 0.0 and 10000.0 are allowed. Determines the break chance of arrows.");

		shardCast = new ShardPattern(tab);
		shardCast.setCreativeTab(tab).setUnlocalizedName("tgregworks.shardcast").setTextureName("tgregworks:cast_shard").setMaxDamage(0).setHasSubtypes(false).setMaxStackSize(1);
		GameRegistry.registerItem(shardCast, "tgregworks.shardcast");
		//shardCast = new Item().setCreativeTab(tab).setUnlocalizedName("tgregworks.shardcast").setTextureName("tgregworks:cast_shard").setMaxDamage(0).setHasSubtypes(false).setMaxStackSize(1);
		if(!TConstruct.pulsar.isPulseLoaded("Tinkers' Smeltery")) {
			CastLegacy.preInit();
		}

		{
			ItemStack stack = new ItemStack(registry.toolParts.get(PartTypes.LargeSwordBlade));
			NBTTagCompound data = TGregUtils.getTagCompound(stack);
			data.setString("material", Materials.Osmiridium.name());
			stack.setTagCompound(data);
			TGregworks.tab.init(stack);
		}

		//registry.registerTools();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.addToolRenderMappings();
		registry.registerFluids();
		proxy.registerRenderers();

		if(Loader.isModLoaded(Mods.IguanaTweaksTConstruct)) {
			(iguanatweakstconstruct = new IntegrationITT()).init();
		}
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		recipes.addRecipesForToolBuilder();
		recipes.addGregTechPartRecipes();
		recipes.registerCastRecipes();
		recipes.registerRepairMaterials();
		recipes.registerBoltRecipes();

		/*if(Loader.isModLoaded(Mods.TinkersTailor)) {
			tinkersTailor = new IntegrationTinkersTailor();
			tinkersTailor.registerArmorPartRecipes();
		}*/
		if(Loader.isModLoaded(Mods.TiCTooltips)) {
			ticTooltips = new IntegrationTiCTooltips();
			ticTooltips.postInit();
		}

		if(Loader.isModLoaded(Mods.IguanaTweaksTConstruct)) {
			iguanatweakstconstruct.postInit();
		} else {
			ModifyBuilder.registerModifier(new ModTGregRepair());
		}
		config.save();
	}

	@EventHandler
	public void remap(FMLMissingMappingsEvent e) {
		CastLegacy.remap(e);
	}
}
