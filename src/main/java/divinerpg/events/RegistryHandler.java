package divinerpg.events;

import divinerpg.Config;
import divinerpg.client.render.*;
import divinerpg.objects.blocks.BlockStatue;
import divinerpg.registry.ModBlocks;
import divinerpg.registry.ModItems;
import divinerpg.registry.ModLiquids;
import divinerpg.registry.ModSpawns;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by LiteWolf101 on Feb /21/2019
 */
@Mod.EventBusSubscriber
public class RegistryHandler {

    public static void preInitRegistries(FMLPreInitializationEvent event) {
        ModLiquids.registerFluids();
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        ModBlocks.AddWoodVariants();
        event.getRegistry().registerAll(ModBlocks.BLOCKS.toArray(new Block[0]));
        if (Config.genJSON) {
            ModBlocks.CreateJSONs();
        }
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(ModItems.ITEMS.toArray(new Item[0]));
        if (Config.genJSON) {
            ModItems.CreateJSONs();
        }
    }

    @SubscribeEvent
    public static void onLivingSpawn(LivingSpawnEvent event) {
        ModSpawns.init(event);
    }

    @SubscribeEvent
    public static void onModelRegister(ModelRegistryEvent event) {
        for (Item item : ModItems.ITEMS) {
            registerItemModel(item);

            //FIXME: there may be a better way
            if (item.equals(Item.getItemFromBlock(ModBlocks.frostedChest))) {
                item.setTileEntityItemStackRenderer(new RenderItemFrostedChest());
            } else if (item.equals(Item.getItemFromBlock(ModBlocks.presentBox))) {
                item.setTileEntityItemStackRenderer(new RenderItemPresentBox());
            } else if (item.equals(Item.getItemFromBlock(ModBlocks.boneChest))) {
                item.setTileEntityItemStackRenderer(new RenderItemBoneChest());
            } else if (item.equals(Item.getItemFromBlock(ModBlocks.demonFurnace))) {
                item.setTileEntityItemStackRenderer(new RenderItemDemonFurnace());
            } else if (item instanceof ItemBlock && ((ItemBlock) item).getBlock() instanceof BlockStatue) {
                item.setTileEntityItemStackRenderer(new RenderItemStatue());
            } else if (item.equals(Item.getItemFromBlock(ModBlocks.edenChest))) {
                item.setTileEntityItemStackRenderer(new RenderItemEdenChest());
            } else if (item.equals(Item.getItemFromBlock(ModBlocks.arcaniumExtractor))) {
                item.setTileEntityItemStackRenderer(new RenderItemArcaniumExtractor());
            } else if (item.equals(Item.getItemFromBlock(ModBlocks.dramixAltar))) {
                item.setTileEntityItemStackRenderer(new RenderItemDramixAltar());
            } else if (item.equals(Item.getItemFromBlock(ModBlocks.parasectaAltar))) {
                item.setTileEntityItemStackRenderer(new RenderItemParasectaAltar());
            }
        }

        for (Block block : ModBlocks.BLOCKS) {
            registerItemModel(Item.getItemFromBlock(block));
        }
    }

    public static void registerItemModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

}
