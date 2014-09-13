package vexatos.tgregworks.integration.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.ToolRecipe;
import tconstruct.library.tools.ToolCore;
import tconstruct.tools.TinkerTools;
import vexatos.tgregworks.reference.PartTypes;

import java.util.LinkedList;

/**
 * @author Vexatos
 */
public class TGregToolRecipe extends ToolRecipe {

	protected LinkedList<ItemStack> newHeadList = new LinkedList<ItemStack>();
	protected LinkedList<ItemStack> newHandleList = new LinkedList<ItemStack>();
	protected LinkedList<ItemStack> newAccessoryList = new LinkedList<ItemStack>();
	protected LinkedList<ItemStack> newExtraList = new LinkedList<ItemStack>();
	protected Item toolRod = TConstructRegistry.getItem("toolRod");
	protected Item fletching = TinkerTools.fletching;

	public TGregToolRecipe(ItemStack head, ItemStack handle, ToolCore tool) {
		super(head.getItem(), handle.getItem(), null, null, tool);
		this.newHeadList.add(head);
		this.newHandleList.add(handle);
		result = tool;
	}

	@SuppressWarnings("ConstantConditions")
	public TGregToolRecipe(ItemStack head, ItemStack handle, ItemStack accessory, ToolCore tool) {
		super(head.getItem(), handle.getItem(), accessory.getItem(), null, tool);
		this.newHeadList.add(head);
		this.newHandleList.add(handle);
		if(accessory != null) {
			this.newAccessoryList.add(accessory);
		}
		result = tool;
	}

	@SuppressWarnings("ConstantConditions")
	public TGregToolRecipe(ItemStack head, ItemStack handle, ItemStack accessory, ItemStack extra, ToolCore tool) {
		super(head.getItem(), handle.getItem(), accessory.getItem(), extra.getItem(), tool);
		this.newHeadList.add(head);
		this.newHandleList.add(handle);
		if(accessory != null) {
			this.newAccessoryList.add(accessory);
		}
		if(extra != null) {
			this.newExtraList.add(extra);
		}
		result = tool;
	}

	public TGregToolRecipe(ItemStack head, ItemStack handle, Item fletching, ToolCore arrow) {
		this(head, handle, new ItemStack(fletching), arrow);
	}

	@Override
	public void addHeadItem(ItemStack head) {
		this.newHeadList.add(head);
	}

	@Override
	public void addHandleItem(ItemStack handle) {
		this.newHandleList.add(handle);
	}

	@Override
	public void addAccessoryItem(ItemStack accessory) {
		this.newAccessoryList.add(accessory);
	}

	@Override
	public void addExtraItem(ItemStack extra) {
		this.newExtraList.add(extra);
	}

	@Override
	public boolean validHead(ItemStack input) {
		for(ItemStack part : newHeadList) {
			if((part.getItem() == input.getItem()) && (part.getItemDamage() == input.getItemDamage())) {
				return true;
			}
			if(input.getItem() == PartTypes.getFromID(part.getItemDamage()).counterpart){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validHandle(ItemStack input) {
		for(ItemStack part : newHandleList) {
			if((part.getItem() == input.getItem()) && (part.getItemDamage() == input.getItemDamage())) {
				return true;
			}
			if(input.getItem() == PartTypes.getFromID(part.getItemDamage()).counterpart){
				return true;
			}
			if(toolRod != null && part.getItem() == toolRod && (input.getItem() == Items.stick || input.getItem() == Items.bone)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validAccessory(ItemStack input) {
		if(input == null) {
			return newAccessoryList.size() < 1;
		}
		for(ItemStack part : newAccessoryList) {
			if((part.getItem() == input.getItem()) && (part.getItemDamage() == input.getItemDamage())) {
				return true;
			}
			if(input.getItem() == PartTypes.getFromID(part.getItemDamage()).counterpart){
				return true;
			}
			if(fletching != null && part.getItem() == fletching && input.getItem() == fletching) {
				return true;
			}
			if(toolRod != null && part.getItem() == toolRod && (input.getItem() == Items.stick || input.getItem() == Items.bone)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validExtra(ItemStack input) {
		if(input == null) {
			return newExtraList.size() < 1;
		}
		for(ItemStack part : newExtraList) {
			if((part.getItem() == input.getItem()) && (part.getItemDamage() == input.getItemDamage())) {
				return true;
			}
			if(input.getItem() == PartTypes.getFromID(part.getItemDamage()).counterpart){
				return true;
			}
			if(toolRod != null && part.getItem() == toolRod && (input.getItem() == Items.stick || input.getItem() == Items.bone)) {
				return true;
			}
		}
		return false;
	}
}
