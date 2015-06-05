package vexatos.tgregworks.modifiers;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import tconstruct.modifiers.tools.ModBoolean;
import vexatos.tgregworks.util.ITGregElectricTool;
import vexatos.tgregworks.util.TGregUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author SlimeKnights, Vexatos
 */
public class TGregBatteryModifier extends ModBoolean {

	public HashMap<ItemStack, ItemStack> batteries = new HashMap<ItemStack, ItemStack>();
	public ArrayList<ItemStack> singleuse = new ArrayList<ItemStack>();
	public int modifiersRequired = 1;

	public TGregBatteryModifier() {
		super(new ItemStack[0], 9, "TGregBattery", "\u00a7e", "");
	}

	@Override
	protected boolean canModify(ItemStack tool, ItemStack[] input) {
		NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");
		return tags.getInteger("Modifiers") > 0 && !tags.getBoolean(key) && !tags.getBoolean("Flux") && tool.getItem() instanceof ITGregElectricTool;
	}

	@Override
	public boolean matches(ItemStack[] input, ItemStack tool) {
		NBTTagCompound tags = tool.getTagCompound().getCompoundTag("InfiTool");

		ItemStack foundBattery = null;
		// try to find the battery in the input
		for(ItemStack stack : input) {
			for(ItemStack battery : batteries.keySet()) {
				if(stack == null) {
					continue;
				}
				if(stack.getItem() != battery.getItem()) {
					continue;
				}
				if(!(stack.getItem() instanceof IElectricItem)) {
					continue;
				}
				// we don't allow multiple batteries to be added
				if(foundBattery != null) {
					return false;
				}

				// battery found, gogogo
				foundBattery = stack;
			}
		}

		// no battery present
		if(foundBattery == null) {
			return false;
		}

		boolean hasCircuit = false;

		for(ItemStack stack : input) {
			if(stack == batteries.get(foundBattery)) {
				hasCircuit = true;
			}
		}

		if(!hasCircuit) {
			return false;
		}

		// check if we already have an electric modifier
		if(tags.getBoolean(key)) {
			// only allow if it's an upgrade
			// remark: we use the ToolCores function here instead of accessing the tag directly, to achieve backwards compatibility with tools without tags.

			return ((IElectricItem) foundBattery.getItem()).getMaxCharge(foundBattery) > ((IElectricItem) tool.getItem()).getMaxCharge(tool);
		}
		// otherwise check if we have enough modfiers
		else if(tags.getInteger("Modifiers") < modifiersRequired) {
			return false;
		}

		// all requirements satisfied!
		return true;
	}

	@SuppressWarnings("ConstantConditions")
	@Override
	public void modify(ItemStack[] input, ItemStack tool) {
		NBTTagCompound tags = tool.getTagCompound();

		// update modifiers (only applies if it's not an upgrade)
		if(!tags.hasKey(key)) {
			int modifiers = tags.getCompoundTag("InfiTool").getInteger("Modifiers");
			modifiers -= modifiersRequired;
			tags.getCompoundTag("InfiTool").setInteger("Modifiers", modifiers);
			addModifierTip(tool, "\u00a7eElectric");
		}

		tags.getCompoundTag("InfiTool").setBoolean(key, true);
		tags = TGregUtils.getCompoundTag(tags, "TGregBattery");

		// find the battery in the input
		ItemStack inputBattery = null;
		for(ItemStack stack : input) {
			for(ItemStack battery : batteries.keySet()) {
				if(stack == null) {
					continue;
				}
				if(stack.getItem() != battery.getItem()) {
					continue;
				}
				if(!(stack.getItem() instanceof IElectricItem)) {
					continue;
				}

				// we're guaranteed to only find one battery because more are prevented above
				inputBattery = stack;
			}
		}

		// get the energy interface
		IElectricItem energyContainer = (IElectricItem) inputBattery.getItem();

		// set the charge values
		long charge = (long) ElectricItem.manager.getCharge(inputBattery);

		// add already present charge in the tool
		if(tags.hasKey("currentCharge")) {
			charge += tags.getInteger("currentCharge");
		}
		long maxCharge = (long) energyContainer.getMaxCharge(inputBattery);

		ItemStack subject42 = inputBattery.copy();

		int progress = 0; // prevent endless loops with creative battery, blah
		long change = 1;
		// fill the battery full
		while(progress < maxCharge && change > 0) {
			change = (long) ElectricItem.manager.charge(subject42, 100000, energyContainer.getTier(subject42), false, false);
			progress += change;
		}
		// get the maximum extraction rate
		long maxExtract = (long) ElectricItem.manager.discharge(subject42, Integer.MAX_VALUE, energyContainer.getTier(subject42), false, false, true);

		subject42 = inputBattery.copy();

		// completely empty the battery
		progress = 0;
		change = 1;
		while(progress < maxCharge && change > 0) {
			change = (long) ElectricItem.manager.discharge(subject42, 100000, energyContainer.getTier(subject42), false, false, false);
			progress += change;
		}
		long maxReceive = (long) ElectricItem.manager.charge(subject42, Integer.MAX_VALUE, energyContainer.getTier(subject42), false, true);

		// make sure we don't overcharge
		charge = Math.min(charge, maxCharge);

		tags.setBoolean("electric", true);
		tags.setLong("currentCharge", charge);
		tags.setLong("maxCharge", maxCharge);
		tags.setInteger("tier", energyContainer.getTier(subject42));
		tags.setLong("transferLimit", maxExtract);
		tags.setLong("transferLimit", maxReceive);
		if(singleuse.contains(inputBattery)) {
			tags.setBoolean("singleuse", true);
		}

		tags.setInteger(key, 1);
		ITGregElectricTool toolcore = (ITGregElectricTool) tool.getItem();
		tool.setItemDamage((int) (1 + (toolcore.getRealMaxCharge(tool) - charge) * (tool.getMaxDamage() - 1) / toolcore.getRealMaxCharge(tool)));
	}
}
