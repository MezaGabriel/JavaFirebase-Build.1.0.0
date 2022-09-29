package com.xGabrielx.appinventario.Modelos;

import java.util.Objects;

public class Venta {

    public String idVenta;
    public String idProductos;
    public String fechaVenta;
    public String total;

    public Venta(){
    }

    public Venta(String idVenta, String idProductos, String fechaVenta, String total) {
        this.idVenta = idVenta;
        this.idProductos = idProductos;
        this.fechaVenta = fechaVenta;
        this.total = total;
    }

    public Venta(String fechaVenta, String total) {
        this.fechaVenta = String.valueOf(fechaVenta);
        this.total = total;
    }

    public String getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(String idVenta) {
        this.idVenta = idVenta;
    }

    public String getIdProductos() {
        return idProductos;
    }

    public void setIdProductos(String idProductos) {
        this.idProductos = idProductos;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venta venta = (Venta) o;
        return idVenta == venta.idVenta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVenta);
    }
}
