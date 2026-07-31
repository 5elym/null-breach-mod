package com.notelyis.nullstep;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

public class NullStepDeviceItem extends Item {

    public NullStepDeviceItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide()) {
            player.sendSystemMessage(Component.literal("Null Step Device Activated"));
            ItemStack itemStack = player.getItemInHand(hand);

            // TODO: Add timer to auto teleport after 10 seconds
            // TODO: Add noclip flying effect after right click
            // TODO: Add cooldown to prevent spamming

            if (itemStack.has(NullStep.NSD_SAVED_LOCATION)) {
                SavedLocation savedLocation = itemStack.get(NullStep.NSD_SAVED_LOCATION);

                ServerLevel serverWorld = (ServerLevel) world;
                ServerPlayer serverPlayer = (ServerPlayer) player;

                TeleportTransition transition = new TeleportTransition(
                        serverWorld,
                        savedLocation.pos(),
                        Vec3.ZERO, // Momentum
                        savedLocation.yaw(),
                        savedLocation.pitch(),
                        TeleportTransition.PLACE_PORTAL_TICKET);

                serverPlayer.teleport(transition);

                player.sendSystemMessage(Component.literal("Teleported to saved position!"));
                itemStack.remove(NullStep.NSD_SAVED_LOCATION);

            } else {
                itemStack.set(NullStep.NSD_SAVED_LOCATION,
                        new SavedLocation(player.position(), player.getYRot(), player.getXRot()));
                player.sendSystemMessage(Component.literal("Location Locked: " + player.position().toString()));
            }
        }
        return InteractionResult.SUCCESS;
    }

}
