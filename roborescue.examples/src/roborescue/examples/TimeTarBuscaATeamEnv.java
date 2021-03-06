/* Este programa eh modelo para ser utilizado na tarefa de busca A* x LRTA*
 da disciplina de Sistemas Inteligentes 1.
 O time TarBuscaA vai salvar seu aliado que está como refém do inimigo. 
 */
package roborescue.examples;

import atuador.AtuadorAssincrono;
import atuador.AtuadorSincrono;
import br.edu.utfpr.hexgrid.*;
import jason.RoborescueEnv;
import jason.asSyntax.Structure;
import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import robocode.rescue.RobotInfo;
import robocode.rescue.interfaces.RMIRobotInterface;

public class TimeTarBuscaATeamEnv extends RoborescueEnv {

    private static final String nomeTime = "TimeTarBuscaA";
    private final int numRobos = 5;
    private RMIRobotInterface[] aliados;
    private RobotInfo[] inimigos;
    private char meuLadoCampo;
    private RobotInfo[] robos;
    private AtuadorAssincrono atuador;
    private Boolean primeiraVez = true;

    //Para inicializacoes necessarias
    @Override
    public void setup() {
        try {
            aliados = getServerRef().getTeamInterfaces(nomeTime);
            meuLadoCampo = aliados[0].getRobotInfo().getX() > 200 ? 'e' : 'd';
            atuador = new AtuadorAssincrono();

            /* O aliados[0] do time A eh o refem e inicia posicionado na 
             ** extremidade oposta do campo em relacao aos seus companheiros.
             ** Este robô deve ser resgatado.
             */
            System.out.println(nomeTime + ": pos robo REFEM X="
                    + aliados[0].getRobotInfo().getX()
                    + " Y=" + aliados[0].getRobotInfo().getY());

            /* o robo[1] do time A eh o salvador do refem
             ** vc pode escolher entre mexer ou nao nao posicao inicial dele
             */
            System.out.println(nomeTime + ": pos robo SALVADOR X="
                    + aliados[1].getRobotInfo().getX()
                    + " Y=" + aliados[1].getRobotInfo().getY());

            /*DONE- posicionar os robos aleatoriamente para o A*
             */
            if (meuLadoCampo == 'e') {
              
                aliados[2].ahead(300);
                aliados[2].execute();


                aliados[3].ahead(400);
                aliados[3].execute();
               
                aliados[4].ahead(475);
                aliados[4].execute();
                try{
                    sleep(3000);
                }
                catch (InterruptedException ex) {
                    Logger.getLogger(TimeTarBuscaATeamEnv.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } else {              
                aliados[4].ahead(175);
                aliados[4].execute();
                
                aliados[3].ahead(200);
                aliados[3].execute();
                               
                aliados[2].ahead(150);
                aliados[2].execute();
                
                aliados[2].ahead(40);
                aliados[2].execute();                 
            }

            /* Obtem informacoes dos robos do time inimigo - as posicoes dos 
             ** robos inimigos devem ser fixadas no outro time - o que eh
             ** importante para executar os algoritmos de busca
             */
            inimigos = new RobotInfo[numRobos];

            // observar que o arg eh o proprio nome do time para impedir trapacas
            inimigos = getServerRef().getEnemyTeamInfo(nomeTime);

            System.out.println("*** Inimigos de " + myTeam + " ***");
            for (int i = 0; i < numRobos; i++) {
                System.out.println(" [" + i + "]: X=" + inimigos[i].getX()
                        + " Y=" + inimigos[i].getY());
            }
        } catch (RemoteException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public boolean executeAction(String ag, Structure action) {
        return true;
    }

    public void mainLoop() throws RemoteException {
        robos = getServerRef().getMyTeamInfo(myTeam);

        RobotInfo refem = robos[0];
        double xRefem = refem.getX();
        double yRefem = refem.getY();
        RMIRobotInterface[] teamRef = getTeamRef();

        /*System.out.println("salvador " + (int) teamRef[1].getRobotInfo().getX() + ", "
                + (int) teamRef[1].getRobotInfo().getY());
        System.out.println("   refem " + (int) xRefem + ", " + (int) yRefem);*/

        // aguarda o robo aliado 3 acabar seu movimento pois eh o que vai
        // mais longe
        if (teamRef[3].getDistanceRemaining() <= 0.1 && teamRef[2].getDistanceRemaining() <= 0.1 && teamRef[4].getDistanceRemaining() <= 0.1) {
            if (primeiraVez) {
                primeiraVez = false;
                ArrayDeque<Pos> filaDeAcoes;
                ArrayList<Pos> PosAliados = new ArrayList();
                for(int cont1 = 2; cont1 < 5; cont1++){
                    PosAliados.add(new Pos((int)aliados[cont1].getRobotInfo().getX()/60, (int)aliados[cont1].getRobotInfo().getY()/60));
                }
                ArrayList<Pos> PosInimigos = new ArrayList();
                for(int cont2 = 0; cont2 < 5; cont2++){
                    PosInimigos.add(new Pos((int)inimigos[cont2].getX()/60, (int)inimigos[cont2].getY()/60));
                }               
                HexBoard board = new HexBoard(PosAliados, PosInimigos, new Pos((int)aliados[0].getRobotInfo().getX()/60, (int)aliados[0].getRobotInfo().getY()/60), 2, false);
                //TODO Planeja o caminho usando o A*
                filaDeAcoes = board.Astar((int)aliados[1].getRobotInfo().getX()/60, (int)aliados[1].getRobotInfo().getY()/60, (int)aliados[0].getRobotInfo().getX()/60, (int)aliados[0].getRobotInfo().getY()/60);
                //Executa o caminho achado pelo A*
                ArrayDeque<Pos> volta = new ArrayDeque();
                volta.push(new Pos((int)aliados[1].getRobotInfo().getX()/60, (int)aliados[1].getRobotInfo().getY()/60));
                while(!filaDeAcoes.isEmpty()){
                    Pos pop = filaDeAcoes.pop();
                    Pos ultimo = volta.peek();
                    volta.push(pop);
                    
                    int deltaY = pop.getY() - ultimo.getY();
                    int deltaX = pop.getX() - ultimo.getX();
                    double change = 0;
                    
                    /*if(ultimo.getX() % 2 != 0 && deltaX < 0 && deltaY > 0){                      
                        aliados[1].setTurnLeft(60);
                        aliados[1].setAhead(60);
                        change = 60;
                    }
                    else if(ultimo.getX() % 2 == 0 && deltaX == 0 && deltaY < 0){                      
                        aliados[1].setTurnLeft(60);
                        aliados[1].setAhead(60);
                        change = 60;
                    }                               
                    else if(ultimo.getX() % 2 != 0 && deltaX == 0 && deltaY > 0){                     
                        aliados[1].setTurnRight(120);
                        aliados[1].setAhead(60);
                        change = -120;
                    }
                    else if(ultimo.getX() % 2 == 0 && deltaX < 0 && deltaY > 0){                     
                        aliados[1].setTurnRight(120);
                        aliados[1].setAhead(60);
                        change = -120;
                    }                            
                    else if(ultimo.getX() % 2 == 0 && deltaX == 0 && deltaY > 0){                    
                        aliados[1].setTurnRight(60);
                        aliados[1].setAhead(60);
                        change = -60;
                    }
                    else if(ultimo.getX() % 2 != 0 && deltaX > 0 && deltaY < 0){                    
                        aliados[1].setTurnRight(60);
                        aliados[1].setAhead(60);
                        change = -60;
                    }                            
                    else if(ultimo.getX() % 2 != 0 && deltaX < 0 && deltaY < 0){
                        aliados[1].setTurnLeft(120);
                        aliados[1].setAhead(60);
                        change = 120;
                    }
                    else if(ultimo.getX() % 2 == 0 && deltaX == 0 && deltaY < 0){
                        aliados[1].setTurnLeft(120);
                        aliados[1].setAhead(60);
                        change = 120;
                    }
                    else if(deltaX > 0)
                        aliados[1].ahead(60);
                    else if(deltaX < 0 ){
                        aliados[1].setTurnRight(180);
                        aliados[1].setAhead(60);
                        change = 180;
                    }        
                    aliados[1].execute();
                    try{
                        sleep(100);
                    }
                    catch (InterruptedException ex) {
                        Logger.getLogger(TimeTarBuscaATeamEnv.class.getName()).log(Level.SEVERE, null, ex);
                    }                    
                    aliados[1].setTurnRight(change);
                    aliados[1].execute();
                    try{
                        sleep(100);
                    }
                    catch (InterruptedException ex) {
                        Logger.getLogger(TimeTarBuscaATeamEnv.class.getName()).log(Level.SEVERE, null, ex);
                    }*/                          
                    atuador.irPara(aliados[1], pop.getX()*60, pop.getY()*60);
                }
                while(!volta.isEmpty()){
                    Pos pop = volta.pop();
                    atuador.irPara(aliados[1], pop.getX()*60, pop.getY()*60);
                }
                aliados[1].ahead(150);
                aliados[1].execute();                
                
                
            }
        }

    }

    @Override
    public void end() {
        try {
            super.getEnvironmentInfraTier().getRuntimeServices().stopMAS();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        TimeTarBuscaATeamEnv team = new TimeTarBuscaATeamEnv();
        team.init(new String[]{nomeTime, "localhost"});

        while (true) {
            try {
                team.mainLoop();
                Thread.sleep(20);
            } catch (RemoteException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
