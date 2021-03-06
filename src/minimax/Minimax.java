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
    protected String analysisReport = "";

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
//            for(int j = 0; j < current.children.get(i).children.size(); j++){
//                current.children.get(i).children.get(j).addAllChildren();
//            }
        }

        //Mencari value terbaik (sesuai player) yang bisa didapatkan dari state sekarang
        int bestValue = minimax(current, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, this.player);
        System.out.println("\nbestValue: " + bestValue);

        Node toPick = findMax2(current, bestValue);
        this.match.clear();
        System.out.println(computeValue(current));

        for(int i = 0; i < current.children.size(); i++){
            System.out.print(computeValue(current.children.get(i)) + " <> ");
            for(int j = 0; j < current.children.get(i).children.size(); j++){
                System.out.print(computeValue(current.children.get(i).children.get(j)) + " ");
            }
            System.out.println();
        }

        System.out.println("picked: " + computeValue(toPick));
        return toPick.moveFromParent;
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

    public static boolean isSave(WhoCell[] whoCells, int i){
        if(whoCells[i].getPlayer() == 1){
            int pos = whoCells[i].getCellNo();
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
        } else {
            int pos = whoCells[i].getCellNo();
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
     * Method untuk menghitung jumlah bidak milik player 1 (bidak putih)
     * @param node
     * @return jumlah bidak player 1
     */
    public int countPlayer1(Node node){
        //variabel untuk menyimpan jumlah
        int count = 0;
        //array WhoCell yang diisi dengan detail sesuai dari node parameter
        WhoCell[] whoCells = this.getWhoCellDetails(node);
        //looping untuk tiap item di array
        for(int i = 0; i < whoCells.length; i++){
            //jika item merupakan player 1 maka tambah count
            if(whoCells[i].getPlayer() == 1){
                count++;
            }
        }
        //kembalikan count sebagai hasil penghitungan
        return count;
    }

    /**
     * Method untuk menghitung jumlah bidak milik player 2 (bidak hitam)
     * @param node
     * @return jumlah player 2
     */
    public int countPlayer2(Node node){
        //variabel untuk menyimpan jumlah
        int count = 0;
        //array WhoCell yang diisi dengan detail sesuai dari node parameter
        WhoCell[] whoCells = this.getWhoCellDetails(node);
        //looping untuk tiap item di array
        for(int i = 0; i < whoCells.length; i++){
            //jika item merupakan player 2 maka tambah count
            if(whoCells[i].getPlayer() == 2){
                count++;
            }
        }
        //kembalikan count sebagai hasil penghitungan
        return count;
    }

    /**
     * Method untuk mencari semua leaf node yang memiliki nilai max, sesuai dengan hasil dari minimax
     * dijalankan dengan rekursif
     * @param node diisi dengan node root
     * @param value nilai maksimum dari minimax
     * Akan mengembalikan parent node dari semua leaf node yang memiliki nilai sama dengan value
     */
    /*public void findMax(Node node, int value){
        //jika sudah mencapai leaf
        if(node.children.size() == 0){
            //jika nilai dari node sama dengan value
            if(computeValue(node) == value){
                //buat node baru sama dengan node saat ini
                Node temp = node;
                //Naikkan node selama node bukan berada di tingkat 2 (child dari root)
                while(temp.parent.parent != null){
                    temp = temp.parent;
                }
                //Masukkan node ke array match
                this.match.add(temp);
            }
        }
        //jika belum mencapai leaf
        else {
            //untuk semua child dari node
            for(int i = 0; i < node.children.size(); i++){
                //jalankan method findMax
                findMax(node.children.get(i), value);
            }
        }
    }*/

    public Node findMax2(Node node, int value){
        for(int i = 0; i < node.children.size(); i++){
            if(this.player == 1){
                if(findMinInNode(node.children.get(i)) == value){
                    return node.children.get(i);
                }
            } else {
                if (findMaxInNode(node.children.get(i)) == value) {
                    return node.children.get(i);
                }
            }
        }
        return null;
    }

    public int findMinInNode(Node node){
        if(node.children.size() == 0){
            return computeValue(node);
        } else {
            int min = Integer.MAX_VALUE;
            for(int i = 0; i < node.children.size(); i++){
                if(computeValue(node.children.get(i)) < min){
                    min = computeValue(node.children.get(i));
                }
            }
            return min;
        }
    }

    public int findMaxInNode(Node node){
        if(node.children.size() == 0){
            return computeValue(node);
        } else {
            int max = Integer.MIN_VALUE;
            for(int i = 0; i < node.children.size(); i++){
                if(computeValue(node.children.get(i)) > max){
                    max = computeValue(node.children.get(i));
                }
            }
            return max;
        }
    }
    /**
     * Algoritma untuk Minimax
     * sumber: https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-4-alpha-beta-pruning/
     * @param node
     * @param depth
     * @param alpha
     * @param beta
     * @param player
     * @return
     */
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
