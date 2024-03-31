package br.com.fatec.techninja.model;

import java.util.ArrayList;
import java.util.List;
public class Modulo {
    private String titulo;

    private Modulo(Builder builder) {
        this.titulo = builder.titulo;
    }

    public static class Builder {
        private String titulo;

        public Builder titulo(String titulo) {
            this.titulo = titulo;
            return this;
        }

        public Modulo build() {
            return new Modulo(this);
        }
    }
}