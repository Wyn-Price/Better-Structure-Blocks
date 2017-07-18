package com.wynprice.betterStructureBlocks;

import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CustomBlockStructure extends BlockStructure
{
	private int x = 0, y = 0, z = 0;
	public CustomBlockStructure() {
		setRegistryName("struc");
		setUnlocalizedName("struc");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new CustomTileEntityStructure();
	}
	
	@Override
	 public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	 {
		Main.click(worldIn.getTileEntity(pos));
		return true;
	 }
	
	
}
