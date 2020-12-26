package minimax;

import game.Game;
import main.collections.FastArrayList;
import metadata.graphics.no.No;
import random.RandomAI;
import util.AI;
import util.Context;
import util.Move;
import util.state.containerState.ContainerState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Minimax2 extends AI {

    protected int player = -1;
    protected String analysisReport = "";

    protected List<Node> match;

    public Minimax2()
    {
        this.friendlyName = "Minimax 2";
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
            for(int j = 0; j < current.children.get(i).children.size(); j++){
                current.children.get(i).children.get(j).addAllChildren();
            }
        }

        //Buat Node baru dengan value Integer.MIN_VALUE untuk alpha (parameter bebas karena hanya value yg dipakai)
        Node alpha = new Node(null, null, context);
        alpha.setValue(Integer.MIN_VALUE);

        //Buat Node baru dengan value Integer.MAX_VALUE untuk beta (parameter bebas karena hanya value yg dipakai)
        Node beta = new Node(null, null, context);
        beta.setValue(Integer.MAX_VALUE);

        //Jalankan algoritma minimax untuk mendapatkan node dengan value optimal, yang akan direturn
        Node bestValue = minimax(current, 0, alpha, beta, this.player);
        System.out.println("\nbestValue: " + bestValue.value);

        //Node toPick = findMax2(current, bestValue);
        System.out.println(computeValue(current));

        for(int i = 0; i < current.children.size(); i++){
            System.out.print(computeValue(current.children.get(i)) + " <> ");
            for(int j = 0; j < current.children.get(i).children.size(); j++){
                System.out.print(computeValue(current.children.get(i).children.get(j)) + " ");
            }
            System.out.println();
        }

        //Ambil parent node (node yang merupakan child dari root) dari node yang terpilih
        while(bestValue.parent.parent != null){
            bestValue = bestValue.parent;
        }

        System.out.println("picked: " + bestValue.value);
        return bestValue.moveFromParent;
    }

    /**
     * Method untuk menghitung nilai dari setiap state (context dari node), untuk keperluan heuristik
     * @param node Node yang akan dihitung nilai dari statenya
     * @return Nilai state dari Node
     */
    public static int computeValue(Node node){
        int result1 = 0;
        WhoCell[] whoCells = getWhoCellDetails(node);
        for(int i = 0; i < whoCells.length; i++){
            if(whoCells[i].getPlayer() == 1){
                result1 += 100;
            }
            if(whoCells[i].getPlayer() == 2){
                result1 -= 100;
            }
            //Posisi player 1 (unsafe)
            if((whoCells[i].getCellNo() >= 16 && whoCells[i].getCellNo() <= 23) && whoCells[i].getPlayer() == 1){
                result1 += 10;
            }
            if((whoCells[i].getCellNo() >= 24 && whoCells[i].getCellNo() <= 31) && whoCells[i].getPlayer() == 1){
                result1 += 20;
            }
            if((whoCells[i].getCellNo() >= 32 && whoCells[i].getCellNo() <= 39) && whoCells[i].getPlayer() == 1){
                result1 += 30;
            }
            if((whoCells[i].getCellNo() >= 40 && whoCells[i].getCellNo() <= 47) && whoCells[i].getPlayer() == 1){
                result1 += 40;
            }
            if((whoCells[i].getCellNo() >= 48 && whoCells[i].getCellNo() <= 55) && whoCells[i].getPlayer() == 1){
                result1 += 50;
            }
            if((whoCells[i].getCellNo() >= 56 && whoCells[i].getCellNo() <= 63) && whoCells[i].getPlayer() == 1){
                result1 += 999999;
            }
            //Posisi player 1 (safe)
//            if((whoCells[i].getCellNo() >= 16 && whoCells[i].getCellNo() <= 23) && whoCells[i].getPlayer() == 1 && isSave(whoCells, i)){
//                result1 += 100;
//            }
//            if((whoCells[i].getCellNo() >= 24 && whoCells[i].getCellNo() <= 31) && whoCells[i].getPlayer() == 1 && isSave(whoCells, i)){
//                result1 += 1000;
//            }
//            if((whoCells[i].getCellNo() >= 32 && whoCells[i].getCellNo() <= 39) && whoCells[i].getPlayer() == 1 && isSave(whoCells, i)){
//                result1 += 10000;
//            }
            if((whoCells[i].getCellNo() >= 40 && whoCells[i].getCellNo() <= 47) && whoCells[i].getPlayer() == 1 && isSave(whoCells, i)){
                result1 += 99999;
            }
            if((whoCells[i].getCellNo() >= 48 && whoCells[i].getCellNo() <= 55) && whoCells[i].getPlayer() == 1 && isSave(whoCells, i)){
                result1 += 99999;
            }
//            if((whoCells[i].getCellNo() >= 56 && whoCells[i].getCellNo() <= 63) && whoCells[i].getPlayer() == 1 && isSave(whoCells, i)){
//                result1 += 10000000;
//            }

            //Posisi player 2 (unsafe)
            if((whoCells[i].getCellNo() >= 40 && whoCells[i].getCellNo() <= 47) && whoCells[i].getPlayer() == 2){
                result1 -= 10;
            }
            if((whoCells[i].getCellNo() >= 32 && whoCells[i].getCellNo() <= 39) && whoCells[i].getPlayer() == 2){
                result1 -= 20;
            }
            if((whoCells[i].getCellNo() >= 24 && whoCells[i].getCellNo() <= 31) && whoCells[i].getPlayer() == 2){
                result1 -= 30;
            }
            if((whoCells[i].getCellNo() >= 16 && whoCells[i].getCellNo() <= 23) && whoCells[i].getPlayer() == 2){
                result1 -= 40;
            }
            if((whoCells[i].getCellNo() >= 8 && whoCells[i].getCellNo() <= 15) && whoCells[i].getPlayer() == 2){
                result1 -= 50;
            }
            if((whoCells[i].getCellNo() >= 0 && whoCells[i].getCellNo() <= 7) && whoCells[i].getPlayer() == 2){
                result1 -= 999999;
            }
            //Posisi player 2 (safe)
//            if((whoCells[i].getCellNo() >= 40 && whoCells[i].getCellNo() <= 47) && whoCells[i].getPlayer() == 2 && isSave(whoCells, i)){
//                result1 -= 100;
//            }
//            if((whoCells[i].getCellNo() >= 32 && whoCells[i].getCellNo() <= 39) && whoCells[i].getPlayer() == 2 && isSave(whoCells, i)){
//                result1 -= 1000;
//            }
//            if((whoCells[i].getCellNo() >= 24 && whoCells[i].getCellNo() <= 31) && whoCells[i].getPlayer() == 2 && isSave(whoCells, i)){
//                result1 -= 10000;
//            }
            if((whoCells[i].getCellNo() >= 16 && whoCells[i].getCellNo() <= 23) && whoCells[i].getPlayer() == 2 && isSave(whoCells, i)){
                result1 -= 99999;
            }
            if((whoCells[i].getCellNo() >= 8 && whoCells[i].getCellNo() <= 15) && whoCells[i].getPlayer() == 2 && isSave(whoCells, i)){
                result1 -= 99999;
            }
//            if((whoCells[i].getCellNo() >= 0 && whoCells[i].getCellNo() <= 7) && whoCells[i].getPlayer() == 2 && isSave(whoCells, i)){
//                result1 -= 10000000;
//            }
        }
        //System.out.println(result);
        return result1;
    }

    /**
     * Method untuk memeriksa apakah suatu bidak di titik i aman
     * @param whoCells array yang berisi who cell
     * @param i posisi yang akan diperiksa
     * @return boolean apakah aman atau tidak
     */
    public static boolean isSave(WhoCell[] whoCells, int i){
        //jika bidak merupakan milik player 1
        if(whoCells[i].getPlayer() == 1){
            //posisi dari bidak player 1 tersebut
            int pos = whoCells[i].getCellNo();
            //cek apakah ada bidak lawan di posisi yang dapat menyerang
            for(int j = 0; j < whoCells.length; j++){
                if(whoCells[j].getPlayer() == 2){
                    if(whoCells[j].getCellNo() == pos - 17 ||
                            whoCells[j].getCellNo() == pos - 15 ||
                            whoCells[j].getCellNo() == pos - 10 ||
                            whoCells[j].getCellNo() == pos - 6 ||
                            whoCells[j].getCellNo() == pos + 6 ||
                            whoCells[j].getCellNo() == pos + 10 ||
                            whoCells[j].getCellNo() == pos + 15 ||
                            whoCells[j].getCellNo() == pos + 17){
                        return false;
                    }
                }
            }
            return true;
            //jika bidak merupakan milik player 2
        } else {
            //posisi dari bidak player 1 tersebut
            int pos = whoCells[i].getCellNo();
            //cek apakah ada bidak lawan di posisi yang dapat menyerang
            for(int j = 0; j < whoCells.length; j++){
                if(whoCells[j].getPlayer() == 1){
                    if(whoCells[j].getCellNo() == pos - 17 ||
                            whoCells[j].getCellNo() == pos - 15 ||
                            whoCells[j].getCellNo() == pos - 10 ||
                            whoCells[j].getCellNo() == pos - 6 ||
                            whoCells[j].getCellNo() == pos + 6 ||
                            whoCells[j].getCellNo() == pos + 10 ||
                            whoCells[j].getCellNo() == pos + 15 ||
                            whoCells[j].getCellNo() == pos + 17){
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /**
     * Algoritma untuk Minimax dengan Alpha-Beta Pruning
     * sumber: https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-4-alpha-beta-pruning/
     * @param node
     * @param depth
     * @param alpha
     * @param beta
     * @param player
     * @return Node dengan nilai optimal
     */
    public Node minimax(Node node, int depth, Node alpha, Node beta, int player){
        if(node.children.size() == 0){
            return node;
        }

        if(player == 1){
            Node bestVal = new Node(null, null, node.context);
            bestVal.setValue(Integer.MIN_VALUE);
            for(int i = 0; i < node.children.size(); i++){
                Node value = minimax(node.children.get(i), depth+1, alpha, beta, 2);
                bestVal = max(bestVal, value);
                alpha = max(alpha, bestVal);
                if(beta.value <= alpha.value){
                    break;
                }
            }
            return bestVal;
        } else {
            Node bestVal = new Node(null, null, node.context);
            bestVal.setValue(Integer.MAX_VALUE);
            for(int i = 0; i < node.children.size(); i++){
                Node value = minimax(node.children.get(i), depth+1, alpha, beta, 1);
                bestVal = min(bestVal, value);
                beta = min(beta, bestVal);
                if(beta.value <= alpha.value){
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
    public Node max(Node a, Node b){
        return a.value >= b.value ? a : b;
    }

    /**
     * Mencari minimum dari dua bilangan
     * @param a bilangan 1
     * @param b bilangan 2
     * @return nilai minimum diantara a dan b
     */
    public Node min(Node a, Node b){
        return a.value >= b.value ? b : a;
    }

    /**
     * Method untuk mendapatkan detail who cell (tile yang terisi dan milik player mana) dari sebuah node
     * @param node
     * @return array berisi objek WhoCell
     */
    public static WhoCell[] getWhoCellDetails(Node node){
        //Context dari node saat ini
        Context context = node.context;
        //Detail who cell dalam format String
        String state = context.state().containerStates()[0].cloneWhoCell().toString();
        //Ubah string menjadi array, buang karakter pertama dan terakhir karena merupakan { dan }
        String[] whoCell = state.substring(1, state.length()-1).split(", ");
        //Buat array dengan tipe data WhoCell sesuai panjang array String
        WhoCell[] cellDetails = new WhoCell[whoCell.length];
        //Isi array dengan objek WhoCell
        for(int i = 0; i < cellDetails.length; i++){
            cellDetails[i] = new WhoCell(Integer.parseInt(whoCell[i]));
        }
        //Kembalikan array yang berisi objek WhoCell
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

        private int value;
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
            this.value = computeValue(this);
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

        public void setValue(int value){
            this.value = value;
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
