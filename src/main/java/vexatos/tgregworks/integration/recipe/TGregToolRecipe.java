package vexatos.tgregworks.integration.recipe;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.ToolRecipe;
import tconstruct.library.tools.ToolCore;
import tconstruct.tools.TinkerTools;
import vexatos.tgregworks.item.ItemTGregPart;

import java.util.LinkedList;

/**
 * @author Vexatos
 */
public class TGregToolRecipe extends ToolRecipe {

	protected LinkedList<ItemTGregPart> newHeadList = new LinkedList<ItemTGregPart>();
	protected LinkedList<ItemTGregPart> newHandleList = new LinkedList<ItemTGregPart>();
	protected LinkedList<ItemTGregPart> newAccessoryList = new LinkedList<ItemTGregPart>();
	protected LinkedList<ItemTGregPart> newExtraList = new LinkedList<ItemTGregPart>();
	protected Item toolRod = TConstructRegistry.getItem("toolRod");
	protected Item fletching = TinkerTools.fletching;

	public TGregToolRecipe(ItemTGregPart head, ItemTGregPart handle, ToolCore tool) {
		super(head, handle, null, null, tool);
		this.newHeadList.add(head);
		this.newHandleList.add(handle);
		result = tool;
	}

	@SuppressWarnings("ConstantConditions")
	public TGregToolRecipe(ItemTGregPart head, ItemTGregPart handle, ItemTGregPart accessory, ToolCore tool) {
		super(head, handle, accessory, null, tool);
		this.newHeadList.add(head);
		this.newHandleList.add(handle);
		if(accessory != null) {
			this.newAccessoryList.add(accessory);
		}
		result = tool;
	}

	@SuppressWarnings("ConstantConditions")
	public TGregToolRecipe(ItemTGregPart head, ItemTGregPart handle, ItemTGregPart accessory, ItemTGregPart extra, ToolCore tool) {
		super(head, handle, accessory, extra, tool);
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

	public TGregToolRecipe(ItemTGregPart head, ItemTGregPart handle, Item fletching, ToolCore arrow) {
		super(head, handle, fletching, arrow);
	}

	public TGregToolRecipe(ItemTGregPart head, Item bowstring, ItemTGregPart accessory, ToolCore shortbow) {
		super(head, bowstring, accessory, shortbow);
	}

	@Override
	public void addHeadItem(Item head) {
		if(head instanceof ItemTGregPart) {
			this.newHeadList.add((ItemTGregPart) head);
		}
		super.addHeadItem(head);
	}

	@Override
	public void addHandleItem(Item handle) {
		if(handle instanceof ItemTGregPart) {
			this.newHandleList.add((ItemTGregPart) handle);
		}
		super.addHandleItem(handle);
	}

	@Override
	public void addAccessoryItem(Item accessory) {
		if(accessory instanceof ItemTGregPart) {
			this.newAccessoryList.add((ItemTGregPart) accessory);
		}
		super.addAccessoryItem(accessory);
	}

	@Override
	public void addExtraItem(Item extra) {
		if(extra instanceof ItemTGregPart) {
			this.newExtraList.add((ItemTGregPart) extra);
		}
		super.addExtraItem(extra);
	}

	@Override
	public boolean validHead(Item input) {
		for(ItemTGregPart part : newHeadList) {
			if((part == input) && isEqualType(part, input)) {
				return true;
			}
			if(input == part.getType().counterpart) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validHandle(Item input) {
		for(ItemTGregPart part : newHandleList) {
			if((part == input) && isEqualType(part, input)) {
				return true;
			}
			if(input == part.getType().counterpart) {
				return true;
			}
			if(toolRod != null && part.getType().counterpart == toolRod && (input == Items.stick || input == Items.bone)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validAccessory(Item input) {
		if(input == null) {
			return newAccessoryList.size() < 1;
		}
		for(ItemTGregPart part : newAccessoryList) {
			if((part == input) && isEqualType(part, input)) {
				return true;
			}
			if(input == part.getType().counterpart) {
				return true;
			}
			if(fletching != null && accessoryList.contains(fletching) && input == fletching) {
				return true;
			}
			if(toolRod != null && part.getType().counterpart == toolRod && (input == Items.stick || input == Items.bone)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean validExtra(Item input) {
		if(input == null) {
			return newExtraList.size() < 1;
		}
		for(ItemTGregPart part : newExtraList) {
			if((part == input) && isEqualType(part, input)) {
				return true;
			}
			if(input == part.getType().counterpart) {
				return true;
			}
			if(toolRod != null && part.getType().counterpart == toolRod && (input == Items.stick || input == Items.bone)) {
				return true;
			}
		}
		return false;
	}

	protected boolean isEqualType(ItemTGregPart part, Item input) {
		if(input instanceof ItemTGregPart) {
			return part.getType() == ((ItemTGregPart) input).getType();
		}
		return input == part.getType().counterpart;
	}
}
