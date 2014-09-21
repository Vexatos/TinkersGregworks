package vexatos.tgregworks.proxy;

import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.enums.Materials;
import tconstruct.client.entity.projectile.DaggerRenderCustom;
import tconstruct.library.client.TConstructClientRegistry;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.item.entity.TGregDaggerEntity;

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
		RenderingRegistry.registerEntityRenderingHandler(TGregDaggerEntity.class, new DaggerRenderCustom());
	}
}
