package vexatos.tgregworks.integration.iguanatweakstconstruct;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import vexatos.tgregworks.integration.modifiers.ModTGregRepair;
import iguanaman.iguanatweakstconstruct.reference.Config;

/**
 * Copied from IguanaTweaksTConstruct to support that mod's limited tool repair config option. All credits go to their
 * authors.
 */
public class ModLimitedTGregRepair extends ModTGregRepair {

    @Override
    protected boolean canModify(ItemStack tool, ItemStack[] input) {
        NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
        int repairCount = tags.getInteger("RepairCount");
        return repairCount < Config.maxToolRepairs && super.canModify(tool, input);
    }
}
