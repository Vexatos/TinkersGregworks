package vexatos.tgregworks.util;

import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import net.minecraft.item.ItemStack;

/**
 * @author Vexatos
 */
public interface ITGregElectricTool extends ISpecialElectricItem, IElectricItemManager {

	public long getRealCharge(ItemStack itemStack);

	public long getRealMaxCharge(ItemStack itemStack);
}
