package vexatos.tgregworks.util;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.Materials;
import net.minecraft.nbt.NBTTagCompound;
import tconstruct.library.event.ToolCraftEvent;
import tconstruct.library.tools.ToolMaterial;
import vexatos.tgregworks.TGregworks;

/**
 * @author Vexatos
 */
public class BuildingHandler {

	@SubscribeEvent
	public void onToolBuilding(ToolCraftEvent e) {
		NBTTagCompound data = e.toolTag;
		if(data == null) {
			return;
		}
		if(!data.hasKey("InfiTool")) {
			data.setTag("InfiTool", new NBTTagCompound());
		}
		data = data.getCompoundTag("InfiTool");

		ToolMaterial[] materials = e.materials;

		if(materials.length >= 1 && materials[0] != null && Materials.get(materials[0].materialName) != null
			&& TGregworks.registry.toolMaterials.contains(Materials.get(materials[0].materialName))) {

			Materials headMaterial = Materials.get(materials[0].materialName);
			data.setInteger("HeadColor", (headMaterial.getRGBA()[0] << 16) | (headMaterial.getRGBA()[1] << 8) | (headMaterial.getRGBA()[2]));
		}
		if(materials.length >= 2 && materials[1] != null && Materials.get(materials[1].materialName) != null
			&& TGregworks.registry.toolMaterials.contains(Materials.get(materials[1].materialName))) {

			Materials headMaterial = Materials.get(materials[1].materialName);
			data.setInteger("HandleColor", (headMaterial.getRGBA()[0] << 16) | (headMaterial.getRGBA()[1] << 8) | (headMaterial.getRGBA()[2]));
		}
		if(materials.length >= 3 && materials[2] != null && Materials.get(materials[2].materialName) != null
			&& TGregworks.registry.toolMaterials.contains(Materials.get(materials[2].materialName))) {

			Materials headMaterial = Materials.get(materials[2].materialName);
			data.setInteger("AccessoryColor", (headMaterial.getRGBA()[0] << 16) | (headMaterial.getRGBA()[1] << 8) | (headMaterial.getRGBA()[2]));
		}
		if(materials.length >= 4 && materials[3] != null && Materials.get(materials[3].materialName) != null
			&& TGregworks.registry.toolMaterials.contains(Materials.get(materials[3].materialName))) {

			Materials headMaterial = Materials.get(materials[3].materialName);
			data.setInteger("ExtraColor", (headMaterial.getRGBA()[0] << 16) | (headMaterial.getRGBA()[1] << 8) | (headMaterial.getRGBA()[2]));
		}
	}
}
