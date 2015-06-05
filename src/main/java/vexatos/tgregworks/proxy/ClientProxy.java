package vexatos.tgregworks.proxy;

import gregtech.api.enums.Materials;
import tconstruct.library.client.TConstructClientRegistry;
import vexatos.tgregworks.TGregworks;

/**
 * @author Vexatos
 */
public class ClientProxy extends CommonProxy {

	@Override
	public void addToolRenderMappings() {
		for(Materials m : TGregworks.registry.toolMaterials) {
			TConstructClientRegistry.addMaterialRenderMapping(TGregworks.registry.matIDs.get(m), "tgregworks", "", true);
		}
	}

	public void registerRenderers() {
		//RenderingRegistry.registerEntityRenderingHandler(TGregDaggerEntity.class, new DaggerRenderCustom());
	}
}
