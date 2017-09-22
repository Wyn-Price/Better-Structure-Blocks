package com.wynprice.betterStructureBlocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.Vec3d;
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
		ArrayList<Double> allDoubs = new ArrayList<>();
		HashMap<Double, CustomTileEntityStructure> tileMap = new HashMap<>();
		for(TileEntity te : sender.getEntityWorld().loadedTileEntityList)
			if(te instanceof CustomTileEntityStructure)
			{
				double distance = sender.getPositionVector().distanceTo(new Vec3d(te.getPos()));
				allDoubs.add(distance);
				tileMap.put(distance, (CustomTileEntityStructure) te);
			}
		Collections.sort(allDoubs);
		if(allDoubs.isEmpty())
		{
			sender.sendMessage(new TextComponentString("No TileEnties found"));
			return;
		}
		CustomTileEntityStructure te = tileMap.get(allDoubs.get(0));
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
			
			te.setx(Integer.parseInt(args[1]), args[0].equals("pos"));
			te.sety(Integer.parseInt(args[2]), args[0].equals("pos"));
			te.setz(Integer.parseInt(args[3]), args[0].equals("pos"));
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
			te.setx(Integer.parseInt(args[2]), args[0].equals("pos"));
			break;
			
		case "y":
			te.sety(Integer.parseInt(args[2]), args[0].equals("pos"));
			break;
			
		case "z":
			te.setz(Integer.parseInt(args[2]), args[0].equals("pos"));
			break;

		}
	}



}
