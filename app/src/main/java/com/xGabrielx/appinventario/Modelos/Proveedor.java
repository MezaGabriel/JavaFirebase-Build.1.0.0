package com.xGabrielx.appinventario.Modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Proveedor {

    public static int item;
    public String id;
    public String nomProveedor;
    public String telefono;
    public String email;

    public static  List<Proveedor> ITEMS = new ArrayList<Proveedor>();
    public static  Map<String, Proveedor> ITEM_MAP = new HashMap<String, Proveedor>();

    public static final int COUNT = 0;

    public Proveedor() {
    }

    public Proveedor(String id, String nomProveedor, String telefono, String email) {
        this.id = id;
        this.nomProveedor = nomProveedor;
        this.telefono = telefono;
        this.email = email;
    }

    public static void addItem(Proveedor item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static void updateItem (Proveedor proveedor){
        ITEMS.set(ITEMS.indexOf(proveedor),proveedor);
        ITEM_MAP.put(proveedor.getId(), proveedor);
    }

    public static void deleteItem (Proveedor proveedor){
        ITEMS.remove(proveedor);
        ITEM_MAP.remove(proveedor);
    }

    public String getNomProveedor() {
        return nomProveedor;
    }

    public void setNomProveedor(String nomProveedor) {
        this.nomProveedor = nomProveedor;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId(){
        return id;
    }

    public void setId(String id) { this.id = id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proveedor proveedor = (Proveedor) o;
        return Objects.equals(id, proveedor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
