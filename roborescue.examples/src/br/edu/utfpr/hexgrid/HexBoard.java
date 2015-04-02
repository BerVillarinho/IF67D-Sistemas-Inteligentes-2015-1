package br.edu.utfpr.hexgrid;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 *
 * @author Bernardo e Maycow
 * Inspirado por: http://www.redblobgames.com/grids/hexagons/
 */
public class HexBoard implements Serializable {
    ArrayList<ArrayList<Hex>> board;
    
    public HexBoard(ArrayList<Pos> aliados, ArrayList<Pos> inimigos, Pos objetivo){
        //instanciar os vertices
        board = new ArrayList();
        for(int i = 0; i < 41; i++)
            board.add(new ArrayList());
        
        for(int i = 0; i < 41; i++)        
            for(int j = 0; j < 25; j++)
                    board.get(i).add(new Hex(i, j));
        
        board.get(3).get(10).setH(Integer.MAX_VALUE);
        
//        Hex goal = new Hex(objetivo.getX(),objetivo.getY());
//        
//        for(int i = 0; i < 41; i++){
//            for(int j = 0; j < 25; j++){
//                //board.get(i).get(j).setH(Math.abs(goal.getX()-i) + Math.abs(goal.getY() - j));
//                board.get(i).get(j).setH((goal.getX()-i) + (goal.getY() - j));
//            }
//        }
        
        ArrayList<Pos> barreira = new ArrayList();

        for(Pos p : aliados){
            barreira.add(new Pos(p.x * 60 ,p.y  * 60 ));
            barreira.add(new Pos((p.x-1) * 60,(p.y-1) * 60));
            barreira.add(new Pos(p.x * 60,(p.y-1) * 60));
            barreira.add(new Pos((p.x+1) * 60,(p.y) * 60));
            barreira.add(new Pos(p.x * 60,(p.y+1)* 60));
            barreira.add(new Pos((p.x-1)*60,(p.y+1)*60));
            barreira.add(new Pos((p.x-1) * 60,p.y * 60));
        }
        for(Pos p : inimigos){
            barreira.add(new Pos(p.x * 60 ,p.y  * 60 ));
            barreira.add(new Pos((p.x-1) * 60,(p.y-1) * 60));
            barreira.add(new Pos(p.x * 60,(p.y-1) * 60));
            barreira.add(new Pos((p.x+1) * 60,(p.y) * 60));
            barreira.add(new Pos(p.x * 60,(p.y+1)* 60));
            barreira.add(new Pos((p.x-1)*60,(p.y+1)*60));
            barreira.add(new Pos((p.x-1) * 60,p.y * 60));
        }
        //inicializar as arestas
        
        Boolean add = true;
        Pos atual = null;
        
        for(int i = 0; i < 41; i++){
            for(int j = 0; j < 25; j++){
                //TODO: Arrumar essa parte que ta completamente errada
                if(i != 0){
                    //if(!barreira.contains(new Pos(board.get(i-1).get(j).getX()*60,board.get(i-1).get(j).getY()*60))){
                    add = true;
                    atual = new Pos(board.get(i-1).get(j).getX()*60,board.get(i-1).get(j).getY()*60);
                    for(Pos p : barreira)
                        if(p.getX() == atual.getX() && p.getY() == atual.getY() ){
                            add = false;
                            break;
                        }
                    
                    if(add)
                        board.get(i).get(j).getVizinhos().add(board.get(i-1).get(j));
                    //}
                }
                if(i !=0 && j != 0){
                    //if(!barreira.contains(new Pos(board.get(i-1).get(j-1).getX()*60,board.get(i-1).get(j-1).getY()*60))){
                    add = true;
                    atual = new Pos(board.get(i-1).get(j-1).getX()*60,board.get(i-1).get(j-1).getY()*60);
                    for(Pos p : barreira)
                        if(p.getX() == atual.getX() && p.getY() == atual.getY() ){
                            add = false;
                            break;
                        }                    
                    if(add)
                        board.get(i).get(j).getVizinhos().add(board.get(i-1).get(j-1));
                    //}
                    //if(!barreira.contains(new Pos(board.get(i).get(j-1).getX()*60,board.get(i).get(j-1).getY()*60))){
                        add = true;
                        atual = new Pos(board.get(i).get(j-1).getX()*60,board.get(i).get(j-1).getY()*60);
                        for(Pos p : barreira)
                            if(p.getX() == atual.getX() && p.getY() == atual.getY() ){
                                add = false;
                                break;
                            }
                        if(add)
                            board.get(i).get(j).getVizinhos().add(board.get(i).get(j-1));
                    //}
                }
                if(i != 40){
                    //if(!barreira.contains(new Pos(board.get(i+1).get(j).getX()*60,board.get(i+1).get(j).getY()*60))){
                    add = true;
                    atual = new Pos(board.get(i+1).get(j).getX()*60,board.get(i+1).get(j).getY()*60);
                    for(Pos p : barreira)
                        if(p.getX() == atual.getX() && p.getY() == atual.getY() ){
                            add = false;
                            break;
                        }                    
                    if(add)
                        board.get(i).get(j).getVizinhos().add(board.get(i+1).get(j));
                    //}
                }
                if(i != 40 && j != 24){
                    //if(!barreira.contains(new Pos(board.get(i+1).get(j+1).getX()*60,board.get(i+1).get(j+1).getY()*60))){
                    add = true;
                    atual = new Pos(board.get(i+1).get(j+1).getX()*60,board.get(i+1).get(j+1).getY()*60);
                    for(Pos p : barreira)
                        if(p.getX() == atual.getX() && p.getY() == atual.getY() ){
                            add = false;
                            break;
                        }                    
                    if(add)
                        board.get(i).get(j).getVizinhos().add(board.get(i+1).get(j+1));
                    //}
                    //if(!barreira.contains(new Pos(board.get(i).get(j+1).getX()*60,board.get(i).get(j+1).getY()*60))){
                    add = true;
                    atual = new Pos(board.get(i).get(j+1).getX()*60,board.get(i).get(j+1).getY()*60);
                    for(Pos p : barreira)
                        if(p.getX() == atual.getX() && p.getY() == atual.getY() ){
                            add = false;
                            break;
                        }                    
                    if(add)
                        board.get(i).get(j).getVizinhos().add(board.get(i).get(j+1));
                    //}
                }
            }
        }
        
        //Heuristica que nao deu certo...
        Hex goal = new Hex(objetivo.getX(),objetivo.getY());
        int xGoalCubo, yGoalCubo, zGoalCubo;
        xGoalCubo = goal.getX() - (goal.getY() - (goal.getY()&1))/2;
        zGoalCubo = goal.getY();
        yGoalCubo = -xGoalCubo-zGoalCubo;
        
        int xCubo, yCubo, zCubo;
        
        for(int i = 0; i < 41; i++){
            for(int j = 0; j < 25; j++){
                //x = q - (r - (r&1)) / 2
                xCubo = board.get(i).get(j).getX() - (board.get(i).get(j).getY() - (board.get(i).get(j).getY()&1))/2;
                //z = r
                zCubo = board.get(i).get(j).getY();
                //y = -x-z
                yCubo = -xCubo-zCubo;
                
                board.get(i).get(j).setH(
                        Math.max(
                            Math.max(
                                Math.abs(xCubo - xGoalCubo), 
                                Math.abs(yCubo - yGoalCubo)
                            ),
                            Math.abs(zCubo - zGoalCubo)
                        )
                    );
            }
        } 
    }
    
    public ArrayDeque<Pos> Astar(int sx, int sy, int gx, int gy){
        ArrayDeque<Pos> plano = new ArrayDeque();
        ArrayList<Hex> closed = new ArrayList();
        Hex current = board.get(sx).get(sy);
        ArrayList<Hex> open = new ArrayList();
        open.add(current);        
        
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
            if(current.getX() == gx && current.getY() == gy){
                System.out.println("A* CHEGOU EM GOAL!!!!");
                while(current.getX() != sx && current.getY() != sy){
                    plano.push(new Pos(current.getX() * 3600, current.getY() * 3600));
                    current = current.getParent();
                }
                return plano;
            }
            for(Hex v: current.getVizinhos()){
                if(!closed.contains(v)){
                    if(!open.contains(v)){
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
        }
        return plano;
    }
    
    public Pos LRTAstar(int sx, int sy, int gx, int gy){
        Hex current = board.get(sx).get(sy);
        Hex last = current;
        for(Hex v: current.getVizinhos()){
            v.setInflacao(v.getInflacao() + 50);
        }
        Hex menorf = current.getVizinhos().get(0);
        for(Hex w: current.getVizinhos()){
            if(w.getH() + w.getInflacao() < menorf.getH() + menorf.getInflacao()){
                menorf = w;
            }
            else if(w.getH() + w.getInflacao() == menorf.getH() + menorf.getInflacao()){
                double r =  Math.random();
                if(r > 0.5){
                    menorf = w;
                }
            }
        }
        if(menorf.getX() == gx && menorf.getY() == gy)
            return new Pos(-1, -1);
        return new Pos(menorf.getX() * 3600, menorf.getY() * 3600);
    }
    
}
