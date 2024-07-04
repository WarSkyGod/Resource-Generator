package top.craft_hello.ResourceGenerator.command;

import cn.handyplus.lib.adapter.PlayerSchedulerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import top.craft_hello.ResourceGenerator.Chest;
import top.craft_hello.ResourceGenerator.ResourceGenerator;

import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Rg implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Console){
            sender.sendMessage("§c§l该命令不能在控制台执行");
            return true;
        }
        Player player = (Player) sender;

        try {
            File chestFile =  new File(ResourceGenerator.getPlugin(ResourceGenerator.class).getDataFolder() + "/chest", player.getName() + ".yml");
            FileConfiguration chestFileConfiguration = YamlConfiguration.loadConfiguration(chestFile);
            int chestMaxSize = chestFileConfiguration.getInt("chest_max_size") == 0 ? 0 : chestFileConfiguration.getInt("chest_max_size");
            int chestUseSize = chestFileConfiguration.getInt("chest_use_size") == 0 ? 0 : chestFileConfiguration.getInt("chest_use_size");
            chestFileConfiguration.set("chest_max_size", chestMaxSize);
            chestFileConfiguration.set("chest_use_size", chestUseSize);

            if (args.length == 0){
                Inventory gui = Bukkit.createInventory(null, 6*9, "§6§l资源生成器");
                ItemStack chest = new ItemStack(Material.CHEST);
                ItemMeta chestItemMeta = chest.getItemMeta();
                chestItemMeta.setDisplayName("§6§l当前仓库大小：" + (chestMaxSize - chestUseSize) + "/" + chestMaxSize + "格");
                chestItemMeta.setLore(Arrays.asList("§6§l当前已使用：" + chestUseSize + "格"));
                chest.setItemMeta(chestItemMeta);
                gui.setItem(0, chest);

                chestItemMeta.setDisplayName("§a§l增加仓库大小");
                chestItemMeta.setLore(Arrays.asList("§6§l每增加1728格需消耗1个箱子"));
                chest.setItemMeta(chestItemMeta);
                gui.setItem(1, chest);

                chestItemMeta.setDisplayName("§c§l减小仓库大小");
                chestItemMeta.setLore(Arrays.asList("§6§l每减小1728格返还一个箱子"));
                chest.setItemMeta(chestItemMeta);
                gui.setItem(2, chest);
                player.openInventory(gui);

                ItemStack diamond = new ItemStack(Material.DIAMOND);
                ItemMeta diamondItemMeta = diamond.getItemMeta();
                diamondItemMeta.setDisplayName("§a§l存入1颗钻石");
                diamondItemMeta.setLore(Arrays.asList("§6§l消耗1颗钻石并存入仓库"));
                diamond.setItemMeta(diamondItemMeta);
                gui.setItem(3, diamond);
                player.openInventory(gui);

                diamondItemMeta.setDisplayName("§c§l取出1颗钻石");
                diamondItemMeta.setLore(Arrays.asList("§6§l从仓库中取出1颗钻石"));
                diamond.setItemMeta(diamondItemMeta);
                gui.setItem(4, diamond);
                player.openInventory(gui);
                return true;
            }

            HashMap<String, Object> items = chestFileConfiguration.contains("items") ? new HashMap<>(chestFileConfiguration.getConfigurationSection("items").getValues(false)) : new HashMap<>();
            Chest chest = new Chest(player, chestMaxSize, chestUseSize, items);

            switch (args[0]){
                case "addChestSize":
                    Integer itemAmount = Integer.valueOf(args[1]);
                    if(chest.increaseChestMaxSize(itemAmount)){
                        chestFileConfiguration.set("chest_max_size", chest.getChestMaxSize());
                        chestFileConfiguration.save(chestFile);
                    }
                    break;
                case "getChestSize":
                    itemAmount = Integer.valueOf(args[1]);
                    if(chest.decreaseChestMaxSize(itemAmount)){
                        chestFileConfiguration.set("chest_max_size", chest.getChestMaxSize());
                        chestFileConfiguration.save(chestFile);
                    }
                    break;
                case "addItem":
                    if(args.length == 2){
                        if(player.getItemInHand().isEmpty()){
                            player.sendMessage("§a§l> §c§l请手持一个物品！");
                            break;
                        }
                        itemAmount = Integer.valueOf(args[1]);

                        if(chest.inputItems(player.getItemInHand().getType(), itemAmount)){
                            chestFileConfiguration.set("chest_use_size", chest.getChestUseSize());
                            chestFileConfiguration.set("items", chest.getItems());
                            chestFileConfiguration.save(chestFile);
                        }
                        break;
                    }

                    if (args.length == 3){
                        Material item = Material.matchMaterial(args[1]);
                        itemAmount = Integer.valueOf(args[2]);
                        if(chest.inputItems(item, itemAmount)){
                            chestFileConfiguration.set("chest_use_size", chest.getChestUseSize());
                            chestFileConfiguration.set("items", chest.getItems());
                            chestFileConfiguration.save(chestFile);
                        }
                        break;
                    }
                case "getItem":
                    Material item = Material.matchMaterial(args[1]);
                    itemAmount = Integer.valueOf(args[2]);
                    if(chest.outputItems(item, itemAmount)){
                        chestFileConfiguration.set("chest_use_size", chest.getChestUseSize());
                        chestFileConfiguration.set("items", chest.getItems());
                        chestFileConfiguration.save(chestFile);
                    }
                    break;
            }
        } catch (NumberFormatException e) {
            player.sendMessage("§a§l> §c§l物品数量错误！");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e){
            player.sendMessage("§a§l> §c§l物品名称错误！");
        }catch (ArrayIndexOutOfBoundsException e){
            player.sendMessage("§a§l> §c§l请检查命令是否完整！");
        }
        return true;
    }
}
