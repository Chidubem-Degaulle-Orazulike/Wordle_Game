import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Main {

    //a list of all possible 5 letter words in English
    public static HashSet<String> dictionary = new HashSet<>();

    //a smaller list of words for selecting the target word from (i.e. the word the player needs to guess)
    public static ArrayList<String> targetWords = new ArrayList<>();
    public static ArrayList<String> genWord = new ArrayList<>();
    public static ArrayList<String> rhymeHint = new ArrayList<>();

    public static void main(String[] args) {
    //load in the two word lists
        try{
            Scanner in_dict  = new Scanner(new File("JWord/src/gameDictionary.txt"));
            while(in_dict.hasNext()){
                dictionary.add(in_dict.next());
            }

            Scanner in_targets = new Scanner(new File("JWord/src/targetWords.txt"));
            while(in_targets.hasNext()){
                targetWords.add(in_targets.next());
                rhymeHint.add(in_targets.next());
            }
            in_dict.close();
            in_targets.close();
            String generateWord = getTarget();
            genWord.add(generateWord);
//            System.out.println();
            // running the wordle class
            SwingUtilities.invokeLater(new Runnable(){
                @Override
                public void run() {
                    new Wordle();
                }
            });



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //use this method for selecting a word. It's important for marking that the word you have selected is printed out to the console!
    public static String getTarget(){
        Random r = new Random();
        String target = targetWords.get(r.nextInt(targetWords.size()));
        //don't delete this line.
        System.out.println(target);
        return target;
    }

    // the function that will be called by the gui to pass the current word generated to be guessed stored in the genword array
    public static String passGenWord(){
        String passedWord = genWord.get(genWord.size()-1);
        return passedWord;
    }

    // this function is called to generate a word that rhymes with the target word to be gueesed
    public static String hintRhyme(){
        String finale = "";
        for(int i = 0; i < rhymeHint.size();i++){
            //condition set so if the word selected has the same last two letters as the target word it is chosen
            if(rhymeHint.get(i).charAt(3) == passGenWord().charAt(3) && rhymeHint.get(i).charAt(4) == passGenWord().charAt(4)){
                finale += rhymeHint.get(i);
                break;
            }
        }
        //return the string
        return finale;
    }

}
