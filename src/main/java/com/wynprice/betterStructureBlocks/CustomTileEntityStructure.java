package com.wynprice.betterStructureBlocks;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStructure;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class CustomTileEntityStructure extends TileEntityStructure
{
    public String name = "";
    public String author = "";
    public String metadata = "";
    public BlockPos position = new BlockPos(0, 1, 0);
    public BlockPos actualPosition = new BlockPos(0, 1, 0);
    public BlockPos size = BlockPos.ORIGIN;
    private BlockPos actualSize = BlockPos.ORIGIN;
    public Mirror mirror = Mirror.NONE;
    public Rotation rotation = Rotation.NONE;
    public TileEntityStructure.Mode mode = TileEntityStructure.Mode.DATA;
    public boolean ignoreEntities = true;
    public boolean powered;
    public boolean showAir;
    public boolean showBoundingBox = true;
    public float integrity = 1.0F;
    public long seed;
	
	public void setS(BlockPos s) {
		this.size = s;
		this.actualSize = s;
		System.out.println("setting size to " + s.getX() + ", " + s.getY() + ", " + s.getZ());
		super.setSize(s);
	}
	
	public void setP(BlockPos p) {
		this.position = p;
		this.actualPosition = p;
		System.out.println("setting position to " + p.getX() + ", " + p.getY() + ", " + p.getZ());
		super.setPosition(p);
	}
	
	@Override
	public boolean save(boolean p_189712_1_) {
		System.out.println("attempting to save");
		if (this.mode == TileEntityStructure.Mode.SAVE && !this.world.isRemote && !StringUtils.isNullOrEmpty(this.name))
        {
            BlockPos blockpos = this.getPos().add(this.actualPosition);
            WorldServer worldserver = (WorldServer)this.world;
            MinecraftServer minecraftserver = this.world.getMinecraftServer();
            TemplateManager templatemanager = worldserver.getStructureTemplateManager();
            HarshenTemplate template = HarshenTemplate.getTemplate(new ResourceLocation(this.name));
            template.takeBlocksFromWorld(this.world, this.actualPosition, blockpos, this.actualSize, true, Blocks.STRUCTURE_VOID);
            template.setAuthor("Pricea1");
            return !p_189712_1_ || HarshenTemplate.saveToFile(minecraftserver, new ResourceLocation(this.name));
        }
        else
        {
            return false;
        }
	}
	
	@Override
	public boolean load(boolean p_189714_1_) {
		System.out.println("attempting to load");
		if (this.mode == TileEntityStructure.Mode.LOAD && !this.world.isRemote && !StringUtils.isNullOrEmpty(this.name))
        {
            BlockPos blockpos = this.getPos();
            BlockPos blockpos1 = blockpos.add(this.actualPosition);
            WorldServer worldserver = (WorldServer)this.world;
            MinecraftServer minecraftserver = this.world.getMinecraftServer();
            TemplateManager templatemanager = worldserver.getStructureTemplateManager();
            Template template = templatemanager.get(minecraftserver, new ResourceLocation(this.name));

            if (template == null)
            {
                return false;
            }
            else
            {
                if (!StringUtils.isNullOrEmpty(template.getAuthor()))
                {
                    this.author = template.getAuthor();
                }

                BlockPos blockpos2 = template.getSize();
                System.out.println(template.getSize());
                boolean flag = this.actualSize.equals(template.getSize());

                if (!flag)
                {
                    this.actualSize = blockpos2;
                    this.markDirty();
                    IBlockState iblockstate = this.world.getBlockState(blockpos);
                    this.world.notifyBlockUpdate(blockpos, iblockstate, iblockstate, 3);
                }

                if (p_189714_1_ && !flag)
                {
                    return false;
                }
                else
                {
                    PlacementSettings placementsettings = (new PlacementSettings()).setMirror(this.mirror).setRotation(this.rotation).setIgnoreEntities(this.ignoreEntities).setChunk((ChunkPos)null).setReplacedBlock((Block)null).setIgnoreStructureBlock(false);

                    if (this.integrity < 1.0F)
                    {
                        placementsettings.setIntegrity(MathHelper.clamp(this.integrity, 0.0F, 1.0F)).setSeed(Long.valueOf(this.seed));
                    }

                    template.addBlocksToWorldChunk(this.world, blockpos1, placementsettings);
                    return true;
                }
            }
        }
        else
        {
            return false;
        }
	}
	
	@Override
	public void nextMode() {
		switch (this.getMode())
        {
            case SAVE:
                this.setMode(TileEntityStructure.Mode.LOAD);
                break;
            case LOAD:
                this.setMode(TileEntityStructure.Mode.CORNER);
                break;
            case CORNER:
                this.setMode(TileEntityStructure.Mode.DATA);
                break;
            case DATA:
                this.setMode(TileEntityStructure.Mode.SAVE);
        }
	}
	
	@Override
	public void setMode(TileEntityStructure.Mode modeIn)
    {
		super.setMode(modeIn);
        this.mode = modeIn;
        IBlockState iblockstate = this.world.getBlockState(this.getPos());

        if (iblockstate.getBlock() instanceof CustomBlockStructure)
        {
            this.world.setBlockState(this.getPos(), iblockstate.withProperty(BlockStructure.MODE, modeIn), 2);
        }
    }

	
	@Override
	public void setName(String nameIn)
    {
        String s = nameIn;

        for (char c0 : ChatAllowedCharacters.ILLEGAL_STRUCTURE_CHARACTERS)
        {
            s = s.replace(c0, '_');
        }
        super.setName(nameIn);
        this.name = s;
    }
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setString("name", this.name);
        compound.setString("author", this.author);
        compound.setString("metadata", this.metadata);
        compound.setInteger("posX", this.position.getX());
        compound.setInteger("posY", this.position.getY());
        compound.setInteger("posZ", this.position.getZ());
        compound.setInteger("sizeX", this.size.getX());
        compound.setInteger("sizeY", this.size.getY());
        compound.setInteger("sizeZ", this.size.getZ());
        compound.setString("rotation", this.rotation.toString());
        compound.setString("mirror", this.mirror.toString());
        compound.setString("mode", this.mode.toString());
        compound.setBoolean("ignoreEntities", this.ignoreEntities);
        compound.setBoolean("powered", this.powered);
        compound.setBoolean("showair", this.showAir);
        compound.setBoolean("showboundingbox", this.showBoundingBox);
        compound.setFloat("integrity", this.integrity);
        compound.setLong("seed", this.seed);
        return compound;
    }
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.setName(compound.getString("name"));
        this.author = compound.getString("author");
        this.metadata = compound.getString("metadata");
        int i = compound.getInteger("posX");
        int j = compound.getInteger("posY");
        int k = compound.getInteger("posZ");
        this.position = new BlockPos(i, j, k);
        int l = MathHelper.clamp(compound.getInteger("sizeX"), 0, Integer.MAX_VALUE);
        int i1 = MathHelper.clamp(compound.getInteger("sizeY"), 0, Integer.MAX_VALUE);
        int j1 = MathHelper.clamp(compound.getInteger("sizeZ"), 0, Integer.MAX_VALUE);
        this.size = new BlockPos(l, i1, j1);
        super.setSize(this.size);
        super.setPosition(this.position);

        try
        {
            this.rotation = Rotation.valueOf(compound.getString("rotation"));
        }
        catch (IllegalArgumentException var11)
        {
            this.rotation = Rotation.NONE;
        }

        try
        {
            this.mirror = Mirror.valueOf(compound.getString("mirror"));
        }
        catch (IllegalArgumentException var10)
        {
            this.mirror = Mirror.NONE;
        }

        try
        {
            this.mode = TileEntityStructure.Mode.valueOf(compound.getString("mode"));
        }
        catch (IllegalArgumentException var9)
        {
            this.mode = TileEntityStructure.Mode.DATA;
        }

        this.ignoreEntities = compound.getBoolean("ignoreEntities");
        this.powered = compound.getBoolean("powered");
        this.showAir = compound.getBoolean("showair");
        this.showBoundingBox = compound.getBoolean("showboundingbox");

        if (compound.hasKey("integrity"))
        {
            this.integrity = compound.getFloat("integrity");
        }
        else
        {
            this.integrity = 1.0F;
        }

        this.seed = compound.getLong("seed");
	}
	
	
	
	public void setx(int x, boolean typeIsPos)
	{
		if(typeIsPos)
			position = new BlockPos(x, position.getY(), position.getZ());
		else
			size = new BlockPos(x, size.getY(), size.getZ());
	}
	
	public void sety(int y, boolean typeIsPos)
	{
		if(typeIsPos)
			position = new BlockPos(position.getX(), y, position.getZ());
		else
			size = new BlockPos(size.getX(), y, size.getZ());
	}
	
	public void setz(int z, boolean typeIsPos)
	{
		if(typeIsPos)
			position = new BlockPos(position.getX(), position.getY(), z);
		else
			size = new BlockPos(size.getX(), size.getY(), z);
	}
	
}
