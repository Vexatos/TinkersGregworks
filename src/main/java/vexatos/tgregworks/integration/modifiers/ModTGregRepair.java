package vexatos.tgregworks.integration.modifiers;

import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import tconstruct.library.crafting.PatternBuilder;
import tconstruct.library.modifier.IModifyable;
import tconstruct.library.modifier.ItemModifier;
import tconstruct.library.tools.AbilityHelper;
import tconstruct.library.tools.ToolCore;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.integration.TGregRepairRegistry.RepairMaterial;

/**
 * @author Vexatos
 */
public class ModTGregRepair extends ItemModifier {

    public ModTGregRepair() {
        super(new ItemStack[0], 0, "TGregRepair");
    }

    @Override
    public boolean matches(ItemStack[] input, ItemStack tool) {
        return canModify(tool, input);
    }

    @Override
    protected boolean canModify(ItemStack tool, ItemStack[] input) {
        NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
        if (tags.getInteger("Damage") > 0) {
            int headID = tags.getInteger("Head");
            boolean areInputsValid = true;
            for (ItemStack curInput : input) {
                if (curInput != null && headID != PatternBuilder.instance.getPartID(curInput)
                        && !isValidRepairMaterial(curInput, headID)) {
                    areInputsValid = false;
                    break;
                }
            }
            if (areInputsValid) {
                return calculateIfNecessary(tool, input, headID);
            }
        }
        return false;
    }

    private boolean calculateIfNecessary(ItemStack tool, ItemStack[] input, int materialID) {
        NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
        int damage = tags.getInteger("Damage");
        int numInputs = 0;
        int materialValue = 0;
        for (ItemStack curInput : input) {
            if (curInput != null) {
                int partValue = PatternBuilder.instance.getPartValue(curInput);
                materialValue += partValue != 0 ? partValue : getRepairAmount(curInput, materialID);
                numInputs++;
            }
        }
        if (numInputs == 0) {
            return false;
        }

        int totalRepairValue = calculateIncrease(tool, materialValue, numInputs);
        float averageRepairValue = totalRepairValue / numInputs;

        return numInputs == 1 || (damage - totalRepairValue >= -averageRepairValue);
    }

    private boolean isValidRepairMaterial(ItemStack input, int materialID) {
        Set<RepairMaterial> repairMaterials = TGregworks.repair.repairMaterials
                .get(TGregworks.registry.materialIDMap.get(materialID));
        for (RepairMaterial mat : repairMaterials) {
            if (mat.matches(input)) {
                return true;
            }
        }
        return false;
    }

    private int getRepairAmount(ItemStack input, int materialID) {
        Set<RepairMaterial> repairMaterials = TGregworks.repair.repairMaterials
                .get(TGregworks.registry.materialIDMap.get(materialID));
        for (RepairMaterial mat : repairMaterials) {
            if (mat.matches(input)) {
                return mat.value;
            }
        }
        return 0;
    }

    private int calculateIncrease(ItemStack tool, int materialValue, int itemsUsed) {
        NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
        int damage = tags.getInteger("Damage");
        int dur = tags.getInteger("BaseDurability");
        int increase = (int) (50 * itemsUsed + (dur * 0.4f * materialValue));

        int modifiers = tags.getInteger("Modifiers");
        float mods = 1.0f;
        if (modifiers == 2) {
            mods = 0.9f;
        } else if (modifiers == 1) {
            mods = 0.8f;
        } else if (modifiers == 0) {
            mods = 0.7f;
        }

        increase *= mods;

        int repair = tags.getInteger("RepairCount");
        float repairCount = (100 - repair) / 100f;
        if (repairCount < 0.5f) {
            repairCount = 0.5f;
        }
        increase *= repairCount;
        increase /= ((ToolCore) tool.getItem()).getRepairCost();
        return increase;
    }

    @Override
    public void modify(ItemStack[] input, ItemStack tool) {
        NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
        tags.setBoolean("Broken", false);
        int damage = tags.getInteger("Damage");
        int headID = tags.getInteger("Head");
        int dur = tags.getInteger("BaseDurability");
        int itemsUsed = 0;

        int materialValue = 0;
        for (ItemStack curInput : input) {
            if (curInput != null) {
                int partValue = PatternBuilder.instance.getPartValue(curInput);
                materialValue += partValue != 0 ? partValue : getRepairAmount(curInput, headID);
                itemsUsed++;
            }
        }

        int increase = calculateIncrease(tool, materialValue, itemsUsed);
        int repair = tags.getInteger("RepairCount");
        repair += itemsUsed;
        tags.setInteger("RepairCount", repair);

        damage -= increase;
        if (damage < 0) {
            damage = 0;
        }
        tags.setInteger("Damage", damage);

        AbilityHelper.damageTool(tool, 0, null, true);
    }

    @Override
    public void addMatchingEffect(ItemStack tool) {}

    public boolean validType(IModifyable input) {
        return input.getModifyType().equals("Tool");
    }
}
