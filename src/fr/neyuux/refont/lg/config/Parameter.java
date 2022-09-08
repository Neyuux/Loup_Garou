package fr.neyuux.refont.lg.config;

import fr.neyuux.refont.lg.utils.CustomItemStack;

public class Parameter {

    private final CustomItemStack item;

    private Object value;

    private final ParameterType parameterType;


    public Parameter(CustomItemStack item, Object value, ParameterType parameterType) {
        this.item = item;
        this.value = value;
        this.parameterType = parameterType;
    }


    public CustomItemStack getItem() {
        return this.item;
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

    public ParameterType getParameterType() {
        return this.parameterType;
    }
}

enum ParameterType {
    ROLE,
    GLOBAL
}
