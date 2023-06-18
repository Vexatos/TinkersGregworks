package vexatos.tgregworks.integration.recipe.tconstruct;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;

import gregtech.api.enums.Materials;
import tconstruct.library.crafting.FluidType;

/**
 * @author Vexatos
 */
public class TGregFluidType extends FluidType {

    public final int matID;
    public final Materials material;

    public TGregFluidType(Materials m, Block block, int meta, int baseTemperature, Fluid fluid, boolean isToolpart,
            int matID) {
        super(block, meta, baseTemperature, fluid, isToolpart);
        this.material = m;
        this.matID = matID;
    }
}
