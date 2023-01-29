package vexatos.tgregworks.integration.recipe.tconstruct;

import java.util.LinkedList;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

import tconstruct.library.TConstructRegistry;
import tconstruct.library.crafting.ToolRecipe;
import tconstruct.library.tools.DynamicToolPart;
import tconstruct.library.tools.ToolCore;
import tconstruct.weaponry.TinkerWeaponry;
import vexatos.tgregworks.item.ItemTGregPart;

/**
 * @author Vexatos
 */
public class TGregToolRecipe extends ToolRecipe {

    protected LinkedList<ItemTGregPart> newHeadList = new LinkedList<ItemTGregPart>();
    protected LinkedList<ItemTGregPart> newHandleList = new LinkedList<ItemTGregPart>();
    protected LinkedList<ItemTGregPart> newAccessoryList = new LinkedList<ItemTGregPart>();
    protected LinkedList<ItemTGregPart> newExtraList = new LinkedList<ItemTGregPart>();
    protected Item toolRod = TConstructRegistry.getItem("toolRod");
    protected Item fletching = TinkerWeaponry.fletching;

    public TGregToolRecipe(ItemTGregPart head, ItemTGregPart handle, ToolCore tool) {
        super(head, handle, null, null, tool);
        this.newHeadList.add(head);
        this.newHandleList.add(handle);
        result = tool;
    }

    public TGregToolRecipe(ItemTGregPart head, ItemTGregPart handle, ItemTGregPart accessory, ToolCore tool) {
        super(head, handle, accessory, null, tool);
        this.newHeadList.add(head);
        this.newHandleList.add(handle);
        if (accessory != null) {
            this.newAccessoryList.add(accessory);
        }
        result = tool;
    }

    public TGregToolRecipe(ItemTGregPart head, ItemTGregPart handle, ItemTGregPart accessory, ItemTGregPart extra,
            ToolCore tool) {
        super(head, handle, accessory, extra, tool);
        this.newHeadList.add(head);
        this.newHandleList.add(handle);
        if (accessory != null) {
            this.newAccessoryList.add(accessory);
        }
        if (extra != null) {
            this.newExtraList.add(extra);
        }
        result = tool;
    }

    public TGregToolRecipe(ItemTGregPart head, ItemTGregPart handle, Item fletching, ToolCore arrow) {
        super(head, handle, fletching, arrow);
        this.newHeadList.add(head);
        this.newHandleList.add(handle);
    }

    protected TGregToolRecipe(ItemTGregPart head, Item bowstring, ItemTGregPart accessory, ToolCore shortbow) {
        super(head, bowstring, accessory, shortbow);
        this.newHeadList.add(head);
        if (accessory != null) {
            this.newAccessoryList.add(accessory);
        }
    }

    protected TGregToolRecipe(ItemTGregPart head, ItemTGregPart handle, Item bowstring, ItemTGregPart extra,
            ToolCore shortbow) {
        super(head, handle, bowstring, extra, shortbow);
        this.newHeadList.add(head);
        this.newHandleList.add(handle);
        if (extra != null) {
            this.newExtraList.add(extra);
        }
    }

    protected TGregToolRecipe(ItemTGregPart head, Item bowstring, ItemTGregPart accessory, ItemTGregPart extra,
            ToolCore shortbow) {
        super(head, bowstring, accessory, extra, shortbow);
        this.newHeadList.add(head);
        if (accessory != null) {
            this.newAccessoryList.add(accessory);
        }
        if (extra != null) {
            this.newExtraList.add(extra);
        }
    }

    protected TGregToolRecipe(ItemTGregPart head, DynamicToolPart shaft, Item fletching, ToolCore ammo) {
        super(head, shaft, fletching, ammo);
        this.newHeadList.add(head);
    }

    @Override
    public void addHeadItem(Item head) {
        if (head instanceof ItemTGregPart) {
            this.newHeadList.add((ItemTGregPart) head);
        }
        super.addHeadItem(head);
    }

    @Override
    public void addHandleItem(Item handle) {
        if (handle instanceof ItemTGregPart) {
            this.newHandleList.add((ItemTGregPart) handle);
        }
        super.addHandleItem(handle);
    }

    @Override
    public void addAccessoryItem(Item accessory) {
        if (accessory instanceof ItemTGregPart) {
            this.newAccessoryList.add((ItemTGregPart) accessory);
        }
        super.addAccessoryItem(accessory);
    }

    @Override
    public void addExtraItem(Item extra) {
        if (extra instanceof ItemTGregPart) {
            this.newExtraList.add((ItemTGregPart) extra);
        }
        super.addExtraItem(extra);
    }

    @Override
    public boolean validHead(Item input) {
        for (ItemTGregPart part : newHeadList) {
            if ((part == input) && isEqualType(part, input)) {
                return true;
            }
            if (input == part.getType().getCounterpart()) {
                return true;
            }
        }
        return super.validHead(input);
    }

    @Override
    public boolean validHandle(Item input) {
        for (ItemTGregPart part : newHandleList) {
            if ((part == input) && isEqualType(part, input)) {
                return true;
            }
            if (input == part.getType().getCounterpart()) {
                return true;
            }
            if (toolRod != null && part.getType().getCounterpart() == toolRod
                    && (input == Items.stick || input == Items.bone)) {
                return true;
            }
        }
        return super.validHandle(input);
    }

    @Override
    public boolean validAccessory(Item input) {
        if (input == null) {
            return accessoryList.size() < 1;
        }
        if (fletching != null && accessoryList.contains(fletching) && input == fletching) {
            return true;
        }
        for (ItemTGregPart part : newAccessoryList) {
            if ((part == input) && isEqualType(part, input)) {
                return true;
            }
            if (input == part.getType().getCounterpart()) {
                return true;
            }
            if (toolRod != null && part.getType().getCounterpart() == toolRod
                    && (input == Items.stick || input == Items.bone)) {
                return true;
            }
        }
        return super.validAccessory(input);
    }

    @Override
    public boolean validExtra(Item input) {
        if (input == null) {
            return extraList.size() < 1;
        }
        for (ItemTGregPart part : newExtraList) {
            if ((part == input) && isEqualType(part, input)) {
                return true;
            }
            if (input == part.getType().getCounterpart()) {
                return true;
            }
            if (toolRod != null && part.getType().getCounterpart() == toolRod
                    && (input == Items.stick || input == Items.bone)) {
                return true;
            }
        }
        return super.validExtra(input);
    }

    protected boolean isEqualType(ItemTGregPart part, Item input) {
        if (input instanceof ItemTGregPart) {
            return part.getType() == ((ItemTGregPart) input).getType();
        }
        return input == part.getType().getCounterpart();
    }
}
