package com.notelyis.nullstep;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NullStepDeviceItem extends Item {

    public NullStepDeviceItem(Properties properties) {
        super(properties);
        // TODO Auto-generated constructor stub
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide()) {
            player.sendSystemMessage(Component.literal("Null Step Device Activated"));
            ItemStack itemStack = player.getItemInHand(hand);

            // TODO: Add timer to auto teleport after 10 seconds
            // TODO: Add noclip flying effect after right click
            // TODO: Add cooldown to prevent spamming

            if (itemStack.has(NullStep.NSD_POS)) {
                BlockPos savedPos = itemStack.get(NullStep.NSD_POS);
                player.teleportTo(savedPos.getX() + 0.5, savedPos.getY(), savedPos.getZ() + 0.5);
                player.sendSystemMessage(
                        Component.literal("Teleported to saved position: " + savedPos.toShortString()));
                itemStack.remove(NullStep.NSD_POS);

            } else {
                BlockPos playerPos = player.blockPosition();
                itemStack.set(NullStep.NSD_POS, playerPos);
                player.sendSystemMessage(Component.literal("Location Locked: " + playerPos.toShortString()));
            }
        }
        return InteractionResult.SUCCESS;
    }

}
