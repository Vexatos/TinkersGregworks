package vexatos.tgregworks.integration;

import com.google.common.collect.HashMultimap;
import gregapi.oredict.OreDictMaterial;
import gregapi.util.OM;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import vexatos.tgregworks.TGregworks;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.TGregUtils;

/**
 * @author Vexatos
 */
public class TGregRepairRegistry {
	public final HashMultimap<OreDictMaterial, RepairMaterial> repairMaterials = HashMultimap.create();

	public static abstract class RepairMaterial {
		public final int value;

		public RepairMaterial(int value) {
			this.value = value;
		}

		public abstract boolean matches(ItemStack input);
	}

	public static class ShardRepairMaterial extends RepairMaterial {

		public final OreDictMaterial m;

		public ShardRepairMaterial(OreDictMaterial m, int value) {
			super(value);
			this.m = m;
		}

		@Override
		public boolean matches(ItemStack input) {
			if(input.getItem() == TGregworks.registry.toolParts.get(PartTypes.Chunk)) {
				NBTTagCompound data = TGregUtils.getTagCompound(input);
				if(!data.hasKey("material")) {
					return false;
				}
				OreDictMaterial material = OreDictMaterial.get(data.getString("material"));
				if(material != null && material == this.m) {
					return true;
				}
			}
			return false;
		}
	}

	private static class OreDictRepairMaterial extends RepairMaterial {
		private final String tag;

		public OreDictRepairMaterial(String tag, int value) {
			super(value);
			this.tag = tag;
		}

		@Override
		public boolean matches(ItemStack input) {
			return OM.is(tag, input);
		}
	}

	public void registerShardRepairMaterial(OreDictMaterial m, int value) {
		repairMaterials.put(m, new ShardRepairMaterial(m, value));
	}

	public void registerOreDictRepairMaterial(OreDictMaterial m, String tag, int value) {
		repairMaterials.put(m, new OreDictRepairMaterial(tag, value));
	}
}
