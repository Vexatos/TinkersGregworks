package vexatos.tgregworks.integration.iguanatweakstconstruct;

import iguanaman.iguanatweakstconstruct.IguanaTweaksTConstruct;
import iguanaman.iguanatweakstconstruct.reference.Config;
import tconstruct.library.crafting.ModifyBuilder;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.integration.modifiers.ModTGregRepair;

/**
 * @author Vexatos
 */
public class IntegrationITT {
	public void init() {
		if(IguanaTweaksTConstruct.pulsar.isPulseLoaded("ToolPartReplacing")) {
			TGregworks.log.info("Adding IguanaTweaksTConstruct Tool Part Replacing support.");
			ModifyBuilder.registerModifier(new ModTGregPartReplacement());
		}
	}

	public void postInit() {
		if(IguanaTweaksTConstruct.pulsar.isPulseLoaded("Tweaks") && Config.maxToolRepairs > -1) {
			TGregworks.log.info("Adding IguanaTweaksTConstruct Limited Tool Repair support.");
			ModifyBuilder.registerModifier(new ModLimitedTGregRepair());
		} else {
			ModifyBuilder.registerModifier(new ModTGregRepair());
		}
	}
}
