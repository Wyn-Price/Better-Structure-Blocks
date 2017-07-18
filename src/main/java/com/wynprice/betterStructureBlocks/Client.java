package com.wynprice.betterStructureBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraftforge.client.model.ModelLoader;

public class Client extends Server
{
	@Override
	public void render(Block b) {
		b.setCreativeTab(new CreativeTabs(CreativeTabs.getNextID(), "sb") {
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(b);
			}
		});
    	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(b), 0, new ModelResourceLocation(b.getRegistryName(), "inventory"));

	}
	
	@Override
	public void click(TileEntity te) {
		Minecraft.getMinecraft().displayGuiScreen(new CustomGuiStructure((TileEntityStructure) te));
	}
	
	@Override
	public void update(CustomTileEntityStructure te) {
		new CustomGuiStructure(te).f(CustomTileEntityStructure.position, CustomTileEntityStructure.size, CustomTileEntityStructure.mirror, 
				CustomTileEntityStructure.rotation, CustomTileEntityStructure.mode.toString(), true, true,
				false, CustomTileEntityStructure.integrity, CustomTileEntityStructure.seed);
	}
}
