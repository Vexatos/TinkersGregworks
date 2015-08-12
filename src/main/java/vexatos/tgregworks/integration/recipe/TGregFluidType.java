package vexatos.tgregworks.integration.recipe;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import tconstruct.library.crafting.FluidType;

/**
 * @author Vexatos
 */
public class TGregFluidType extends FluidType {
	public final int matID;

	public TGregFluidType(Block block, int meta, int baseTemperature, Fluid fluid, boolean isToolpart, int matID) {
		super(block, meta, baseTemperature, fluid, isToolpart);
		this.matID = matID;
	}
}
