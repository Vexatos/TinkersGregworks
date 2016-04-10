package vexatos.tgregworks.util;

import gregapi.oredict.OreDictMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.reference.PartTypes;

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
		Integer matID = TGregworks.registry.matIDs.get(OreDictMaterial.get(data.getString("material")));
		return matID != null ? matID : 0;
	}

	public static ItemStack newItemStack(OreDictMaterial m, PartTypes p, int amount) {
		ItemStack stack = new ItemStack(TGregworks.registry.toolParts.get(p), amount, TGregworks.registry.toolMaterials.indexOf(m) + 1);
		NBTTagCompound data = TGregUtils.getTagCompound(stack);
		data.setString("material", m.mNameInternal);
		stack.setTagCompound(data);
		return stack;
	}
}
