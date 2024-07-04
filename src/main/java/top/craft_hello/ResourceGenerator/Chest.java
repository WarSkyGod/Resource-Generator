package top.craft_hello.ResourceGenerator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Chest {
    private Player player;
    private String ownerName;
    private String ownerUUID;
    private Integer chestMaxSize;
    private Integer chestUseSize;
    private Map<String, Object> items;



    public Chest(Player player) {
        this(player, 0);
    }

    public Chest(Player player, Integer chestMaxSize) {
        this(player, chestMaxSize, 0);
    }

    public Chest(Player player, Integer chestMaxSize, Integer chestUseSize) {
        this(player, chestMaxSize, chestUseSize, new HashMap<>());
    }

    public Chest(Player player, Integer chestMaxSize, Integer chestUseSize, Map<String, Object> items) {
        setPlayer(player);
        setChestMaxSize(chestMaxSize);
        setChestUseSize(chestUseSize);
        setItems(items);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        if (player != null) {
            this.player = player;
            this.ownerName = player.getName();
            this.ownerUUID = player.getUniqueId().toString();
        }

    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(String ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public Integer getChestMaxSize() {
        return chestMaxSize;
    }

    public void setChestMaxSize(Integer chestMaxSize) {
        if (chestMaxSize >= 0){
            this.chestMaxSize = chestMaxSize;
        }else {
            this.chestMaxSize = 0;
        }
    }

    public Integer getChestUseSize() {
        return chestUseSize;
    }

    public void setChestUseSize(Integer chestUseSize) {
        if (chestUseSize >= 0){
            this.chestUseSize = chestUseSize;
        }else {
            this.chestUseSize = 0;
        }
    }

    public Map<String, Object> getItems() {
        return items;
    }

    public void setItems(Map<String, Object> items) {
        if(items != null){
            this.items = items;
        }else {
            this.items = new HashMap<>();
        }
    }

    public boolean increaseChestMaxSize() {
        return increaseChestMaxSize(1);
    }

    public boolean increaseChestMaxSize(Integer chestNumber) {
        if(chestNumber < 1){
            player.sendMessage("§a§l> §c§l至少需要消耗一个" + Material.CHEST.name() + "！");
            return false;
        }

        if(!player.getInventory().contains(Material.CHEST, chestNumber)){
            player.sendMessage("§a§l> §c§l你背包里没有足够的" + Material.CHEST.name() + "！");
            return false;
        }

        player.getInventory().removeItem(new ItemStack(Material.CHEST, chestNumber));
        setChestMaxSize(getChestMaxSize() + 1728 * chestNumber);
        player.sendMessage("§a§l> 你成功增加了" + 1728 * chestNumber + "格仓库大小!");
        return true;
    }

    public boolean decreaseChestMaxSize() {
        return decreaseChestMaxSize(1);
    }

    public boolean decreaseChestMaxSize(Integer chestAmount) {
        if(chestAmount < 1){
            player.sendMessage("§a§l> §c§l至少需要降低一个" + Material.CHEST.name() + "的仓库大小！");
            return false;
        }

        if(getChestMaxSize() - getChestUseSize() < 1728 * chestAmount){
            player.sendMessage("§a§l> §c§l你没有足够的仓库容量!");
            return false;
        }

        if(3564 < chestAmount) {
            player.sendMessage("§a§l> §c§l你的背包无法容纳" + chestAmount + "个" + Material.CHEST.name() + "！");
            return false;
        }

        player.getInventory().addItem(new ItemStack(Material.CHEST, chestAmount));
        setChestMaxSize(getChestMaxSize() - 1728 * chestAmount);
        player.sendMessage("§a§l> 你成功减小了" + 1728 * chestAmount + "格仓库大小!");
        return true;
    }

    public boolean inputItems(Material material, Integer amount) {
        if (amount < 1) {
            player.sendMessage("§a§l> §c§l至少需要存入一个物品!");
            return false;
        }

        if (!player.getInventory().contains(material, amount)) {
            player.sendMessage("§a§l> §c§l你的背包里没有足够的" + material.name() + "!");
            return false;
        }

        if(getChestMaxSize() - getChestUseSize() < amount){
            player.sendMessage("§a§l> §c§l你没有足够的仓库容量!");
            return false;
        }


        player.getInventory().removeItem(new ItemStack(material, amount));
        setChestUseSize(getChestUseSize() + amount);
        if (items.containsKey(material.name())){
            items.replace(material.name(), (Integer)items.get(material.name()) + amount);
        }else {
            items.put(material.name(), amount);
        }

        player.sendMessage("§a§l> 你成功存入了" + amount + "个" + material.name() + "!");
        return true;
    }

    public boolean outputItems(Material material, Integer amount) {
        if (amount < 1) {
            player.sendMessage("§a§l> §c§l至少需要取出一个物品!");
            return false;
        }

        if (!items.containsKey(material.name()) || items.containsKey(material.name()) && (Integer)items.get(material.name()) < amount) {
            player.sendMessage("§a§l> §c§l你的仓库里没有足够的" + material.name() + "!");
            return false;
        }

        if(3564 < amount) {
            player.sendMessage("§a§l> §c§l你的背包无法容纳" + amount + "个" + material.name() + "！");
            return false;
        }

        player.getInventory().addItem(new ItemStack(material, amount));
        setChestUseSize(getChestUseSize() - amount);
        items.replace(material.name(), (Integer)items.get(material.name()) - amount);
        player.sendMessage("§a§l> 你成功取出了" + amount + "个" + material.name() + "!");
        return true;
    }
}
