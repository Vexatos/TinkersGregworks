package vexatos.tgregworks.integration.smeltery;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import tconstruct.library.TConstructRegistry;
import tconstruct.smeltery.TinkerSmeltery;
import tconstruct.smeltery.items.MetalPattern;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.reference.Mods;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * @author Vexatos
 */
public class CastLegacy {

    public static Item metalPattern;

    public static void preInit() {
        TGregworks.log.info("TConstruct Smeltery module disabled; adding own tool part casts.");
        metalPattern = new MetalPattern("cast_", "materials/").setUnlocalizedName("tconstruct.MetalPattern");
        GameRegistry.registerItem(metalPattern, "metalPattern");
        String[] patternTypes = { "ingot", "toolRod", "pickaxeHead", "shovelHead", "hatchetHead", "swordBlade",
                "wideGuard", "handGuard", "crossbar", "binding", "frypanHead", "signHead", "knifeBlade", "chiselHead",
                "toughRod", "toughBinding", "largePlate", "broadAxeHead", "scytheHead", "excavatorHead", "largeBlade",
                "hammerHead", "fullGuard" };
        for (int i = 0; i < patternTypes.length; i++) {
            TConstructRegistry.addItemStackToDirectory(patternTypes[i] + "Cast", new ItemStack(metalPattern, 1, i));
        }
    }

    public static void remap(FMLMissingMappingsEvent e) {
        if (TinkerSmeltery.metalPattern == null) {
            return;
        }
        for (FMLMissingMappingsEvent.MissingMapping mapping : e.get()) {
            if (mapping.name.equalsIgnoreCase(Mods.TGregworks + ":metalPattern")) {
                if (mapping.type == GameRegistry.Type.ITEM) {
                    mapping.remap(TinkerSmeltery.metalPattern);
                }
            }
        }
    }
}
