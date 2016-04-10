package vexatos.tgregworks.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregapi.data.CS;
import gregapi.data.MT;
import gregapi.oredict.IOreDictItemDataOverrideItem;
import gregapi.oredict.OreDictItemData;
import gregapi.oredict.OreDictMaterial;
import mantle.items.abstracts.CraftingItem;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import tconstruct.library.util.IToolPart;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.TGregUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vexatos
 */
public class ItemTGregPart extends CraftingItem implements IToolPart, IOreDictItemDataOverrideItem {

	private PartTypes type;

	public ItemTGregPart(PartTypes p) {
		super(toolMaterialNames.toArray(new String[toolMaterialNames.size()]), buildTextureNames(p), "parts/", "tinker", TGregworks.tab);
		this.setHasSubtypes(true);
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
		if(!data.hasKey("material") || OreDictMaterial.get(data.getString("material")) == MT.NULL) {
			matName = "Unknown";
		} else {
			matName = OreDictMaterial.get(data.getString("material")).mNameLocal;
		}

		matName = matName + " " + type.getPartName();
		//String material = StatCollector.translateToLocal("tgregworks.parttype." + matName);
		//String name = StatCollector.translateToLocal("tgregworks.toolpart." + PartTypes.getFromID(stack.getItemDamage()) + "." + matName);
		//name = name.replaceAll("%%material", material);

		if(stack.getItemDamage() == 0) {
			matName = matName + " (Deprecated)";
		}

		return matName;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		NBTTagCompound data = TGregUtils.getTagCompound(stack);
		String matName;
		if(!data.hasKey("material") || OreDictMaterial.get(data.getString("material")) == MT.NULL) {
			matName = "Unknown";
		} else {
			matName = OreDictMaterial.get(data.getString("material")).mNameLocal;
		}
		//return StatCollector.translateToLocal("tgregworks.toolpart." + PartTypes.getFromID(stack.getItemDamage()) + "." + matName);
		return matName;
	}

	@Override
	public String getUnlocalizedName() {
		return type.getPartName();
	}

	private static String[] buildTextureNames(PartTypes p) {
		String[] names = new String[1];
		names[0] = p.getTextureName();
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
		for(OreDictMaterial m : TGregworks.registry.toolMaterials) {
			ItemStack stack = new ItemStack(b, 1, TGregworks.registry.toolMaterials.indexOf(m) + 1);
			NBTTagCompound data = TGregUtils.getTagCompound(stack);
			data.setString("material", m.mNameInternal);
			stack.setTagCompound(data);
			list.add(stack);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack stack, int pass) {
		//int meta = getDamage(stack);
		//NBTTagCompound data = getTagCompound(stack);
		//if(!data.hasKey("material")) {
		//	return icons[meta];
		//}
		//Materials m = Materials.get(data.getString("material"));
		return icons[0];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return icons[0];
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

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
		NBTTagCompound data = TGregUtils.getTagCompound(stack);
		if(!data.hasKey("material")) {
			return;
		}
		OreDictMaterial m = OreDictMaterial.get(data.getString("material"));
		if(m != null) {
			stack.setItemDamage(TGregworks.registry.toolMaterials.indexOf(m) + 1);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List tooltip, boolean par4) {
		super.addInformation(stack, player, tooltip, par4);
		if(stack.getItemDamage() == 0) {
			tooltip.add(EnumChatFormatting.GRAY.toString() + EnumChatFormatting.ITALIC.toString() + "Put this into your inventory to update it.");
		}
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
			return MT.NULL.mRGBaSolid;
		}
		OreDictMaterial m = OreDictMaterial.get(data.getString("material"));
		if(m == null || m == MT.NULL) {
			return MT.NULL.mRGBaSolid;
		}
		for(byte i = 0; i < m.mRGBa.length; i++) {
			if(m.mRGBaSolid[i] > 255) {
				m.mRGBaSolid[i] = 255;
			}
			if(m.mRGBaSolid[i] < 0) {
				m.mRGBaSolid[i] = 0;
			}
		}
		return m.mRGBaSolid;
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

	@Override
	public OreDictItemData getOreDictItemData(ItemStack stack) {
		NBTTagCompound data = TGregUtils.getTagCompound(stack);
		if(!data.hasKey("material")) {
			return null;
		} else {
			OreDictMaterial mat = OreDictMaterial.get(data.getString("material"));
			return new OreDictItemData(mat, (CS.U / 2) * this.type.getPrice());
		}
	}
}
