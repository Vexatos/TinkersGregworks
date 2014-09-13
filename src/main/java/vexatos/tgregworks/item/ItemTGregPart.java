package vexatos.tgregworks.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import mantle.items.abstracts.CraftingItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import tconstruct.library.util.IToolPart;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.integration.PartTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Vexatos
 */
public class ItemTGregPart extends CraftingItem implements IToolPart {

	public ItemTGregPart() {
		super(toolMaterialNames.toArray(new String[toolMaterialNames.size()]), buildTextureNames(), "parts/", "tgregworks", TGregworks.tab);
		this.setHasSubtypes(true);
		this.requiresMultipleRenderPasses();
		this.setMaxDamage(0);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		NBTTagCompound data = getTagCompound(stack);
		Materials material;
		String matName;
		if(!data.hasKey("material") || Materials.get(data.getString("material")) == Materials._NULL) {
			matName = "Unknown";
		} else {
			matName = Materials.get(data.getString("material")).mDefaultLocalName;
		}
		//String material = StatCollector.translateToLocal("tgregworks.parttype." + matName);
		//String name = StatCollector.translateToLocal("tgregworks.toolpart." + PartTypes.getFromID(stack.getItemDamage()) + "." + matName);
		//name = name.replaceAll("%%material", material);

		return matName + " " + PartTypes.getFromID(stack.getItemDamage()).partName;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		NBTTagCompound data = getTagCompound(stack);
		String matName;
		if(!data.hasKey("material") || Materials.get(data.getString("material")) == Materials._NULL) {
			matName = "Unknown";
		} else {
			matName = Materials.get(data.getString("material")).mDefaultLocalName;
		}
		//return StatCollector.translateToLocal("tgregworks.toolpart." + PartTypes.getFromID(stack.getItemDamage()) + "." + matName);
		return matName;
	}

	private static String[] buildTextureNames() {
		String[] names = new String[toolMaterialNames.size()];
		for(int i = 0; i < PartTypes.values().length; i++) {
			names[i] = PartTypes.getFromID(i).textureName;
		}
		return names;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.icons = new IIcon[PartTypes.values().length];

		for(int i = 0; i < this.icons.length; ++i) {
			if(!(textureNames[i].equals("")))
				this.icons[i] = iconRegister.registerIcon(modTexPrefix + ":" + folder + textureNames[i]);
		}
	}

	public static ArrayList<String> toolMaterialNames = TGregworks.registry.toolMaterialNames;
	public static HashMap<Materials, Integer> matIDs = TGregworks.registry.matIDs;

	@SuppressWarnings("unchecked")
	@Override
	public void getSubItems(Item b, CreativeTabs tab, List list) {
		for(Materials m : TGregworks.registry.toolMaterials) {
			for(PartTypes p : PartTypes.values()) {
				ItemStack stack = new ItemStack(b, 1, p.metaID);
				NBTTagCompound data = getTagCompound(stack);
				data.setString("material", m.name());
				stack.setTagCompound(data);
				list.add(stack);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass) {
		int meta = getDamage(stack);
		//NBTTagCompound data = getTagCompound(stack);
		//if(!data.hasKey("material")) {
		//	return icons[meta];
		//}
		//Materials m = Materials.get(data.getString("material"));
		return icons[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return icons[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int pass) {
		NBTTagCompound data = getTagCompound(stack);
		if(!data.hasKey("material")) {
			return super.getColorFromItemStack(stack, pass);
		}
		int colour;
		int[] rgba = toIntArray(getRGBa(stack));
		colour = (rgba[0] << 16) | (rgba[1] << 8) | (rgba[2]);
		return colour;
	}

	private int[] toIntArray(short[] r) {
		int[] i = new int[r.length];
		for(int j = 0; j < r.length; j++) {
			i[j] = r[j];
		}
		return i;
	}

	/**
	 * @return the Color Modulation the Material is going to be rendered with.
	 */
	public static short[] getRGBa(ItemStack stack) {
		NBTTagCompound data = getTagCompound(stack);
		if(!data.hasKey("material")) {
			return Materials._NULL.mRGBa;
		}
		Materials m = Materials.get(data.getString("material"));
		if(m == null || m == Materials._NULL) {
			return Materials._NULL.mRGBa;
		}
		for(byte i = 0; i < m.mRGBa.length; i++) {
			if(m.mRGBa[i] > 255) {
				m.mRGBa[i] = 255;
			}
			if(m.mRGBa[i] < 0) {
				m.mRGBa[i] = 0;
			}
		}
		return m.mRGBa;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconIndex(ItemStack stack) {
		return getIcon(stack, 0);
	}

	public static NBTTagCompound getTagCompound(ItemStack stack) {
		if(stack.hasTagCompound()) {
			return stack.getTagCompound();
		}
		NBTTagCompound data = new NBTTagCompound();
		stack.setTagCompound(data);
		return data;
	}

	@Override
	public int getMaterialID(ItemStack stack) {
		NBTTagCompound data = getTagCompound(stack);
		if(!data.hasKey("material")) {
			return -1;
		}
		return matIDs.get(Materials.get(data.getString("material")));
	}
}
