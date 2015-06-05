package vexatos.tgregworks;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.api.enums.Materials;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tconstruct.library.TConstructCreativeTab;
import vexatos.tgregworks.integration.TGregRecipeRegistry;
import vexatos.tgregworks.integration.TGregRegistry;
import vexatos.tgregworks.proxy.CommonProxy;
import vexatos.tgregworks.reference.Mods;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.BuildingHandler;
import vexatos.tgregworks.util.TGregUtils;

/**
 * @author Vexatos
 */
@Mod(modid = Mods.TGregworks, name = Mods.TGregworks_NAME, version = "@VERSION@",
	dependencies = "required-after:" + Mods.TConstruct + "@[1.7.10-1.8.5,);required-after:" + Mods.GregTech + "@[MC1710]")
public class TGregworks {

	public static Logger log = LogManager.getLogger(Mods.TGregworks);
	public Configuration config;

	@Mod.Instance(value = Mods.TGregworks)
	public static TGregworks instance;

	@SidedProxy(clientSide = "vexatos.tgregworks.proxy.ClientProxy", serverSide = "vexatos.tgregworks.proxy.CommonProxy")
	public static CommonProxy proxy;

	public static TGregRegistry registry;
	public static TGregRecipeRegistry recipes;

	public static TConstructCreativeTab tab = new TConstructCreativeTab("tabTGregworks");

	public TGregworks() {
		if(Loader.isModLoaded(Mods.TConstruct)) {
			log.info("TConstruct detected, initializing Intergalactical integration...");
		}
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		config.save();

		registry = new TGregRegistry();
		registry.registerToolParts();
		recipes = new TGregRecipeRegistry();

		registry.registerItems();

		{
			ItemStack stack = new ItemStack(registry.toolParts.get(PartTypes.LargeSwordBlade));
			NBTTagCompound data = TGregUtils.getTagCompound(stack);
			data.setString("material", Materials.Osmiridium.name());
			stack.setTagCompound(data);
			TGregworks.tab.init(stack);
		}

		//registry.registerTools();

		MinecraftForge.EVENT_BUS.register(new BuildingHandler());
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.addToolRenderMappings();
		recipes.addRecipesForToolBuilder();
		recipes.addGregTechPartRecipes();
		proxy.registerRenderers();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {

	}
}
