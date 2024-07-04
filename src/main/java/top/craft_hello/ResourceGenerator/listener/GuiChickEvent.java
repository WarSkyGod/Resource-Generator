package top.craft_hello.ResourceGenerator.listener;

import cn.handyplus.lib.adapter.PlayerSchedulerUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import top.craft_hello.ResourceGenerator.Chest;
import top.craft_hello.ResourceGenerator.ResourceGenerator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


public class GuiChickEvent implements Listener {
    @EventHandler
    public void onGuiChickEvent(InventoryClickEvent guiChickEvent) {
        if (guiChickEvent.getWhoClicked().getOpenInventory().getTitle().equals("§6§l资源生成器")) {
            guiChickEvent.setCancelled(true);
        }

        Player player = (Player) guiChickEvent.getWhoClicked();

        switch (guiChickEvent.getRawSlot()) {
            case 1:
                PlayerSchedulerUtil.performCommand(player, "rg addChestSize 1");
                PlayerSchedulerUtil.performCommand(player, "rg");
                break;
            case 2:
                PlayerSchedulerUtil.performCommand(player, "rg getChestSize 1");
                PlayerSchedulerUtil.performCommand(player, "rg");
                break;
            case 3:
                PlayerSchedulerUtil.performCommand(player, "rg addItem DIAMOND 1");
                PlayerSchedulerUtil.performCommand(player, "rg");
                break;
            case 4:
                PlayerSchedulerUtil.performCommand(player, "rg getItem DIAMOND 1");
                PlayerSchedulerUtil.performCommand(player, "rg");
                break;
            default:
                break;
        }
    }
}
