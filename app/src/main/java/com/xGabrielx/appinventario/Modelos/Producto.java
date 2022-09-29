package com.xGabrielx.appinventario.Modelos;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Producto {

    public int cantidad;
    public String idProd;
    public String idProducto;
    public String nomProducto;
    public String descripcion;
    public String nomProveedor;
    public String modelo;
    public String precio;
    public String almacen;
    public static int item;

    public static List<Producto> ITEMS = new ArrayList<Producto>();
    public static Map<String, Producto> ITEM_MAP = new HashMap<String, Producto>();

    public static final int COUNT = 0;

    public Producto() {
    }

    public Producto(String idProd, String idProducto, String nomProducto, String descripcion, String nomProveedor, String modelo, String precio, String almacen) {
        this.idProd = idProd;
        this.idProducto = idProducto;
        this.nomProducto = nomProducto;
        this.descripcion = descripcion;
        this.nomProveedor = nomProveedor;
        this.modelo = modelo;
        this.precio = precio;
        this.almacen = almacen;
    }

    // PARA VENTAS
    public Producto(int cantidad, String idProducto, String nomProducto, String precio) {
        this.cantidad = cantidad;
        this.idProducto = idProducto;
        this.nomProducto = nomProducto;
        this.precio = precio;
    }

    public static void addItem(Producto item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.idProd, item);
    }

    public static void updateItem (Producto producto){
        ITEMS.set(ITEMS.indexOf(producto),producto);
        ITEM_MAP.put(producto.getIdProd(), producto);
    }

    public static void deleteItem (Producto producto){
        ITEMS.remove(producto);
        ITEM_MAP.remove(producto);
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getIdProd() {
        return idProd;
    }

    public void setIdProd(String idProd) {
        this.idProd = idProd;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getNomProducto() {
        return nomProducto;
    }

    public void setNomProducto(String nomProducto) {
        this.nomProducto = nomProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNomProveedor() {
        return nomProveedor;
    }

    public void setNomProveedor(String nomProveedor) {
        this.nomProveedor = nomProveedor;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getAlmacen() {
        return almacen;
    }

    public void setAlmacen(String almacen) {
        this.almacen = almacen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(idProd, producto.idProd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProd);
    }
}
