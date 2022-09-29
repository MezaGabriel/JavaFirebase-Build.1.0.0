package com.xGabrielx.appinventario.Modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Retiros {
    public static int item;
    public String id;
    public String nomPersona;
    public String cantidad;
    public String observacion;

    public static List<Retiros> ITEMS = new ArrayList<Retiros>();
    public static Map<String, Retiros> ITEM_MAP = new HashMap<String, Retiros>();

    public Retiros() {
    }

    public Retiros(String id, String nomPersona, String cantidad, String observacion) {
        this.id = id;
        this.nomPersona = nomPersona;
        this.cantidad = cantidad;
        this.observacion = observacion;
    }

    /*static {
        ITEMS = new ArrayList<Retiros>();
        for (int i = 1; i <= COUNT; i++) {
            addItem(createRetirosItem(i));
        }
    }*/

    public static void addItem(Retiros item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void updateItem (Retiros retiros){
        ITEMS.set(ITEMS.indexOf(retiros),retiros);
        ITEM_MAP.put(retiros.getId(), retiros);
    }

    public static void deleteItem (Retiros retiros){
        ITEMS.remove(retiros);
        ITEM_MAP.remove(retiros);
    }

    public String getNomPersona() {
        return nomPersona;
    }

    public void setNomPersona(String nomPersona) {
        this.nomPersona = nomPersona;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getId(){
        return id;
    }

    public void setId(String id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Retiros retiros = (Retiros) o;
        return Objects.equals(id, retiros.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
