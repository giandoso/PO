/*
 * João Pedro Giandoso - 2015.1.08.029
 * jpgiandoso@gmail.com  * 
 */
package Caixeiro;

import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import oracle.jrockit.jfr.JFR;

/**
 *
 * @author glauc
 */
public class CaixeiroPanel extends JFrame { //caixeiro viajanta é um JFRAME

    LinkedList<Coordenada> cidades;
    LinkedList<Ciclo> ciclosTotais;//LinkedList para guardar todos os ciclos

    public CaixeiroPanel() {
        this.setSize(1366, 768);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
//        this.repaint();        
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Coordenada x : cidades) {
            g.drawOval((int) x.x * 5 + 500, (int) x.y * 5 + 100, 5, 5);
        }

        for (Ciclo ciclo : ciclosTotais) {
            LinkedList<Integer> p = ciclo.integrantes;

            for (int i = 0; i < p.size() - 1; i++) {
                int p1 = p.get(i);
                int p2 = p.get(i + 1);

                int xinicioLinha = (int) cidades.get(p1).x;
                int yinicioLinha = (int) cidades.get(p1).y;
                int xfimLinha = (int) cidades.get(p2).x;
                int yfimLinha = (int) cidades.get(p2).y;

                g.drawLine((5 * xinicioLinha) + 500, (5 * yinicioLinha) + 100, (5 * xfimLinha) + 500, (5 * yfimLinha) + 100);

            }
            int pUltimo = p.get(p.size() - 1);
            int pPrimeiro = p.get(0);
            int xinicioLinha = (int) cidades.get(pUltimo).x;
            int yinicioLinha = (int) cidades.get(pUltimo).y;
            int xfimLinha = (int) cidades.get(pPrimeiro).x;
            int yfimLinha = (int) cidades.get(pPrimeiro).y;
            g.drawLine((5 * xinicioLinha) + 500, (5 * yinicioLinha) + 100, (5 * xfimLinha) + 500, (5 * yfimLinha) + 100);
        }

    }

    public void ExecutaRotaCaixeiro(String path) throws IOException, IloException {

        Leitura a = new Leitura();
        this.cidades = a.lerPontos(path);

        int qtdCidades = cidades.size(); //a quantidade de pontos é quantidade de pontos

        double[][] matrizDistancias = new double[qtdCidades][qtdCidades];

        IloCplex model = new IloCplex(); //crio o modelo        
        IloLinearNumExpr z = model.linearNumExpr();//criando a expressao z
        IloNumVar[][] X = new IloNumVar[qtdCidades][qtdCidades];

        //criando a matriz X na qual é booleana
        for (int i = 0; i < qtdCidades; i++) {
            for (int j = 0; j < qtdCidades; j++) {
                X[i][j] = model.intVar(0, 1);
            }
        }

        //criando a matriz das distancias entre as cidades
        //e também a funcao de minimizacao junto
        for (int i = 0; i < qtdCidades; i++) {
            for (int j = 0; j < qtdCidades; j++) {
                matrizDistancias[i][j] = Math.sqrt(Math.pow((cidades.get(i).x - cidades.get(j).x), 2.0) + Math.pow((cidades.get(i).y - cidades.get(j).y), 2.0));
                z.addTerm(matrizDistancias[i][j], X[i][j]);//Distancia * X[i][j](0 ou 1)
            }
        }

        model.addMinimize(z); //minimizar as distancias entre as cidades para formar um unico ciclo com a menor distancia entre ela

        //adicionando restrição que as posicao X[i][i] devem ser iguais a 0
        for (int i = 0; i < qtdCidades; i++) {
            model.addEq(X[i][i], 0);
        }

        //restrição que a somatoria da linhas sejam igual a 1, ou seja, cada ponto só pode estar ligando a outro unico ponto
        for (int i = 0; i < qtdCidades; i++) {
            IloLinearNumExpr somaLinha = model.linearNumExpr();
            for (int j = 0; j < qtdCidades; j++) {
                somaLinha.addTerm(1, X[i][j]);
            }
            model.addEq(somaLinha, 1);
        }

        //restrição que a somatoria das colunas sejam igual a 1, ou seja, cada ponto só pode estar recebendo ligacao de um outro unico ponto
        for (int j = 0; j < qtdCidades; j++) {
            IloLinearNumExpr somaColuna = model.linearNumExpr();
            for (int i = 0; i < qtdCidades; i++) {
                somaColuna.addTerm(1, X[i][j]);
            }
            model.addEq(somaColuna, 1);
        }

        model.solve();

        //
        while (true) {


            int[] visitados = new int[qtdCidades]; //vetor para todas as cidades visitadas
            ciclosTotais = new LinkedList();//LinkedList para guardar todos os ciclos

            for (int i = 0; i < qtdCidades; i++) {

                if (visitados[i] == 0) {//cidade nao visitada, inicia-se um novo ciclo
                    int inicioCiclo = i;
                    int prox = i;
                    visitados[i] = 1;
                    LinkedList<Integer> cicloAtual = new LinkedList();//salvar os pontos pertencentes ao ciclo
                    cicloAtual.add(inicioCiclo);

                    do {
                        for (int j = 0; j < qtdCidades; j++) {
                            if (model.getValue(X[prox][j]) > 0.5) { //CONSERTAR PARA COLOCAR IGUAL A 1 
                                prox = j; //vejo o proximo ponto do ciclo

                                //SEMPRE VERIFICO POIS QUANDO 
                                //O ULTIMO NÓ FOR LIGAR NO INICIO, ELE NAO ADICIONA NO CICLO
                                if (j != inicioCiclo) {
                                    cicloAtual.add(j); //adiciono ele ao ciclo
                                }
                                visitados[j] = 1;//marco como visitado
                                break;
                            }
                        }
//                        System.out.println("Inicio: " + inicioCiclo + "prox : " + prox);
                    } while (inicioCiclo != prox);
//                    System.out.println("adicionando em ciclos");
                    //adicionar nos ciclos totais
                    ciclosTotais.add(new Ciclo(cicloAtual));
                }

            }

            System.out.println("Quantidade de ciclos: " + ciclosTotais.size());

            //Após todos estados visitados
            if (ciclosTotais.size() > 1) {//tenho varios ciclos e preciso quebrá-lo
                this.repaint();
                for (Ciclo p : ciclosTotais) {
                    LinkedList<Integer> cicloASerQuebrado = p.integrantes;
                    int tamanhoCiclo = cicloASerQuebrado.size();

                    //Restrição de IDA
                    IloLinearNumExpr restricaoCicloIDA = model.linearNumExpr();
                    for (int i = 0; i < cicloASerQuebrado.size() - 1; i++) {
                        restricaoCicloIDA.addTerm(1, X[cicloASerQuebrado.get(i)][cicloASerQuebrado.get(i + 1)]);
                    }
                    restricaoCicloIDA.addTerm(1, X[cicloASerQuebrado.get(tamanhoCiclo - 1)][cicloASerQuebrado.get(0)]);//adicionando o fim ao inicio
                    model.addLe(restricaoCicloIDA, tamanhoCiclo - 1);

                    //Restrição de VOLTA
                    IloLinearNumExpr restricaoCicloVOLTA = model.linearNumExpr();
                    for (int i = tamanhoCiclo - 1; i > 0; i--) {
                        restricaoCicloVOLTA.addTerm(1, X[cicloASerQuebrado.get(i)][cicloASerQuebrado.get(i - 1)]);
                    }
                    restricaoCicloVOLTA.addTerm(1, X[cicloASerQuebrado.get(0)][cicloASerQuebrado.get(tamanhoCiclo - 1)]);//adicionando o inicio ao fim
                    model.addLe(restricaoCicloVOLTA, tamanhoCiclo - 1);

                }
            } else {
                this.repaint();
                break;
            }
            if (model.solve()) {
                System.out.println("Solucionando Novamente");

            } else {
                System.out.println("Deu ruim");
            }
        }

    }

}
