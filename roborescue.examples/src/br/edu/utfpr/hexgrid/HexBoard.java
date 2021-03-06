package br.edu.utfpr.hexgrid;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 *
 * @author Bernardo
 * Inspirado por: http://www.redblobgames.com/grids/hexagons/
 */
public class HexBoard implements Serializable {
    ArrayList<ArrayList<Hex>> board;

    public ArrayList<ArrayList<Hex>> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<ArrayList<Hex>> board) {
        this.board = board;
    }
    
    public HexBoard(ArrayList<Pos> aliados, ArrayList<Pos> inimigos, Pos objetivo, int raiobarreira, boolean lrta){
        //instanciar os vertices
        board = new ArrayList();
        for(int i = 0; i < 41; i++)
            board.add(new ArrayList());
        
        for(int i = 0; i < 41; i++)        
            for(int j = 0; j < 25; j++)
                    board.get(i).add(new Hex(i, j));
        
        Hex goal = new Hex(objetivo.getX(),objetivo.getY());
       
       //Heuristica utilizada: distancia entre dois pontos
       for(int i = 0; i < 41; i++){
           for(int j = 0; j < 25; j++){
               board.get(i).get(j).setH((int) (Math.sqrt((Math.pow(goal.getX()-i, 2) + Math.pow(goal.getY() - j, 2)) * 60)));
           }
        }
       
       if(lrta){
           loadBoard();
       }
       
        for(int i = 0; i < 41; i++)        
            for(int j = 0; j < 25; j++){
               if((j % 2) > 0){
                   if(j > 0)
                        board.get(i).get(j).getVizinhos().add(board.get(i).get(j-1));
                   if(i < 40 && j > 0)
                        board.get(i).get(j).getVizinhos().add(board.get(i+1).get(j-1));
                   if(i > 0)                   
                        board.get(i).get(j).getVizinhos().add(board.get(i-1).get(j));
                   if(j < 24)
                        board.get(i).get(j).getVizinhos().add(board.get(i).get(j+1));
                   if(i < 40 && j < 24)
                        board.get(i).get(j).getVizinhos().add(board.get(i+1).get(j+1));
                   if(i < 40)
                        board.get(i).get(j).getVizinhos().add(board.get(i+1).get(j));                   
               }
               else{
                   if(i > 0 && j > 0)  
                        board.get(i).get(j).getVizinhos().add(board.get(i-1).get(j-1));
                   if(j > 0)
                        board.get(i).get(j).getVizinhos().add(board.get(i).get(j-1));
                   if(i < 40)
                        board.get(i).get(j).getVizinhos().add(board.get(i+1).get(j));
                   if(j < 24)
                        board.get(i).get(j).getVizinhos().add(board.get(i).get(j+1));
                   if(i > 0 && j < 24)
                        board.get(i).get(j).getVizinhos().add(board.get(i-1).get(j+1));
                   if(i > 0)
                        board.get(i).get(j).getVizinhos().add(board.get(i-1).get(j));
            }
        }       
       
       //TODO marcar as células de barreira como bloqueadas
       this.barreira(aliados, inimigos, raiobarreira);
       
       
        
    }
    
    private void barreira(ArrayList<Pos> aliados, ArrayList<Pos> inimigos, int raiobarreira){
        for(Hex h : board.get(0)){
            h.setBlock(true);
        }
        /*for(Hex h : board.get(board.size()-1)){
            h.setBlock(true);
        }*/  
        for(int j = 0; j < 25; j++){
                board.get(j).get(0).setBlock(true); 
                board.get(j).get(1).setBlock(true);
        }
        for(int j = 0; j < 41; j++){
                board.get(j).get(0).setBlock(true);
                board.get(j).get(1).setBlock(true);
                board.get(j).get(board.get(j).size() -1 ).setBlock(true);
                board.get(j).get(board.get(j).size() -2 ).setBlock(true);
        }
        
        for(Pos p : aliados){
            this.board.get(p.x).get(p.y).setBlock(true);
            for(Hex v: this.board.get(p.x).get(p.y).getVizinhos())
                v.setBlock(true);
        }
        for(Pos p : inimigos){
            this.board.get(p.x).get(p.y).setBlock(true);
            ArrayList<Hex> doBlock = this.board.get(p.x).get(p.y).getVizinhos();
            for(int i = 0; i < raiobarreira; i++){
                ArrayList<Hex> toAdd = new ArrayList();
                for(Hex h: doBlock){
                    h.setBlock(true);
                    if(i + 1 <= raiobarreira)
                        toAdd.addAll(h.getVizinhos());
                }
                doBlock.clear();
                if(i + 1 <= raiobarreira)
                    doBlock.addAll(toAdd);
            }
        }
    }
    
    public ArrayDeque<Pos> Astar(int sx, int sy, int gx, int gy){
        ArrayDeque<Pos> plano = new ArrayDeque();
        ArrayList<Hex> closed = new ArrayList();
        Hex current = board.get(sx).get(sy);
        ArrayList<Hex> open = new ArrayList();
        open.add(current);
        int custo = 1;
        
        while(!open.isEmpty()){         
            Hex menorf = open.get(0);
            for(Hex h: open){
                if(h.getF() < menorf.getF()){
                    menorf = h;
                }
            }
            current = menorf;
            open.remove(current);
            if(open.isEmpty()){
                System.out.println("ESVAZIOU O OPEN!!!!!!");
            }
            closed.add(current);
            for(Hex v: current.getVizinhos()){
                if(!closed.contains(v) && !v.isBlock()){
                    if(!open.contains(v) || v.getG() > (current.getG() + 1)){
                        if(!open.contains(v))
                            open.add(v);
                        v.setParent(current);
                        v.setG(current.getG() + 1);
                    }
                    else if(current.getG() + 1 < v.getF() ){
                            v.setG(current.getG() + 1);
                            v.setParent(current);
                    }
                }
            }            
            if(current.getX() == gx && current.getY() == gy){
                System.out.println("A* CHEGOU EM GOAL!!!!");
                while(current.getX() != sx || current.getY() != sy){
                    System.out.println("[" + current.getX() + "," + current.getY() + "]" + " F:" + current.getF());
                    plano.push(new Pos(current.getX(), current.getY()));
                    custo++;
                    current = current.getParent();
                }
                System.out.println("O custo do caminho é:" + custo);
                System.out.println("A disância entre os dois pontos é:" + ((int) (Math.sqrt((Math.pow(gx - sx, 2) + Math.pow(gy - sy, 2))))));
                return plano;
            }
        }
        System.out.println("NÃO ACHOU CAMINHO; FALHOU");
        return new ArrayDeque<>();
    }
    
    //TODO ler do arquivo
    public Pos LRTAstar(int sx, int sy, int gx, int gy){
        Hex current = board.get(sx).get(sy);
        Hex last = current;
        current.setInflacao(current.getInflacao() + 5);
        for(Hex v: current.getVizinhos()){
            v.setInflacao(v.getInflacao() + 5);
        }
        Hex menorf = current.getVizinhos().get(0);
        for(Hex w: current.getVizinhos()){
            if(w.getH() + w.getInflacao() < menorf.getH() + menorf.getInflacao() && !w.isBlock()){
                menorf = w;
            }
            else if(w.getH() + w.getInflacao() == menorf.getH() + menorf.getInflacao() && !w.isBlock()){
                double r =  Math.random();
                if(r > 0.5){
                    menorf = w;
                }
            }
        }
        if(menorf.getX() == gx && menorf.getY() == gy)
            return new Pos(-1, -1);
        return new Pos(menorf.getX(), menorf.getY());
    }
    
    public void saveBoard(){
      ArrayList<ArrayList<Integer>> inflacoes = new ArrayList();
      for(int i = 0; i < 41; i++)
            inflacoes.add(new ArrayList());
      
        for(int i = 0; i < 41; i++)        
            for(int j = 0; j < 25; j++)
                    inflacoes.get(i).add(board.get(i).get(j).getInflacao());      
      
      try{
            FileOutputStream fileOut = new FileOutputStream("board.ser"); 
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(inflacoes);
            System.out.println("Serialized data is saved in board.ser");
            fileOut.close();
            out.close();              
      }
      catch(IOException i)
      {
          System.out.println("NÃO DEU PRA SALVAR");
      }
    }
    
    private void loadBoard(){
        ArrayList<ArrayList<Integer>> inflacoes = new ArrayList();
        try
        {
            FileInputStream fis = new FileInputStream("board.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            inflacoes = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
         }catch(IOException ioe){
             System.out.println("Arquivo não encontrado");
             return;
          }catch(ClassNotFoundException c){
             System.out.println("Class not found");
             return;
          }        
        
        for(int i = 0; i < 41; i++)        
            for(int j = 0; j < 25; j++){
                    board.get(i).get(j).setInflacao(inflacoes.get(i).get(j));
        }
        
        
    }
    
    public void printBlock(){
        for(ArrayList<Hex> h: this.getBoard()){
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