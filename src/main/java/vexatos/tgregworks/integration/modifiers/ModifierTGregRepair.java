package vexatos.tgregworks.integration.modifiers;

import gregtech.api.enums.Materials;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tconstruct.library.crafting.PatternBuilder;
import tconstruct.library.tools.AbilityHelper;
import tconstruct.library.tools.ToolCore;
import tconstruct.modifiers.tools.ModToolRepair;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.TGregUtils;

/**
 * @author Vexatos
 */
public class ModifierTGregRepair extends ModToolRepair {

	@Override
	protected boolean canModify(ItemStack tool, ItemStack[] input) {
		NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
		if(tags.getInteger("Damage") > 0) {
			int headID = tags.getInteger("Head");
			boolean areInputsValid = true;
			for(ItemStack curInput : input) {
				if(curInput != null && headID != PatternBuilder.instance.getPartID(curInput)
					&& !isValidTGregChunk(curInput, headID)) {
					areInputsValid = false;
					break;
				}
			}
			if(areInputsValid) {
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
		for(ItemStack curInput : input) {
			if(curInput != null) {
				int partValue = PatternBuilder.instance.getPartValue(curInput);
				materialValue += partValue != 0 ? partValue : getTGregPartValue(curInput, materialID);
				numInputs++;
			}
		}
		if(numInputs == 0) {
			return false;
		}

		int totalRepairValue = calculateIncrease(tool, materialValue, numInputs);
		float averageRepairValue = totalRepairValue / numInputs;

		return numInputs == 1 || (damage - totalRepairValue >= -averageRepairValue);
	}

	private boolean isValidTGregChunk(ItemStack input, int materialID) {
		if(input.getItem() == TGregworks.registry.toolParts.get(PartTypes.Chunk)) {
			Materials mainMaterial = TGregworks.registry.materialIDMap.get(materialID);
			if(mainMaterial != null) {
				NBTTagCompound data = TGregUtils.getTagCompound(input);
				if(!data.hasKey("material")) {
					return false;
				}
				Materials material = Materials.get(data.getString("material"));
				if(material != null && material == mainMaterial) {
					return true;
				}
			}
		}
		return false;
	}

	private int getTGregPartValue(ItemStack input, int materialID) {
		return isValidTGregChunk(input, materialID) ? 1 : 0;
	}

	private int calculateIncrease(ItemStack tool, int materialValue, int itemsUsed) {
		NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
		int damage = tags.getInteger("Damage");
		int dur = tags.getInteger("BaseDurability");
		int increase = (int) (50 * itemsUsed + (dur * 0.4f * materialValue));

		int modifiers = tags.getInteger("Modifiers");
		float mods = 1.0f;
		if(modifiers == 2) {
			mods = 0.9f;
		} else if(modifiers == 1) {
			mods = 0.8f;
		} else if(modifiers == 0) {
			mods = 0.7f;
		}

		increase *= mods;

		int repair = tags.getInteger("RepairCount");
		float repairCount = (100 - repair) / 100f;
		if(repairCount < 0.5f) {
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
		for(ItemStack curInput : input) {
			if(curInput != null) {
				int partValue = PatternBuilder.instance.getPartValue(curInput);
				materialValue += partValue != 0 ? partValue : getTGregPartValue(curInput, headID);
				itemsUsed++;
			}
		}

		int increase = calculateIncrease(tool, materialValue, itemsUsed);
		int repair = tags.getInteger("RepairCount");
		repair += itemsUsed;
		tags.setInteger("RepairCount", repair);

		damage -= increase;
		if(damage < 0) {
			damage = 0;
		}
		tags.setInteger("Damage", damage);

		AbilityHelper.damageTool(tool, 0, null, true);
	}
}
