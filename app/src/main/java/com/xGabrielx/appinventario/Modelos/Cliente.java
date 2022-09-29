package com.xGabrielx.appinventario.Modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Cliente {

    public String idCliente;
    public String nomCliente;
    public String numTelefono;
    public String email;
    public String direccion;
    public String rfc;
    public String tipoPersona;
    public String observaciones;
    public static int item;

    public static List<Cliente> ITEMS = new ArrayList<Cliente>();
    public static Map<String, Cliente> ITEM_MAP = new HashMap<String, Cliente>();

    public static final int COUNT = 0;

    public Cliente() {
    }

    public Cliente(String idCliente, String nomCliente, String numTelefono, String email, String direccion, String rfc, String tipoPersona, String observaciones) {
        this.idCliente = idCliente;
        this.nomCliente = nomCliente;
        this.numTelefono = numTelefono;
        this.email = email;
        this.direccion = direccion;
        this.rfc = rfc;
        this.tipoPersona = tipoPersona;
        this.observaciones = observaciones;
    }

    public static void addItem(Cliente item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.idCliente, item);
    }

    public static void updateItem (Cliente cliente){
        ITEMS.set(ITEMS.indexOf(cliente),cliente);
        ITEM_MAP.put(cliente.getIdCliente(), cliente);
    }

    public static void deleteItem (Cliente cliente){
        ITEMS.remove(cliente);
        ITEM_MAP.remove(cliente);
    }


    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomCliente() {
        return nomCliente;
    }

    public void setNomCliente(String nomCliente) {
        this.nomCliente = nomCliente;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(idCliente, cliente.idCliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCliente);
    }
}