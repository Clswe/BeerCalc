package com.example.beercalc.model;

import com.example.beercalc.tools.Globais;

public class Bebida {

    public int id;
    public String nome;
    public Double preco;

    public Bebida(){

    }

    public Bebida(int id, String nome, Double valor){
        this.id = id;
        this.nome = nome;
        this.preco = valor;
    }

    public int getId() {
        return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    @Override
    public String toString(){

        return this.nome + " " + Globais.formataDecimal(this.preco,2,true);
    }


    }

