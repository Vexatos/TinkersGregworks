package vexatos.tgregworks.util;

import gregtech.api.enums.Materials;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vexatos.tgregworks.TGregworks;

/**
 * @author Vexatos
 */
public class TGregUtils {
	public static NBTTagCompound getTagCompound(ItemStack stack) {
		if(stack.hasTagCompound()) {
			return stack.getTagCompound();
		}
		NBTTagCompound data = new NBTTagCompound();
		stack.setTagCompound(data);
		return data;
	}

	public static NBTTagCompound getCompoundTag(NBTTagCompound tag, String key) {
		if(tag.hasKey(key)) {
			return tag.getCompoundTag(key);
		}
		NBTTagCompound data = new NBTTagCompound();
		tag.setTag(key, data);
		return data;
	}

	public static NBTTagCompound getCompoundTag(ItemStack stack, String key) {
		return getCompoundTag(getTagCompound(stack), key);
	}

	public static int getMaterialID(ItemStack stack) {
		NBTTagCompound data = getTagCompound(stack);
		if(!data.hasKey("material")) {
			return -1;
		}
		return TGregworks.registry.matIDs.get(Materials.get(data.getString("material")));
	}
}
