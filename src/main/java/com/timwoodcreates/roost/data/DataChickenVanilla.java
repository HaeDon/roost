package com.timwoodcreates.roost.data;

import java.util.List;

import com.timwoodcreates.roost.RoostItems;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DataChickenVanilla extends DataChicken {

	private static final NBTTagCompound TAG_COMPOUND = new NBTTagCompound();

	static {
		TAG_COMPOUND.setInteger(MOD_ID_KEY, MOD_ID_VANILLA);
	}

	public static DataChicken getDataFromStack(ItemStack stack) {
		NBTTagCompound tagCompound = stack.getTagCompound();
		if (tagCompound == null || tagCompound.getInteger(MOD_ID_KEY) != MOD_ID_VANILLA) return null;
		return new DataChickenVanilla();
	}

	public static DataChicken getDataFromEntity(Entity entity) {
		if (entity instanceof EntityChicken) return new DataChickenVanilla();
		return null;
	}

	public static void getItemCageSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		ItemStack stack = new ItemStack(itemIn, 1, 0);
		stack.setTagCompound(TAG_COMPOUND.copy());
		subItems.add(stack);
	}

	public DataChickenVanilla() {
		super("vanilla", "entity.Chicken.name");
	}

	@Override
	public boolean isEqual(DataChicken other) {
		return (other instanceof DataChickenVanilla);
	}

	@Override
	public ItemStack createDropStack() {
		Item item = rand.nextInt(3) > 0 ? Items.FEATHER : Items.EGG;
		return new ItemStack(item, 1);
	}

	@Override
	public EntityChicken buildEntity(World world) {
		return new EntityChicken(world);
	}

	@Override
	public void spawnEntity(World world, BlockPos pos) {
		EntityChicken chicken = new EntityChicken(world);
		chicken.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
		chicken.onInitialSpawn(world.getDifficultyForLocation(pos), null);
		chicken.setGrowingAge(getLayTime());
		world.spawnEntityInWorld(chicken);
	}

	@Override
	public ItemStack buildChickenStack() {
		ItemStack stack = new ItemStack(RoostItems.ITEM_CHICKEN);
		stack.setTagCompound(TAG_COMPOUND.copy());
		return stack;
	}

	@Override
	public String toString() {
		return "DataChickenVanilla [name=" + getName() + "]";
	}

}
