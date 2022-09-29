package com.xGabrielx.appinventario.Modelos;

public class Reportes {

    public String fechaVenta;
    public long total;

    public Reportes(){
    }


    public Reportes(String fechaVenta, long total) {
        this.fechaVenta = fechaVenta;
        this.total = total;
    }

    public String  getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String  fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
