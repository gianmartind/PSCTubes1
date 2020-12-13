package minimax;

import game.Game;
import main.collections.FastArrayList;
import random.RandomAI;
import util.AI;
import util.Context;
import util.Move;
import util.state.containerState.ContainerState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Minimax extends AI {

    protected int player = -1;

    public Minimax()
    {
        this.friendlyName = "Example Minimax";
    }

    @Override
    public void initAI(Game game, int playerID) {
        this.player = playerID;
    }

    @Override
    public Move selectAction(Game game, Context context, double maxSeconds, int maxIterations, int maxDepth) {
        Node current = new Node(null, null, context);
        current.addAllChildren();
        final int r = ThreadLocalRandom.current().nextInt(current.children.size());

        return current.children.get(r).moveFromParent;
    }

    //Method untuk menghitung nilai dari setiap state
    //untuk keperluan heuristik
    public int computeValue(Node node){
        int result = 0;

        return result;
    }

    public WhoCell[] getWhoCellDetails(Node node){
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

    public static class WhoCell{
        private int cellNo;
        private int player;

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
