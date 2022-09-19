package fr.neyuux.refont.lg.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomItemStack extends ItemStack {

    private static final List<CustomItemStack> itemList = new ArrayList<>();

    public CustomItemStack(Material m){
        super(m);
        itemList.add(this);
    }

    public CustomItemStack(Material m, int amount){
        super(m, amount);
        addItemInList(this);
    }

    public CustomItemStack(Material m, int amount, byte data) {
        super(m, amount, data);
        addItemInList(this);
    }

    public CustomItemStack(Material m, int amount,String string){
        super(m, amount);
        addItemInList(this);
        this.setDisplayName(string);
    }

    public CustomItemStack(ItemStack i){
        super(i);
        addItemInList(this);
    }

    public void use(HumanEntity player, Event event) {
    }

    public CustomItemStack putAmount(int amount){
        this.setAmount(amount);
        return this;
    }

    public CustomItemStack setDamage(int damage){
        this.setDurability((short) damage);
        return this;
    }

    public CustomItemStack setDisplayName(String displayname){
        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(displayname);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack setSkullOwner(String owner){
        String display = getDisplayName();
        SkullMeta im = (SkullMeta)this.getItemMeta();
        im.setDisplayName(display);
        im.setOwner(owner);
        this.setItemMeta(im);
        return this;
    }

    public CustomItemStack setLore(List<String> lore){
        ItemMeta meta = this.getItemMeta();
        meta.setLore(lore);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack setLoreLine(int line, String text) {
        ItemMeta meta = this.getItemMeta();
        List<String> lore = meta.getLore();
        lore.set(line, text);
        meta.setLore(lore);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack addLore(String text) {
        ItemMeta meta = this.getItemMeta();
        List<String> lore = meta.getLore();
        lore.add(text);
        meta.setLore(lore);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack setTypeV(Material type){
        this.setType(type);
        return this;
    }

    public CustomItemStack addItemFlags(ItemFlag... flags) {
        ItemMeta meta = this.getItemMeta();
        meta.addItemFlags(flags);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack removeItemFlags(ItemFlag... flags) {
        ItemMeta meta = this.getItemMeta();
        meta.removeItemFlags(flags);
        this.setItemMeta(meta);
        return this;
    }

    public CustomItemStack setEnchantments(Map<Enchantment, Integer> enchantments){
        this.addUnsafeEnchantments(enchantments);
        return this;
    }

    public CustomItemStack addEnchantmentV(Enchantment ench, int lvl){
        this.addUnsafeEnchantment(ench, lvl);
        return this;
    }

    public CustomItemStack addGlowEffect() {
        return this.addEnchantmentV(Enchantment.DURABILITY, 1).addItemFlags(ItemFlag.HIDE_ENCHANTS);
    }

    public CustomItemStack setUnbreakable(boolean unbreakable){
        ItemMeta meta = this.getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        this.setItemMeta(meta);
        return this;
    }

    public boolean isUnbreakable(){
        return this.getItemMeta().spigot().isUnbreakable();
    }

    public String getDisplayName() {
        return this.getItemMeta().getDisplayName();
    }

    public CustomItemStack setLore(String... lore){
        return this.setLore(Arrays.asList(lore));
    }

    public CustomItemStack clone(){
        return new CustomItemStack(this);
    }

    public void updateInInv(Inventory inv) {
        int slot = -1;
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (this.isCustomSimilar(item))
                slot = i;
        }
        inv.setItem(slot, this.clone());
    }

    @SuppressWarnings("deprecation")
    public boolean isCustomSimilar(ItemStack stack) {
        if (stack == null) return false;
        else if (stack == this) return true;
        else {
            return this.getTypeId() == stack.getTypeId() && this.getDurability() == stack.getDurability() && this.hasItemMeta() == stack.hasItemMeta() && (!this.hasItemMeta() || !this.getItemMeta().hasLore() || !this.getItemMeta().hasDisplayName() || (this.getItemMeta().getDisplayName().equals(stack.getItemMeta().getDisplayName()) && this.getItemMeta().getLore().equals(stack.getItemMeta().getLore())));
        }
    }

    public static List<CustomItemStack> getItemList() {
        return itemList;
    }

    private static void addItemInList(CustomItemStack customItemStack) {
        for (CustomItemStack item : getItemList())
            if (item.isCustomSimilar(customItemStack)) return;
        getItemList().add(customItemStack);
    }
}