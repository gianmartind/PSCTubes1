package minimax;

import game.Game;
import main.collections.FastArrayList;
import random.RandomAI;
import util.AI;
import util.Context;
import util.Move;
import util.state.containerState.ContainerState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Minimax extends AI {

    protected int player = -1;

    protected List<Node> match;

    public Minimax()
    {
        this.friendlyName = "Example Minimax";
        this.match = new LinkedList<>();
    }

    @Override
    public void initAI(Game game, int playerID) {
        this.player = playerID;
    }

    @Override
    public Move selectAction(Game game, Context context, double maxSeconds, int maxIterations, int maxDepth) {
        Node current = new Node(null, null, context);

        //Menambahkan semua child node untuk current
        current.addAllChildren();

        //Menambahkan child node untuk semua child dari current
        for(int i = 0; i < current.children.size(); i++){
            current.children.get(i).addAllChildren();
            //Pohon tiga tingkat
            /*for(int j = 0; j < current.children.get(i).children.size(); j++){
                current.children.get(i).children.get(j).addAllChildren();
            }*/
        }
        //final int r = ThreadLocalRandom.current().nextInt(current.children.size());

        //Mencari value terbaik (sesuai player) yang bisa didapatkan dari state sekarang
        int bestValue = minimax(current, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, this.player);
        System.out.println("\nbestValue: " + bestValue);

        //Mencari semua leaf node dengan value sama dengan bestValue
        this.findMax(current, bestValue, 0);
        System.out.println("match size: " + this.match.size());
        Node toPick = this.match.get(ThreadLocalRandom.current().nextInt(this.match.size()));
        this.match.clear();
        return toPick.moveFromParent;
    }

    /**
     * Method untuk menghitung nilai dari setiap state (context dari node), untuk keperluan heuristik
     * @param node Node yang akan dihitung nilai dari statenya
     * @return Nilai state dari Node
     */
    public int computeValue(Node node){
        int result1 = 0;
        WhoCell[] whoCells = getWhoCellDetails(node);
        for(int i = 0; i < whoCells.length; i++){
            if(whoCells[i].getPlayer() == 1){
                result1 += 1000;
            }
            if(whoCells[i].getPlayer() == 2){
                result1 -= 1000;
            }

            if((whoCells[i].getCellNo() < 48 && whoCells[i].getCellNo() >= 40) && whoCells[i].getPlayer() == 1){
                result1 += 1;
            }
            if((whoCells[i].getCellNo() < 56 && whoCells[i].getCellNo() >= 48) && whoCells[i].getPlayer() == 1){
                result1 += 10;
            }
            if(whoCells[i].getCellNo() >= 56 && whoCells[i].getPlayer() == 1){
                result1 += 100;
            }

            if((whoCells[i].getCellNo() > 15 && whoCells[i].getCellNo() <= 23) && whoCells[i].getPlayer() == 2){
                result1 -= 1;
            }
            if((whoCells[i].getCellNo() > 7 && whoCells[i].getCellNo() <= 15) && whoCells[i].getPlayer() == 2){
                result1 -= 10;
            }
            if(whoCells[i].getCellNo() <= 7 && whoCells[i].getPlayer() == 2){
                result1 -= 100;
            }
        }
        //System.out.println(result);
        return result1;
    }

    /*
    public Node findMax(Node current, int value){
        System.out.println("value " + value);
        List<Node> childrenList = current.children;
        List<Node> match = new LinkedList<>();

        for(int i = 0; i < childrenList.size(); i++){
            if(childrenList.get(i).children.size() == 0){
                if(computeValue(childrenList.get(i)) == value){
                    match.add(childrenList.get(i));
                }
            } else {
                List<Node> childrenListChildren = childrenList.get(i).children;
                for(int j = 0; j < childrenListChildren.size(); j++){
                    if(computeValue(childrenListChildren.get(j)) == value){
                        match.add(childrenList.get(i));
                    }
                }
            }
        }

        return match.get(ThreadLocalRandom.current().nextInt(match.size()));
    }
    */


    public void findMax(Node node, int value, int depth){
        if(node.children.size() == 0){
            if(computeValue(node) == value){
                Node temp = node;
                while(temp.parent.parent != null){
                    temp = temp.parent;
                }
                this.match.add(temp);
            }
        }
        else {
            for(int i = 0; i < node.children.size(); i++){
                findMax(node.children.get(i), value, depth+1);
            }
        }
    }

    public int minimax(Node node, int depth, int alpha, int beta, int player){
        if(node.children.size() == 0){
            return computeValue(node);
        }

        if(player == 1){
            int bestVal = Integer.MIN_VALUE;
            for(int i = 0; i < node.children.size(); i++){
                int value = minimax(node.children.get(i), depth+1, alpha, beta, 2);
                bestVal = max(bestVal, value);
                alpha = max(alpha, bestVal);
                if(beta <= alpha){
                    break;
                }
            }
            return bestVal;
        } else {
            int bestVal = Integer.MAX_VALUE;
            for(int i = 0; i < node.children.size(); i++){
                int value = minimax(node.children.get(i), depth+1, alpha, beta, 1);
                bestVal = min(bestVal, value);
                beta = min(beta, bestVal);
                if(beta <= alpha){
                    break;
                }
            }
            return bestVal;
        }

    }

    /**
     * Mencari maksimum dari dua bilangan
     * @param a bilangan 1
     * @param b bilangan 2
     * @return nilai maksimum diantara a dan b
     */
    public int max(int a, int b){
        return a >= b ? a : b;
    }

    /**
     * Mencari minimum dari dua bilangan
     * @param a bilangan 1
     * @param b bilangan 2
     * @return nilai minimum diantara a dan b
     */
    public int min(int a, int b){
        return a >= b ? b : a;
    }

    public static WhoCell[] getWhoCellDetails(Node node){
        Context context = node.context;
        String state = context.state().containerStates()[0].cloneWhoCell().toString();
        String[] whoCell = state.substring(1, state.length()-1).split(", ");
        WhoCell[] cellDetails = new WhoCell[whoCell.length];
        for(int i = 0; i < cellDetails.length; i++){
            cellDetails[i] = new WhoCell(Integer.parseInt(whoCell[i]));
        }
        return cellDetails;
    }
    
    //Class untuk menyimpan state saat ini beserta semua kemungkinan state yang dapat dicapai dengan satu move.
    private static class Node {
        
        //Node untuk menyimpan parent dari node saat ini, null untuk root
        private final Node parent;

        //Move yang harus dicapai untuk sampai ke Node ini
        private final Move moveFromParent;
        
        //Context yang diperlukan untuk melihat state saat ini
        private final Context context;
        
        //List yang berisi semua child Node (semua state yang mungkin dicapai)
        private final List<Node> children = new ArrayList<Node>();

        //List yang berisi semua Move yang dapat dilakukan di Context sekarang
        private final FastArrayList<Move> availableMoves;

        /**
         * Constructor
         *
         * @param parent
         * @param moveFromParent
         * @param context
         */
        public Node(final Node parent, final Move moveFromParent, final Context context) {
            this.parent = parent;
            this.moveFromParent = moveFromParent;
            this.context = context;
            final Game game = context.game();
            //Mengisi List availableMoves dengan semua Moves yang mungkin dilakukan
            this.availableMoves = new FastArrayList<>(game.moves(context).moves());
        }

        //Mengisi list children dengan node baru yang didapat dari
        //mengaplikasikan masing-masing availableMoves ke Context saat ini
        public void addAllChildren(){
            for (int i = 0; i < this.availableMoves.size(); i++) {
                Context child = new Context(this.context);
                this.context.game().apply(child, this.availableMoves.get(i));
                this.children.add(new Node(this, this.availableMoves.get(i), child));
            }
        }
    }

    //Class untuk menyimpan whoCell dari setiap state
    public static class WhoCell{
        private int cellNo; //nomor cell
        private int player; //id player di cell

        /**
         * Constructor
         * @param num menerima satu integer yang menunjukan whoCell
         */
        public WhoCell(int num){
            if(num%2 == 0){
                this.cellNo = num/2;
                this.player = 1;
            }
            else {
                this.cellNo = (num - 1)/2;
                this.player = 2;
            }
        }

        public int getCellNo() {
            return cellNo;
        }

        public int getPlayer() {
            return player;
        }
    }
}
