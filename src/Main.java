


import java.io.File;
import java.util.*;

public class Main {

    private static HuffmanNode root;
    private static ArrayList<Character> arrayListChar;
    private static ArrayList<Integer> arrayListInt;
    private static ArrayList<String> arrayListString = new ArrayList<>();
    private static String string;
    private static String encodedString;
    private static String decodedString;
    private static final int[][] TRANSFORMATION_MATRIX = new int[][] {
            {1,0,1,0,1,0,1},
            {0,1,1,0,0,1,1},
            {0,0,0,1,1,1,1},
    };
    private static List<String> sequence;

    public static void main(String[] args) {

        Part1();

        //Part2
        for(int i = 0; i < arrayListChar.size(); i++){
            String cc = null;
            arrayListString.add(cc);
        }

        buildTree(arrayListChar, arrayListInt);
        setPrefixCodes(root, new String());

        sort();

        for(int i = 0; i < arrayListChar.size(); i++){
            System.out.println(arrayListChar.get(i) + " - " + arrayListString.get(i));
        }

        encodedString = "";
        decodedString = "";

        for(int i = 0; i < string.length(); i++){
            for(int j = 0; j < arrayListChar.size(); j++){
                if(string.charAt(i)==arrayListChar.get(j)){
                    encodedString += arrayListString.get(j);
                    break;
                }
            }
        }
        System.out.println(encodedString);

        //Part4
        getParrityProject4();

        //Part5
        makeAnError();


        Part6();

        //Part3
        setDecodedString();

    }

    private static void Part6() {

        for(int i = 0; i < sequence.size(); i++){

            int syn[] = new int[3];

            for(int j = 0; j < sequence.get(i).length(); j++){

                syn[0] += Character.getNumericValue(sequence.get(i).charAt(j)) * TRANSFORMATION_MATRIX[0][j];
                syn[1] += Character.getNumericValue(sequence.get(i).charAt(j)) * TRANSFORMATION_MATRIX[1][j];
                syn[2] += Character.getNumericValue(sequence.get(i).charAt(j)) * TRANSFORMATION_MATRIX[2][j];

            }

            syn[0] %= 2;
            syn[1] %= 2;
            syn[2] %= 2;

            int pos = (syn[2] * 4) + (syn[1] * 2) + syn[0];

            if(pos==0){
                //No error. LOL
            }else{
                StringBuilder sb = new StringBuilder(sequence.get(i));

                if(sb.charAt(pos-1)=='1') sb.setCharAt(pos-1, '0');
                else sb.setCharAt(pos-1, '1');

                sequence.set(i, sb.toString());
            }
        }

        System.out.println("Blocks without error:" + sequence);


        //decode from hamming

        encodedString = "";

        for(int i = 0; i < sequence.size(); i++){

            encodedString += sequence.get(i).charAt(2);

            for(int j = 4; j < sequence.get(i).length(); j++){
                encodedString += sequence.get(i).charAt(j);
            }

        }

    }

    private static void makeAnError() {
        for (int i = 0; i < sequence.size(); i++) {
            int randomPos = (int)(Math.random() * sequence.get(i).length());
            StringBuilder sb = new StringBuilder(sequence.get(i));
            if(sb.charAt(randomPos) == '1'){
                sb.setCharAt(randomPos, '0');
            } else {
                sb.setCharAt(randomPos, '1');
            }
            sequence.set(i, sb.toString());
        }
        System.out.println("Blocks with error:" + sequence);
    }

    private static String[] getNewParityBit (int blockPlace) {
        int[] res = new int[3];
        String[] returnArr = new String[3];
        for (int j = 0; j < TRANSFORMATION_MATRIX[0].length; j++) {
            if (j >= sequence.get(blockPlace).length()) {
                res[0] += 0;
                res[1] += 0;
                res[2] += 0;
                break;
            }
            res[0] += Character.getNumericValue(sequence.get(blockPlace).charAt(j)) * TRANSFORMATION_MATRIX[0][j];
            res[1] += Character.getNumericValue(sequence.get(blockPlace).charAt(j)) * TRANSFORMATION_MATRIX[1][j];
            res[2] += Character.getNumericValue(sequence.get(blockPlace).charAt(j)) * TRANSFORMATION_MATRIX[2][j];
        }

        for (int i = 0; i < res.length; i++) {
            res[i] %= 2;
            returnArr[i] = Integer.toString(res[i]);
        }

        return returnArr;
    }

    private static void getParrityProject4() {
        sequence = java.util.Arrays.asList(encodedString.split("(?<=\\G....)"));
        String firstParityBits = "00";
        for (int i = 0; i < sequence.size(); i++) {
            sequence.set(i, firstParityBits + sequence.get(i));
            sequence.set(i, sequence.get(i).substring(0,3) + '0' + sequence.get(i).substring(3,sequence.get(i).length()));
        }
        System.out.println("Blocks without parrity: " + sequence);

        for (int i = 0; i < sequence.size(); i++) {
            String[] finalParrity = getNewParityBit(i);
            sequence.set(i, finalParrity[0] + finalParrity[1] + sequence.get(i).substring(2,3)+ finalParrity[2] + sequence.get(i).substring(4, sequence.get(i).length()));
        }

        System.out.println("Blocks with parrity:" + sequence);
    }

    static void sort(){

        for(int i = 0; i < arrayListChar.size(); i++){
            for(int j = 0; j < arrayListChar.size()-1; j++){
                if(arrayListInt.get(j) < arrayListInt.get(j+1)||arrayListString.get(j).length() > arrayListString.get(j+1).length()){
                    int temp = arrayListInt.get(j);
                    arrayListInt.set(j, arrayListInt.get(j+1));
                    arrayListInt.set(j+1, temp);

                    char tempc = arrayListChar.get(j);
                    arrayListChar.set(j, arrayListChar.get(j+1));
                    arrayListChar.set(j+1, tempc);

                    String temps = arrayListString.get(j);
                    arrayListString.set(j, arrayListString.get(j+1));
                    arrayListString.set(j+1, temps);
                }
            }
        }
    }

    static void Part1(){
        File file = new File("Text.txt");
        try {

            Scanner scanner = new Scanner(file);
            string = scanner.nextLine().toLowerCase();

        }catch (Exception e){
            e.printStackTrace();
        }

        arrayListChar = new ArrayList<>();
        arrayListInt = new ArrayList<>();

        boolean has;
        int hasI = 0;
        Character character;

        for (int i = 0; i < string.length(); i++){

            has = false;
            character = string.charAt(i);

            for(int j = 0; j < arrayListChar.size(); j++){
                if(arrayListChar.get(j).equals(character)){
                    has = true;
                    hasI = j;
                    break;
                }
            }

            if(!has){
                arrayListChar.add(character);
                arrayListInt.add(1);
            }else{
                arrayListInt.set(hasI, arrayListInt.get(hasI)+1);
            }
        }

        for(int j = 0; j < arrayListChar.size(); j++){
            System.out.println(arrayListChar.get(j) + " - " + ((double)arrayListInt.get(j)/string.length()));
        }
    }

    //Project 2
    private static HuffmanNode buildTree(ArrayList<Character> characterArrayList, ArrayList<Integer> integerArrayList) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for (int i = 0; i < characterArrayList.size(); i++) {
            HuffmanNode huffmanNode = new HuffmanNode();
            huffmanNode.data = characterArrayList.get(i);
            huffmanNode.frequency = integerArrayList.get(i);
            huffmanNode.left = null;
            huffmanNode.right = null;
            priorityQueue.offer(huffmanNode);
        }
        assert priorityQueue.size() > 0;

        while (priorityQueue.size() > 1) {
            HuffmanNode x = priorityQueue.peek();
            priorityQueue.poll();

            HuffmanNode y = priorityQueue.peek();
            priorityQueue.poll();

            HuffmanNode sum = new HuffmanNode();

            sum.frequency = x.frequency + y.frequency;
            sum.data = '-';

            sum.left = x;

            sum.right = y;
            root = sum;

            priorityQueue.offer(sum);
        }

        return priorityQueue.poll();
    }

    private static void setPrefixCodes(HuffmanNode node, String prefix) {

        if (node != null) {
            if (node.left == null && node.right == null) {
                for(int i = 0; i < arrayListString.size(); i++){
                    if(arrayListChar.get(i)==node.data){
                        arrayListString.set(i, prefix);
                    }
                }
            } else {
                prefix += "1";
                setPrefixCodes(node.left, prefix);
                prefix = prefix.substring(0, prefix.length() - 1);

                prefix += "0";
                setPrefixCodes(node.right, prefix);
            }
        }
    }

    private static void setDecodedString() {
        int counter;
        while(encodedString.length() > 0){
            for(int j = 0; j < arrayListChar.size(); j++){
                if(encodedString.charAt(0)==arrayListString.get(j).charAt(0)){

                    counter = 0;
                    for(int k = 0; k < arrayListString.get(j).length(); k++){
                        if(encodedString.charAt(k)==arrayListString.get(j).charAt(k)) counter++;
                    }
                    if(counter == arrayListString.get(j).length()){
                        decodedString += arrayListChar.get(j);
                        encodedString = encodedString.substring(counter);
                        break;
                    }
                }
            }
        }
        System.out.println(decodedString);
    }
}
