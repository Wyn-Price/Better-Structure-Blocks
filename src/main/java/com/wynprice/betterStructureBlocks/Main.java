package com.wynprice.betterStructureBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = Main.MODID, name = Main.NAME ,version = Main.VERSION)
public class Main
{
    public static final String MODID = "sb";
    public static final String VERSION = "1.0";
    public static final String NAME = "Better Structure BLock";
    
    @SidedProxy(clientSide = "com.wynprice.betterStructureBlocks.Client", serverSide = "com.wynprice.betterStructureBlocks.Server")
    public static Server proxy;
   
    @EventHandler
    public void preinit(FMLPreInitializationEvent event)
    {
    	Block b = new CustomBlockStructure();
    	ForgeRegistries.BLOCKS.register(b);
    	ItemBlock i = new ItemBlock(b);
		i.setRegistryName(b.getRegistryName());
    	i.setMaxStackSize(64);
    	ForgeRegistries.ITEMS.register(i);
    	
    	proxy.render(b);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	GameRegistry.registerTileEntity(CustomTileEntityStructure.class, MODID + "CustomTileEntityStructure");
    }
    
    @EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new SetCommand());
	}

	public static void click(TileEntity te) {
		proxy.click(te);
	}

	public static void update(CustomTileEntityStructure te) {
		proxy.update(te);
	
	}
    


}
