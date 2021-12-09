package fr.neyuux.refont.lg.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCustomInventory {

    private final String name;
    private final int size;
    private final HashMap<Integer, CustomItemStack> itemsMap = new HashMap<>();

    public AbstractCustomInventory(String name, int size) {
        this.name = name;
        this.size = size;
    }

    public abstract void registerItems();


    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void onClick(InventoryClickEvent inventoryClickEvent) {

    }

    public void onClose(InventoryCloseEvent inventoryCloseEvent) {

    }

    public void open(HumanEntity player){
        Inventory inventory = Bukkit.createInventory(null, size, name);

        this.registerItems();
        this.itemsMap.forEach(inventory::setItem);

        player.openInventory(inventory);
    }

    public void close(Inventory inv){}

    public void setItem(int slot, CustomItemStack item){
        this.itemsMap.put(slot, item);
    }

    public CustomItemStack getItem(int slot){
        return this.itemsMap.get(slot);
    }

    public Map<Integer, CustomItemStack> getItemsMap() {
        return itemsMap;
    }


    public void setCorner(byte color, int slot, byte direction) {
        CustomItemStack glass = new CustomItemStack(Material.STAINED_GLASS, 1, color);

        this.setItem((slot), glass);

        if (direction <= 2)
            this.setItem((slot + 9), glass);
        else
            this.setItem((slot - 9), glass);

        if (direction % 2 == 0)
            this.setItem((slot - 1), glass);
        else
            this.setItem((slot + 1), glass);
    }

    public void setAllCorners(byte color) {
        setCorner(color, 0, (byte)1);

        setCorner(color, 8, (byte)2);

        setCorner(color, this.getSize() - 9, (byte)3);

        setCorner(color, this.getSize() - 1, (byte)4);
    }
}
