/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.hexgrid;

import java.util.ArrayList;

/**
 *
 * @author Bernardo
 */
public class Hex {
    private ArrayList<Hex> vizinhos;
    private int x, y;
    private int g; //custo para chegar ate ele
    private int h; //custo estimado dele até o objetivo
    private int inflacao; //incremento no g para o lrta*
    private Hex parent; //para traçar o caminho
    private boolean block; //determina se a célua está ou não bloqueada

    public Hex(int x, int y) {
        this.vizinhos = new ArrayList();
        this.x = x;
        this.y = y;
        this.g = 0;
        this.h = 0;
        this.inflacao = 0;
        parent = null;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public ArrayList<Hex> getVizinhos() {
        return vizinhos;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public int getF() {
        return g + h;
    }

    public Hex getParent() {
        return parent;
    }

    public void setParent(Hex parent) {
        this.parent = parent;
    }

    public int getInflacao() {
        return inflacao;
    }

    public void setInflacao(int inflacao) {
        this.inflacao = inflacao;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

}
