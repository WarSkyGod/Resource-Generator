package top.craft_hello.ResourceGenerator;

import cn.handyplus.lib.adapter.HandySchedulerUtil;
import org.bukkit.plugin.java.JavaPlugin;
import top.craft_hello.ResourceGenerator.command.Rg;
import top.craft_hello.ResourceGenerator.listener.GuiChickEvent;
import top.craft_hello.ResourceGenerator.tabcomplete.RgTab;


public final class ResourceGenerator extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        HandySchedulerUtil.init(this);
        getCommand("rg").setExecutor(new Rg());
        getCommand("rg").setTabCompleter(new RgTab());
        getServer().getPluginManager().registerEvents(new GuiChickEvent(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
