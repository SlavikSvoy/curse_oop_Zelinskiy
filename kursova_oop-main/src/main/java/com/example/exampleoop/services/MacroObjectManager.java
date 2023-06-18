package com.example.exampleoop.services;

import com.example.exampleoop.gameEntities.macroObjects.Field;
import com.example.exampleoop.gameEntities.macroObjects.Forest;
import com.example.exampleoop.gameEntities.macroObjects.City;

import java.util.ArrayList;
import java.util.List;

public class MacroObjectManager {
    private static final List<City> citys = new ArrayList<>();
    private static final List<Forest> forests = new ArrayList<>();
    private static final List<Field> fields = new ArrayList<>();

    public static void addMacroObject(Forest forest) {
        forests.add(forest);
    }

    public static void addMacroObject(Field field) {
        fields.add(field);
    }

    public static void addMacroObject(City city) {
        citys.add(city);
    }

    public static List<City> getCitys() {
        return citys;
    }

    public static List<Forest> getForests() {
        return forests;
    }

    public static List<Field> getFields() {
        return fields;
    }
}
