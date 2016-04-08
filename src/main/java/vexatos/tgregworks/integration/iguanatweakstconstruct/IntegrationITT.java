package vexatos.tgregworks.integration.iguanatweakstconstruct;

import iguanaman.iguanatweakstconstruct.IguanaTweaksTConstruct;
import tconstruct.library.crafting.ModifyBuilder;
import vexatos.tgregworks.TGregworks;

/**
 * @author Vexatos
 */
public class IntegrationITT {
	public void init() {
		if(IguanaTweaksTConstruct.pulsar.isPulseLoaded("ToolPartReplacing")) {
			TGregworks.log.info("Adding IguanaTweaksTConstruct integration.");
			ModifyBuilder.registerModifier(new ModTGregPartReplacement());
		}
	}
}
