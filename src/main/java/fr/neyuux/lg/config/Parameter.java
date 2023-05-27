package fr.neyuux.lg.config;

import fr.neyuux.lg.utils.CustomItemStack;

public class Parameter {

    private CustomItemStack item;

    private Object value;


    public Parameter(Object value) {
        this.value = value;
    }


    public CustomItemStack getItem() {
        return this.item;
    }

    public void setItem(CustomItemStack item) {
        this.item = item;
    }

    public Object getValue() {
        return this.value;
    }

    public String getVisibleValue() {
        String value = this.value.toString();

        if (this.value instanceof Boolean) {
            if ((boolean) this.value) return "§aActivé";
            else return "§cDésactivé";
        }

        if (this.value instanceof MayorSuccession) return ((MayorSuccession) this.value).getName();

        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}

