/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.edu.utfpr.hexgrid;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Bernardo
 */
public class TestHex {
    
    public static void main(String[] args) {
        ArrayList<Pos> aliados = new ArrayList<>();
        ArrayList<Pos> inimigos = new ArrayList<>();
        inimigos.add(new Pos(10, 10));
        inimigos.add(new Pos(15, 15));
        inimigos.add(new Pos(20, 20));
        Pos objetivo = new Pos(40, 24);
        HexBoard board = new HexBoard(aliados, inimigos, objetivo, 2);
        Scanner scanner = new Scanner(System.in); 
        
        ArrayDeque<Pos> parent = board.Astar(5, 5, 40, 24);
        board.saveBoard();
        //teste de vizinhancas
        /*int i = scanner.nextInt();
        int i2 = scanner.nextInt();
        for(Hex g: board.getBoard().get(i).get(i2).getVizinhos()){
            System.out.print("[" + g.getX() + "," + g.getY() + "]");
        }*/
        
        //imprimir board
        /*for(Pos g: parent){
            System.out.println("[" + g.getX() + "," + g.getY() + "]");
        }*/
        
        for(ArrayList<Hex> h: board.getBoard()){
            for(Hex g: h){
                //System.out.print("[" + g.getX() + "," + g.getY() + "]");
                if(g.isBlock()){
                  System.out.print("X");
                }
                else{
                  System.out.print("Z");
                }
            }
            System.out.println("");
        }
                
                
    }
    
}
