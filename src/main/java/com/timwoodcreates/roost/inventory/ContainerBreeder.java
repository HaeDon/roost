package com.timwoodcreates.roost.inventory;

import com.timwoodcreates.roost.inventory.slot.SlotChicken;
import com.timwoodcreates.roost.inventory.slot.SlotReadOnly;
import com.timwoodcreates.roost.inventory.slot.SlotSeeds;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBreeder extends Container {

	private final IInventory breederInventory;
	private int timeUntilNextDrop;
	private int timeElapsed;

	public ContainerBreeder(InventoryPlayer playerInventory, IInventory breederInventory) {
		this.breederInventory = breederInventory;

		addSlotToContainer(new SlotChicken(breederInventory, 0, 44, 20));
		addSlotToContainer(new SlotChicken(breederInventory, 1, 62, 20));
		addSlotToContainer(new SlotSeeds(breederInventory, 2, 8, 20));

		for (int i = 0; i < 3; ++i) {
			addSlotToContainer(new SlotReadOnly(breederInventory, i + 3, 116 + i * 18, 20));
		}

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 51 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 109));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return breederInventory.isUseableByPlayer(playerIn);
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, this.breederInventory);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();

		for (int i = 0; i < listeners.size(); ++i) {
			IContainerListener listener = listeners.get(i);

			if (timeUntilNextDrop != breederInventory.getField(0)) {
				listener.sendProgressBarUpdate(this, 0, breederInventory.getField(0));
			}

			if (timeElapsed != breederInventory.getField(1)) {
				listener.sendProgressBarUpdate(this, 1, breederInventory.getField(1));
			}
		}

		timeUntilNextDrop = breederInventory.getField(0);
		timeElapsed = breederInventory.getField(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		breederInventory.setField(id, data);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int fromSlot) {
		ItemStack previous = null;
		Slot slot = inventorySlots.get(fromSlot);

		if (slot != null && slot.getHasStack()) {
			ItemStack current = slot.getStack();
			previous = current.copy();

			if (fromSlot < 6) {
				if (!mergeItemStack(current, 6, 42, true)) {
					return null;
				}
			} else if (!mergeItemStack(current, 0, 6, false)) {
				return null;
			}

			if (current.stackSize == 0) {
				slot.putStack((ItemStack) null);
			} else {
				slot.onSlotChanged();
			}

			if (current.stackSize == previous.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(playerIn, current);
		}
		return previous;
	}

}
