package cc.mewcraft.pickaxepower;

import cc.mewcraft.spatula.message.Translations;
import cc.mewcraft.spatula.utils.PDCUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class LoreWriterImp implements LoreWriter {
    private static final NamespacedKey LORE_SIZE_KEY = new NamespacedKey("pickaxepower", "lore_size");

    private final PowerResolver powerResolver;
    private final Translations translations;

    @Inject
    public LoreWriterImp(
            @NonNull final PowerResolver powerResolver,
            @NonNull final Translations translations
    ) {
        this.powerResolver = powerResolver;
        this.translations = translations;
    }

    @Override
    public ItemStack update(final @Nullable ItemStack item) {
        if (item == null || item.getType().isAir() || !Tag.ITEMS_PICKAXES.isTagged(item.getType()))
            return item;

        PowerData pickaxePower = powerResolver.resolve(item);

        Component powerText = translations.of("item_lore_pickaxe_power")
                .replace("power", pickaxePower.power())
                .component();

        ItemMeta itemMeta = Objects.requireNonNull(item.getItemMeta());
        List<Component> lore = itemMeta.hasLore() ? Objects.requireNonNull(itemMeta.lore()) : new ArrayList<>();
        lore.add(powerText); // add to the bottom line
        PDCUtils.set(itemMeta, LORE_SIZE_KEY, 1);
        itemMeta.lore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    @Override
    public ItemStack revert(final @Nullable ItemStack item) {
        if (item == null || item.getType().isAir() || !Tag.ITEMS_PICKAXES.isTagged(item.getType()))
            return item;

        ItemMeta itemMeta = Objects.requireNonNull(item.getItemMeta());
        Optional<Integer> size = PDCUtils.getInt(itemMeta, LORE_SIZE_KEY);
        if (size.isEmpty())
            return item;

        List<Component> lore = itemMeta.hasLore() ? Objects.requireNonNull(itemMeta.lore()) : new ArrayList<>();
        lore.remove(lore.size() - 1);
        itemMeta.lore(lore); // remove the bottom line
        item.setItemMeta(itemMeta);
        return item;
    }
}
