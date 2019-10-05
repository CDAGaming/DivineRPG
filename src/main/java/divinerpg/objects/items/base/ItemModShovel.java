package divinerpg.objects.items.base;

import java.util.List;

import javax.annotation.Nullable;

import divinerpg.api.java.divinerpg.api.Reference;
import divinerpg.registry.DivineRPGTabs;
import divinerpg.registry.ModItems;
import divinerpg.utils.TooltipLocalizer;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemModShovel extends ItemSpade  {

	public ItemModShovel(ToolMaterial material, String name) {
		super(material);
		setRegistryName(Reference.MODID, name);
		setUnlocalizedName(name);
		setCreativeTab(DivineRPGTabs.tools);

		ModItems.ITEMS.add(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> infoList, ITooltipFlag flagIn) {
		infoList.add(TooltipLocalizer.efficiency(toolMaterial.getEfficiency()));
		if (stack.getMaxDamage() > 0) {
			infoList.add(TooltipLocalizer.usesRemaining(stack.getMaxDamage() - stack.getItemDamage()));
		} else {
			infoList.add(TooltipLocalizer.infiniteUses());
		}
	}
}