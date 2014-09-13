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
import java.util.List;

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
			TConstructRegistry.addToolMaterial(matID, m.name(), m.mToolQuality, m.mDurability, (int) (m.mToolSpeed * 100F), (int) m.mToolQuality, (float) m.mToolQuality - 0.5F, getReinforcedLevel(m), getStoneboundLevel(m), "", (m.getRGBA()[0] << 16) | (m.getRGBA()[1] << 8) | (m.getRGBA()[2]));
			TConstructRegistry.addBowMaterial(matID, m.mDurability, (int) (m.mToolQuality * 10F), ((float) m.mToolQuality) - 0.5F);
			TConstructRegistry.addArrowMaterial(matID, (float)(((double) m.getMass()) / 10F), 0.2F, 99F);
			ToolCore.materialColourMap.put(matID, (m.getRGBA()[0] << 16) | (m.getRGBA()[1] << 8) | (m.getRGBA()[2]));
			matIDs.put(m, matID);
		}

		ItemTGregPart.toolMaterialNames = toolMaterialNames;
		ItemTGregPart.matIDs = matIDs;
	}

	private List<Materials> stonebound1Mats = Arrays.asList(Materials.Titanium, Materials.Tungsten);
	private List<Materials> spiny1Mats = Arrays.asList(Materials.Uranium, Materials.Uranium235);

	private float getStoneboundLevel(Materials m) {
		if(stonebound1Mats.contains(m)) {
			return 1;
		}
		if(spiny1Mats.contains(m)) {
			return -1;
		}
		return 0;
	}

	private List<Materials> reinforced1Mats = Arrays.asList(Materials.SteelMagnetic, Materials.BlackSteel, Materials.BlueSteel, Materials.Titanium, Materials.DamascusSteel, Materials.StainlessSteel, Materials.RedSteel, Materials.MeteoricSteel, Materials.TungstenSteel);
	private List<Materials> reinforced2Mats = Arrays.asList(Materials.Osmium, Materials.Iridium);

	private int getReinforcedLevel(Materials m) {
		if(reinforced1Mats.contains(m)) {
			return 1;
		}
		if(reinforced2Mats.contains(m)) {
			return 2;
		}
		if(m == Materials.Osmiridium) {
			return 3;
		}
		return 0;
	}

	private boolean doesMaterialExist(Materials m) {
		return TConstructRegistry.toolMaterialStrings.containsKey(m.name());
		// && Arrays.asList(GregTech_API.sGeneratedMaterials).contains(m);
	}
}
