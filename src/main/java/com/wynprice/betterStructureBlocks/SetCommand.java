package com.wynprice.betterStructureBlocks;

import java.util.Arrays;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class SetCommand extends CommandBase {

	@Override
	public String getName() {
		return "st";
	}
	
	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return null;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(args.length == 0)
		{
			sender.sendMessage(new TextComponentString("Usage: /st <pos:size> <axis> <value> OR /st <pos:size> <x> <y> <z>"));
			return;
		}
		if(!Arrays.asList("pos", "size").contains(args[0]))
		{
			sender.sendMessage(new TextComponentString("Usage: /st <pos:size> <axis> <value> OR /st <pos:size> <x> <y> <z>"));
			return;
		}
		if(!Arrays.asList("x", "y", "z").contains(args[1]))
		{
			try
			{
				Integer.parseInt(args[1]);
				Integer.parseInt(args[2]);
				Integer.parseInt(args[3]);
			}
			catch (NumberFormatException e) {
				sender.sendMessage(new TextComponentString("Usage: /st <pos:size> <axis> <value> OR /st <pos:size> <x> <y> <z>"));
				return;
			}
			CustomTileEntityStructure.setx(Integer.parseInt(args[1]), args[0].equals("pos"));
			CustomTileEntityStructure.sety(Integer.parseInt(args[2]), args[0].equals("pos"));
			CustomTileEntityStructure.setz(Integer.parseInt(args[3]), args[0].equals("pos"));
			CustomTileEntityStructure.updateBlock((EntityPlayer) sender);
			return;
		}
			
		try
		{
			Integer.parseInt(args[2]);
		}
		catch (NumberFormatException e) {
			sender.sendMessage(new TextComponentString("Usage: /st <pos:size> <axis> <value> OR /st <pos:size> <x> <y> <z>"));
			return;
		}
		switch (args[1]) 
		{
		case "x":
			CustomTileEntityStructure.setx(Integer.parseInt(args[2]), args[0].equals("pos"));
			break;
			
		case "y":
			CustomTileEntityStructure.sety(Integer.parseInt(args[2]), args[0].equals("pos"));
			break;
			
		case "z":
			CustomTileEntityStructure.setz(Integer.parseInt(args[2]), args[0].equals("pos"));
			break;

		}
		CustomTileEntityStructure.updateBlock( (EntityPlayer) sender);
	}



}
