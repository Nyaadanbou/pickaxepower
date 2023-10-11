package cc.mewcraft.pickaxepower.listener;

import cc.mewcraft.pickaxepower.PowerData;
import cc.mewcraft.pickaxepower.PowerResolver;
import cc.mewcraft.spatula.message.Translations;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

import org.checkerframework.checker.nullness.qual.NonNull;

public class PlayerListener implements Listener {

    private final Translations translations;
    private final PowerResolver powerResolver;

    @Inject
    public PlayerListener(
            @NonNull final Translations translations,
            @NonNull final PowerResolver powerResolver
    ) {
        this.translations = translations;
        this.powerResolver = powerResolver;
    }

    // TODO handle explosion?
    // TODO handle piston?

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        int heldItemSlot = player.getInventory().getHeldItemSlot();
        ItemStack heldItem = player.getInventory().getItem(heldItemSlot);
        if (heldItem == null) {
            return;
        }

        PowerData pickaxePower = powerResolver.resolve(heldItem);
        PowerData blockPower = powerResolver.resolve(block);

        if (pickaxePower.power() < blockPower.power()) {
            event.setCancelled(true);
            event.setDropItems(false);
            translations.of("msg_not_enough_pickaxe_power")
                    .resolver(Placeholder.component("power", blockPower.powerComponent()))
                    .resolver(Placeholder.component("block", blockPower.nameLiteralComponent()))
                    .actionBar(player);
        }
    }

}
