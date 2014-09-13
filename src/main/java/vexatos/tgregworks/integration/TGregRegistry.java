package vexatos.tgregworks.integration;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.tools.ToolCore;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.item.ItemTGregPart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Vexatos
 */
public class TGregRegistry {

	private int latestAvailableNumber = 19;

	public ArrayList<Materials> toolMaterials = new ArrayList<Materials>();
	public ArrayList<String> toolMaterialNames = new ArrayList<String>();
	public HashMap<Materials, Integer> matIDs = new HashMap<Materials, Integer>();

	private int getLatestAvailableNumber() {
		for(int i = latestAvailableNumber; true; i++) {
			if(!TConstructRegistry.toolMaterials.containsKey(i)) {
				latestAvailableNumber = i + 1;
				return i;
			}
		}
	}

	public void registerToolParts() {
		TGregworks.log.info("Registering TGregworks tool parts.");
		for(Materials m : Materials.values()) {
			if(((m.mTypes & 64) == 64) && !doesMaterialExist(m) && Arrays.asList(GregTech_API.sGeneratedMaterials).contains(m)) {
				toolMaterials.add(m);
			}
		}
		for(Materials m : toolMaterials) {
			toolMaterialNames.add(m.mDefaultLocalName);
			int matID = getLatestAvailableNumber();
			TConstructRegistry.addToolMaterial(matID, m.name(), m.mToolQuality, m.mDurability, (int) m.mToolSpeed, (int) m.mToolSpeed, (float) m.mToolQuality - 0.5F, (int) (((m.getDensity() / GregTech_API.MATERIAL_UNIT) - 1) % 3), 0F, "", (m.getRGBA()[0] << 16) | (m.getRGBA()[1] << 8) | (m.getRGBA()[2]));
			ToolCore.materialColourMap.put(matID, (m.getRGBA()[0] << 16) | (m.getRGBA()[1] << 8) | (m.getRGBA()[2]));
			matIDs.put(m, matID);

		}
		ItemTGregPart.toolMaterialNames = toolMaterialNames;
		ItemTGregPart.matIDs = matIDs;
	}

	private boolean doesMaterialExist(Materials m) {
		return TConstructRegistry.toolMaterialStrings.containsKey(m.name());
		// && Arrays.asList(GregTech_API.sGeneratedMaterials).contains(m);
	}
}
