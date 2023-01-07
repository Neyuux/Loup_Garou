package fr.neyuux.lg.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCustomInventory {

    private static int totalIDs;

    private final String name;
    private int size;
    private final int id;
    private final HashMap<Integer, CustomItemStack> itemsMap = new HashMap<>();

    public AbstractCustomInventory(String name, int size) {
        this.name = name;
        this.size = size;
        this.id = nextID();
    }

    public abstract void registerItems();


    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public void adaptIntToInvSize(int i) {
        int newsize = Math.max(i, 45);

        if (i < 36) newsize = 36;
        if (i < 27) newsize = 27;
        if (i < 18) newsize = 18;
        if (i < 9) newsize = 9;

        this.size = newsize;
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
        this.itemsMap.remove(slot);
        this.itemsMap.put(slot, item);
    }

    public void addItem(CustomItemStack item) {
        int slot = -1;
        for (int i = 0; slot == -1 && i < this.size; i++) {
            if (!this.itemsMap.containsKey(i)) slot = i;
        }

        this.setItem(slot, item);
    }

    public CustomItemStack getItem(int slot){
        return this.itemsMap.get(slot);
    }

    public Map<Integer, CustomItemStack> getItemsMap() {
        return itemsMap;
    }

    public void setCorner(byte color, int slot, byte direction) {
        CustomItemStack glass = new CustomItemStack(Material.STAINED_GLASS_PANE, 1, color);

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
        this.setCorner(color, 0, (byte)1);

        this.setCorner(color, 8, (byte)2);

        this.setCorner(color, this.getSize() - 9, (byte)3);

        this.setCorner(color, this.getSize() - 1, (byte)4);
    }

    public int getID() {
        return id;
    }


    public static int nextID() {
        totalIDs++;
        return totalIDs;
    }
}
