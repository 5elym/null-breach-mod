package com.notelyis.nullstep;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
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
        }
        return InteractionResult.SUCCESS;
    }

}
