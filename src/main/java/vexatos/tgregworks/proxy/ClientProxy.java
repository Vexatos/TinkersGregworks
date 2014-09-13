package vexatos.tgregworks.proxy;

import gregtech.api.enums.Materials;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.client.TGregworksClientRegistry;

/**
 * @author Vexatos
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void addToolRenderMappings() {
		for(Materials m : TGregworks.registry.toolMaterials) {
			TGregworksClientRegistry.addMaterialRenderMapping(TGregworks.registry.matIDs.get(m), "tgregworks", "", true);
		}
	}
}
