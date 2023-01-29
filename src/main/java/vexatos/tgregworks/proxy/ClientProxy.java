package vexatos.tgregworks.proxy;

import tconstruct.library.client.TConstructClientRegistry;
import vexatos.tgregworks.TGregworks;
import gregtech.api.enums.Materials;

/**
 * @author Vexatos
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void addToolRenderMappings() {
        for (Materials m : TGregworks.registry.toolMaterials) {
            TConstructClientRegistry
                    .addMaterialRenderMapping(TGregworks.registry.matIDs.get(m), "tgregworks", "", true);
        }
    }

    public void registerRenderers() {
        // RenderingRegistry.registerEntityRenderingHandler(TGregDaggerEntity.class, new DaggerRenderCustom());
    }
}
