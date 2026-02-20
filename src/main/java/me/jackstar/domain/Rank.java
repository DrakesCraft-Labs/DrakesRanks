package me.jackstar.drakesranks.domain;

import java.util.ArrayList;
import java.util.List;

public class Rank {

    private final String name;
    private String prefix;
    private String suffix;
    private String color;
    private int weight;
    private final List<String> permissionNodes;

    public Rank(String name, String prefix, String suffix, String color, int weight, List<String> permissionNodes) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = color;
        this.weight = weight;
        this.permissionNodes = new ArrayList<>(permissionNodes);
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<String> getPermissionNodes() {
        return permissionNodes;
    }
}
