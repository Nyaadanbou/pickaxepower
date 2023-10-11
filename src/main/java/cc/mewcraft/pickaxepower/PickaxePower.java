package cc.mewcraft.pickaxepower;

import cc.mewcraft.pickaxepower.listener.PacketListener;
import cc.mewcraft.pickaxepower.listener.PlayerListener;
import cc.mewcraft.spatula.message.Translations;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Singleton;

public class PickaxePower extends ExtendedJavaPlugin {

    @Override protected void enable() {
        saveDefaultConfig();
        reloadConfig();

        // Configure bindings
        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override protected void configure() {
                bind(JavaPlugin.class).toInstance(PickaxePower.this);
                bind(PickaxePower.class).toInstance(PickaxePower.this);
                bind(LoreWriter.class).to(LoreWriterImp.class);
                bind(PowerResolver.class).to(PowerResolverImp.class);
                bind(Translations.class).toProvider(() ->
                        new Translations(PickaxePower.this, "languages")
                ).in(Singleton.class);
            }
        });

        // Register packet listener
        bind(injector.getInstance(PacketListener.class));

        // Register player listener
        registerListener(injector.getInstance(PlayerListener.class));

        try {
            CommandRegistry commandRegistry = injector.getInstance(CommandRegistry.class);
            commandRegistry.prepareCommand(commandRegistry
                    .commandBuilder("pickaxepower")
                    .literal("reload")
                    .permission("pickaxepower.command.reload")
                    .handler(context -> {
                        onDisable();
                        onEnable();
                        context.getSender().sendRichMessage("<aqua>" + getName() + " has been reloaded!");
                    })
                    .build());
            commandRegistry.registerCommands();
        } catch (Exception e) {
            e.printStackTrace();
            getSLF4JLogger().error("Failed to register commands!");
        }
    }

}
