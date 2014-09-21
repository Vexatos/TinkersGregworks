package vexatos.tgregworks.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
}
