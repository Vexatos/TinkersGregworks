package vexatos.tgregworks.client;

import tconstruct.library.TConstructRegistry;
import tconstruct.library.client.TConstructClientRegistry;
import tconstruct.library.tools.ToolCore;

/**
 * @author Vexatos
 */
public class TGregworksClientRegistry extends TConstructClientRegistry {

	public static void addMaterialRenderMapping (int materialID, String domain, String renderName, boolean useDefaultFolder)
	{
		for (ToolCore tool : TConstructRegistry.getToolMapping())
		{
			String[] toolIcons = new String[tool.getPartAmount() + 1];
			for (int i = 0; i < tool.getPartAmount() + 1; i++)
			{
				String icon = domain + ":";
				if (useDefaultFolder)
					icon += tool.getDefaultFolder() + "/";
				icon += renderName + tool.getIconSuffix(i);
				toolIcons[i] = icon;
			}
			tool.registerPartPaths(materialID, toolIcons);
		}
	}
}
