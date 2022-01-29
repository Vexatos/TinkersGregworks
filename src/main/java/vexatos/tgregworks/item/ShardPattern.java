package vexatos.tgregworks.item;

import mantle.items.abstracts.CraftingItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import tconstruct.library.crafting.PatternBuilder;
import tconstruct.library.util.IPattern;

public class ShardPattern extends CraftingItem implements IPattern
{
    public ShardPattern(CreativeTabs tab) {
        this(getPatternNames(), getPatternNames(), "", "tgregworks", tab);
    }
    public ShardPattern(String[] names, String[] tex, String folder, String modTexturePrefix, CreativeTabs tab)
    {
        super(names, tex, folder, modTexturePrefix, tab);
    }

    protected static String[] getPatternNames ()
    {
        String[] names = new String[1];
        names[0] = "cast_shard";
        return names;
    }

    @Override
    public int getPatternCost(ItemStack pattern)
    {
        return 1;
    }

    @Override
    public ItemStack getPatternOutput(ItemStack pattern, ItemStack input, PatternBuilder.MaterialSet set)
    {
        return null;
    }
}
