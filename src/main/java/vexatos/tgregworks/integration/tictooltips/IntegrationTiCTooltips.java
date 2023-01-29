package vexatos.tgregworks.integration.tictooltips;

import static squeek.tictooltips.helpers.ToolPartHelper.*;
import static vexatos.tgregworks.reference.PartTypes.*;

import java.util.List;

import net.minecraft.item.Item;

import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.reference.Mods;
import vexatos.tgregworks.reference.PartTypes;
import cpw.mods.fml.common.Optional;

/**
 * @author Vexatos
 */
public class IntegrationTiCTooltips {

    @Optional.Method(modid = Mods.TiCTooltips)
    public void postInit() {
        TGregworks.log.info("Adding TiCTooltips integration...");
        try {
            add(toolHeads, PickaxeHead, ShovelHead, ExcavatorHead);
            add(weaponMiningHeads, HammerHead);
            add(weaponToolHeads, AxeHead, ScytheHead, LumberHead);
            add(weaponHeads, SwordBlade, LargeSwordBlade, KnifeBlade, FrypanHead, SignHead);
            add(weaponGuards, Crossbar, MediumGuard, LargeGuard);
            add(fullWeaponGuards, FullGuard);
            add(bindings, Binding);
            add(toughBindings, ToughBind);
            add(rods, ToolRod, ToughRod);
            add(plates, LargePlate);
            add(shards, Chunk);
            add(arrowHeads, ArrowHead);
            add(chisels, ChiselHead);
            add(shurikenParts, Shuriken);
            add(bowLimbs, BowLimb);
            add(crossbowLimbs, CrossbowLimb);
            add(crossbowBodies, CrossbowBody);
            TGregworks.log.info("Successfully added TiCTooltips integration.");
        } catch (Exception e) {
            TGregworks.log.error("Failed adding TiCTooltips integration.");
            e.printStackTrace();
        }
    }

    @Optional.Method(modid = Mods.TiCTooltips)
    private void add(List<Item> toolPartList, PartTypes... parts) {
        try {
            for (PartTypes part : parts) {
                toolPartList.add(TGregworks.registry.toolParts.get(part));
            }
        } catch (Exception e) {
            TGregworks.log.error("Failed adding parts of TiCTooltips integration.");
            e.printStackTrace();
        }
    }
}
