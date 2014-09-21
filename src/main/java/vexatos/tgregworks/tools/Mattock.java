package vexatos.tgregworks.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GT_ModHandler;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import vexatos.tgregworks.util.ITGregElectricTool;
import vexatos.tgregworks.util.TGregAbilityHelper;
import vexatos.tgregworks.util.TGregUtils;

import java.util.List;

/**
 * @author Vexatos, using code from GregoriusT
 */
public class Mattock extends tconstruct.items.tools.Mattock implements ITGregElectricTool {
	/* Tags and information about the tool */
	@SuppressWarnings("unchecked")
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		if(!stack.hasTagCompound()) {
			return;
		}
		NBTTagCompound tags = TGregUtils.getCompoundTag(stack, "TGregBattery");
		if(tags.hasKey("electric")) {
			String color = "";
			long charge = this.getRealCharge(stack);

			if(charge != 0) {
				if(charge <= this.getRealMaxCharge(stack) / 3) {
					color = "\u00a74";
				} else if(charge > this.getRealMaxCharge(stack) * 2 / 3) {
					color = "\u00a72";
				} else {
					color = "\u00a76";
				}
			}

			String energy = color + this.getRealCharge(stack) + "/" + this.getRealMaxCharge(stack) + " EU";
			list.add(energy);
		}
		super.addInformation(stack, player, list, par4);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int x, int y, int z, EntityLivingBase player) {
		return !(block != null && (double) block.getBlockHardness(world, x, y, z) != 0.0D)
			|| TGregAbilityHelper.onBlockChanged(itemstack, world, block, x, y, z, player, random);
	}

	// Attacking
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		TGregAbilityHelper.onLeftClickEntity(stack, player, entity, this, 0);
		return false;
	}

	@Override
	public int getDamage(ItemStack stack) {
		NBTTagCompound tags = stack.getTagCompound();
		if(tags == null) {
			return 0;
		}
		tags = TGregUtils.getCompoundTag(tags, "TGregBattery");
		if(tags.hasKey("electric")) {
			long energy = getRealCharge(stack);
			long max = this.getRealMaxCharge(stack);
			if(energy > 0) {
				return (int) (((max - energy) * 100L) / max);
			}
		}
		return super.getDamage(stack);
	}

	    /* Mattock specific */

	@Override
	public void setDamage(ItemStack stack, int damage) {
		TGregAbilityHelper.damageTool(stack, damage - stack.getItemDamage(), null, false);
	}

	public long getRealCharge(ItemStack itemStack) {
		Long[] stats = getElectricStats(itemStack);
		if(stats == null) {
			return 0;
		}
		NBTTagCompound data = TGregUtils.getCompoundTag(itemStack, "TGregBattery");
		return data == null ? 0 : data.getLong("currentCharge");
	}

	public long getRealMaxCharge(ItemStack itemStack) {
		return TGregUtils.getCompoundTag(itemStack, "TGregBattery").getLong("maxCharge");
	}

	public Long[] getElectricStats(ItemStack itemStack) {
		NBTTagCompound data = TGregUtils.getTagCompound(itemStack).hasKey("TGregBattery") ? TGregUtils.getCompoundTag(itemStack, "TGregBattery") : null;
		if(data != null && data.getBoolean("electric")) {
			return new Long[] { data.getLong("maxCharge"), data.getLong("transferLimit"), data.getLong("tier") };
		}
		return null;
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float clickX, float clickY, float clickZ) {
		NBTTagCompound tags = stack.getTagCompound().getCompoundTag("InfiTool");
		return !tags.getBoolean("Broken") && TGregAbilityHelper.hoeGround(stack, player, world, x, y, z, side, random);
	}

	@Override
	public boolean canProvideEnergy(ItemStack itemStack) {
		return isElectric(itemStack);
	}

	@Override
	public Item getChargedItem(ItemStack itemStack) {
		return this;
	}

	@Override
	public Item getEmptyItem(ItemStack itemStack) {
		return this;
	}

	@Override
	public double getMaxCharge(ItemStack itemStack) {
		return TGregUtils.getCompoundTag(itemStack, "TGregBattery").getLong("maxCharge");
	}

	@Override
	public int getTier(ItemStack itemStack) {
		return TGregUtils.getCompoundTag(itemStack, "TGregBattery").getInteger("tier");
	}

	@Override
	public double getTransferLimit(ItemStack itemStack) {
		return TGregUtils.getCompoundTag(itemStack, "TGregBattery").getLong("transferLimit");
	}

	public boolean isElectric(ItemStack itemStack) {
		return TGregUtils.getCompoundTag(itemStack, "TGregBattery").getBoolean("electric");
	}

	@Override
	public final double charge(ItemStack itemStack, double charge, int tier, boolean ignoreTransferLimit, boolean simulate) {
		NBTTagCompound stats = TGregUtils.getCompoundTag(itemStack, "TGregBattery");
		if(stats == null || stats.getInteger("tier") > tier || itemStack.stackSize != 1 || stats.getBoolean("singleuse")) {
			return 0;
		}
		long oldCharge = getRealCharge(itemStack);

		long newCharge = charge == Integer.MAX_VALUE ? Long.MAX_VALUE :
			Math.min(Math.abs(stats.getLong("maxCharge")), oldCharge + (ignoreTransferLimit ? (long) charge : Math.min(stats.getInteger("voltage"), (long) charge)));
		if(!simulate) {
			setCharge(itemStack, newCharge);
		}
		return newCharge - oldCharge;
	}

	@Override
	public double discharge(ItemStack itemStack, double charge, int tier, boolean ignoreTransferLimit, boolean batteryAlike, boolean simulate) {
		Long[] tStats = getElectricStats(itemStack);
		if(tStats == null || tStats[2] > tier) {
			return 0;
		}
		if(batteryAlike && !canProvideEnergy(itemStack)) {
			return 0;
		}
		long tChargeBefore = getRealCharge(itemStack), tNewCharge = Math.max(0, tChargeBefore - (ignoreTransferLimit ? (long) charge : Math.min(tStats[1], (long) charge)));
		if(!simulate) {
			setCharge(itemStack, tNewCharge);
		}
		return tChargeBefore - tNewCharge;
	}

	@Override
	public double getCharge(ItemStack itemStack) {
		return getRealCharge(itemStack);
	}

	@Override
	public boolean canUse(ItemStack itemStack, double amount) {
		return getRealCharge(itemStack) >= amount;
	}

	@Override
	public boolean use(ItemStack itemStack, double amount, EntityLivingBase entityLivingBase) {
		chargeFromArmor(itemStack, entityLivingBase);
		if(entityLivingBase instanceof EntityPlayer && ((EntityPlayer) entityLivingBase).capabilities.isCreativeMode) {
			return true;
		}
		double transfer = discharge(itemStack, amount, Integer.MAX_VALUE, true, false, true);
		if(transfer == amount) {
			discharge(itemStack, amount, Integer.MAX_VALUE, true, false, false);
			chargeFromArmor(itemStack, entityLivingBase);
			return true;
		}
		discharge(itemStack, amount, Integer.MAX_VALUE, true, false, false);
		chargeFromArmor(itemStack, entityLivingBase);
		return false;
	}

	@Override
	public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entityLivingBase) {
		if(entityLivingBase == null || entityLivingBase.worldObj.isRemote) {
			return;
		}
		for(int i = 1; i < 5; i++) {
			ItemStack tArmor = entityLivingBase.getEquipmentInSlot(i);
			if(GT_ModHandler.isElectricItem(tArmor)) {
				IElectricItem tArmorItem = (IElectricItem) tArmor.getItem();
				if(tArmorItem.canProvideEnergy(tArmor) && tArmorItem.getTier(tArmor) >= getTier(itemStack)) {
					double tCharge = ElectricItem.manager.discharge(tArmor, charge(itemStack, Integer.MAX_VALUE - 1, Integer.MAX_VALUE, true, true), Integer.MAX_VALUE, true, true, false);
					if(tCharge > 0) {
						charge(itemStack, tCharge, Integer.MAX_VALUE, true, false);
						if(entityLivingBase instanceof EntityPlayer) {
							Container tContainer = ((EntityPlayer) entityLivingBase).openContainer;
							if(tContainer != null) {
								tContainer.detectAndSendChanges();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public String getToolTip(ItemStack itemStack) {
		return null;
	}

	private boolean setCharge(ItemStack itemStack, long charge) {
		Long[] stats = getElectricStats(itemStack);
		if(stats == null) {
			return false;
		}
		NBTTagCompound data = TGregUtils.getCompoundTag(itemStack, "TGregBattery");
		if(data == null) {
			data = new NBTTagCompound();
		}
		data.removeTag("currentCharge");
		charge = Math.min(stats[0] < 0 ? Math.abs(stats[0] / 2) : charge, Math.abs(stats[0]));
		if(charge > 0) {
			itemStack.setItemDamage(getChargedMetaData(itemStack));
			data.setLong("currentCharge", charge);
		} else {
			itemStack.setItemDamage(getEmptyMetaData(itemStack));
		}
		if(data.hasNoTags()) {
			itemStack.setTagCompound(null);
		} else {
			itemStack.setTagCompound(data);
		}
		return true;
	}

	public short getChargedMetaData(ItemStack itemStack) {
		return (short) (itemStack.getItemDamage() - (itemStack.getItemDamage() % 2));
	}

	public short getEmptyMetaData(ItemStack itemStack) {
		return (short) (itemStack.getItemDamage() + 1 - (itemStack.getItemDamage() % 2));
	}

	@Override
	public IElectricItemManager getManager(ItemStack itemStack) {
		return this;
	}
}
