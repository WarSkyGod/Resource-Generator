package top.craft_hello.ResourceGenerator.tabcomplete;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.craft_hello.ResourceGenerator.ResourceGenerator;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RgTab implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Console){
            return new ArrayList<>();
        }
        Player player = (Player) sender;

        switch (args.length){
            case 1:
                List<String> rgTab = new ArrayList<>();
                rgTab.add("addChestSize");
                rgTab.add("getChestSize");
                rgTab.add("addItem");
                rgTab.add("getItem");
                return rgTab;
            case 2:
                rgTab = new ArrayList<>();
                switch (args[0]){
                    case "addChestSize":
                    case "getChestSize":
                        rgTab.add("请输入一个数字");
                        break;
                    case "addItem":


                        for (ItemStack itemStack : player.getInventory()) {
                            if (itemStack != null){
                                rgTab.add(itemStack.getType().name());
                            }
                        }

                        if (rgTab.isEmpty()){
                            rgTab.add("背包内无可存入物品");
                        }
                        break;

                    case "getItem":

                       File chestFile =  new File(ResourceGenerator.getPlugin(ResourceGenerator.class).getDataFolder() + "/chest", player.getName() + ".yml");
                       FileConfiguration chestFileConfiguration = YamlConfiguration.loadConfiguration(chestFile);
                       HashMap<String, Object> items = chestFileConfiguration.contains("items") ? new HashMap<>(chestFileConfiguration.getConfigurationSection("items").getValues(false)) : new HashMap<>();
                       items.forEach(
                               (key, value) -> rgTab.add(key)
                       );

                       if(rgTab.isEmpty()){
                           rgTab.add("仓库内无可取出物品");
                       }
                       break;
                    default:
                        break;
                }
                return rgTab;
        }
        return new ArrayList<>();
    }
}
