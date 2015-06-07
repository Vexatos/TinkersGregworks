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
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.TGregUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vexatos
 */
public class ItemTGregPart extends CraftingItem implements IToolPart {

	private PartTypes type;

	public ItemTGregPart(PartTypes p) {
		super(toolMaterialNames.toArray(new String[toolMaterialNames.size()]), buildTextureNames(p), "parts/", "tinker", TGregworks.tab);
		this.setHasSubtypes(true);
		this.requiresMultipleRenderPasses();
		this.setMaxDamage(0);
		this.type = p;
	}

	public PartTypes getType() {
		return this.type;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		NBTTagCompound data = TGregUtils.getTagCompound(stack);
		String matName;
		if(!data.hasKey("material") || Materials.get(data.getString("material")) == Materials._NULL) {
			matName = "Unknown";
		} else {
			matName = Materials.get(data.getString("material")).mDefaultLocalName;
		}
		//String material = StatCollector.translateToLocal("tgregworks.parttype." + matName);
		//String name = StatCollector.translateToLocal("tgregworks.toolpart." + PartTypes.getFromID(stack.getItemDamage()) + "." + matName);
		//name = name.replaceAll("%%material", material);

		return matName + " " + type.partName;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		NBTTagCompound data = TGregUtils.getTagCompound(stack);
		String matName;
		if(!data.hasKey("material") || Materials.get(data.getString("material")) == Materials._NULL) {
			matName = "Unknown";
		} else {
			matName = Materials.get(data.getString("material")).mDefaultLocalName;
		}
		//return StatCollector.translateToLocal("tgregworks.toolpart." + PartTypes.getFromID(stack.getItemDamage()) + "." + matName);
		return matName;
	}

	private static String[] buildTextureNames(PartTypes p) {
		String[] names = new String[1];
		names[0] = p.textureName;
		return names;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.icons = new IIcon[1];

		for(int i = 0; i < this.icons.length; ++i) {
			if(!(textureNames[i].equals(""))) {
				this.icons[i] = iconRegister.registerIcon(modTexPrefix + ":" + folder + textureNames[i]);
			}
		}
	}

	public static ArrayList<String> toolMaterialNames = TGregworks.registry.toolMaterialNames;

	@SuppressWarnings("unchecked")
	@Override
	public void getSubItems(Item b, CreativeTabs tab, List list) {
		for(Materials m : TGregworks.registry.toolMaterials) {
			ItemStack stack = new ItemStack(b, 1, 0);
			NBTTagCompound data = TGregUtils.getTagCompound(stack);
			data.setString("material", m.name());
			stack.setTagCompound(data);
			list.add(stack);
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
		NBTTagCompound data = TGregUtils.getTagCompound(stack);
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
		NBTTagCompound data = TGregUtils.getTagCompound(stack);
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

	@Override
	public int getMaterialID(ItemStack stack) {
		return TGregUtils.getMaterialID(stack);
	}
}
