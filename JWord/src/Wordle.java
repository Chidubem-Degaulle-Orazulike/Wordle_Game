import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Wordle implements ActionListener {
    private int count = 0;//counter used to set the current panel or delete the current panel
    private String validator = "";//string used to keep track of the words deleted
    public String hinter = "";

    // creating the grid layout for the wordle gui  i.e the panels and setting the text , clearing the text
    class wordWindow extends JPanel{
        JLabel[] wordleColumn = new JLabel[5];
        public wordWindow(){
            this.setLayout(new GridLayout(1,5));
            Border black = BorderFactory.createLineBorder(Color.BLACK);
            //for loop for creating the first five panels
            for(int i = 0; i < 5; i++){
                wordleColumn[i] = new JLabel();
                wordleColumn[i].setOpaque(true);
                wordleColumn[i].setBorder(black);
                this.add(wordleColumn[i]);
            }
        }
        // setting the wordle panel text with the string, position and required color
        public void setWordlePanelText(String characterValue, int pos, Color color){
            this.wordleColumn[pos].setText(characterValue);
            this.wordleColumn[pos].setBackground(color);
        }

        // function to clear the current 5 panels
        public void clearWordlePanel(){
            for(int j = 0; j < 5; j++){
                wordleColumn[j].setText("");
            }

        }

    }

    class UserInput extends JPanel implements ActionListener{
        // creating the buttons for the wordle game
        private JButton enterButton;
        private JButton listOfButtons[];
        private JButton backspace;
        private String inter = "";// string used to keep track of the current character to be set to the wordle panel

        static final String keyboardLetters = "qwertyuiopasdfghjklzxcvbnm";// list of characters in the querty format for my keyboard

        public UserInput(){
            // setting the layout of the buttons
            this.setLayout(new GridLayout(2,2));
            int length = keyboardLetters.length();
            listOfButtons = new JButton[length];

//            enterButton = new JButton("Enter");
//            this.add(enterButton);
            //using a for loop to create each button in the string

            for(int i = 0; i < length; i++){
                listOfButtons[i] = new JButton("" + keyboardLetters.charAt(i));
                add(listOfButtons[i]);
                listOfButtons[i].addActionListener(this);
            }

            backspace = new JButton("BackSpace");
            this.add(backspace);
            backspace.addActionListener(this);

            enterButton = new JButton("Enter");
            this.add(enterButton);



        }
        public JButton getEnterButton(){return enterButton;}

        @Override
        public void actionPerformed(ActionEvent e) {
            //condition set to trigger the character removal from the screen if the characters are on the board
            //and decrementing the counter to ensure the position of the new characters are correctly placed on the screen
            if(e.getSource() == backspace && validator.length() > 0){
                count--;
                getActivePanel().setWordlePanelText("",count,Color.WHITE);
                validator = validator.substring(0,validator.length()-1);
            }

            if(validator.length() < 5){//can only be triggered when the letters on the screen are less than 5 to prevent errors
                int length = keyboardLetters.length();


                for(int i= 0; i < length; i++){
                    if(e.getSource() == listOfButtons[i]){
                        inter += keyboardLetters.charAt(i);
                        validator += keyboardLetters.charAt(i);
                        getActivePanel().setWordlePanelText(inter,count, Color.GRAY);
                        count++;
                        inter = "";
                    }
                }
//                System.out.println(validator);
            }else{
                JOptionPane.showMessageDialog(null, "Please only Input 5 letter words", "Incorrect input length", JOptionPane.INFORMATION_MESSAGE);
            }






        }
    }

    // generating the wordle frame

    private JFrame wordleFrame;
    private wordWindow[] wordleDisplayArray = new wordWindow[6];
    private UserInput userInput;
    private String genString;
    private int counter = 0;
    public Wordle(){
        wordleFrame = new JFrame("Wordle Game Invention");
        wordleFrame.setSize(1000,800);
        wordleFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        wordleFrame.setLayout(new GridLayout(7,1));
        wordleFrame.setVisible(true);
        wordleFrame.setLocationRelativeTo(null);

        for(int i = 0; i<6; i++){
            wordleDisplayArray[i]= new wordWindow();
            wordleFrame.add(wordleDisplayArray[i]);
        }

        userInput = new UserInput();
        userInput.getEnterButton().addActionListener(this);
        wordleFrame.add(userInput);
        wordleFrame.revalidate();

        genString = getGenString();

    }

    public static boolean isAlpha(String checkGuess){
        return checkGuess != null && checkGuess.matches("^[a-zA-Z]*$");
    }



    @Override
    public void actionPerformed(ActionEvent e) {//conditions set for game functionality i.e winning or losing the game
        // collecting the text from the screen
        String userAnswer = "";
        for(int i =0; i < 5; i++){
            userAnswer+= getActivePanel().wordleColumn[i].getText();
        }

        boolean testAnswer = isAlpha(userAnswer);// ensuring the text on the screen contains characters only though unneccesary just a precaution
        Main dictGuess =  new Main();
        if (dictGuess.dictionary.contains(userAnswer)){//making sure the user guess is only within the dictionary of possible five letter words
            if(userAnswer.length() != 5){
                JOptionPane.showMessageDialog(null, "Please only Input 5 letter words", "Incorrect input length", JOptionPane.INFORMATION_MESSAGE);
            }

            if(testAnswer == false){
                JOptionPane.showMessageDialog(null, "Please enter letters only", "Incorrect Input", JOptionPane.INFORMATION_MESSAGE);
            }

            if(userAnswer.length() == 5){
                if(testAnswer == true && userAnswer.length() == 5){
                    if(wordComparator(userAnswer)){
                        JOptionPane.showMessageDialog(null, "You're correct", "Well done", JOptionPane.INFORMATION_MESSAGE);
                        wordleFrame.dispose();
                        return;
                    }
                }
                // conditioning the counters so the system can correctly go on the correct panels after each guess attempt
                counter++;
                count = counter-1;
                if(counter == 2){
                    count = counter-2;
                }
                if(counter == 3){
                    count = counter-3;
                }
                if(counter == 4){
                    count = counter-4;
                }
                if(counter == 5){
                    JOptionPane.showMessageDialog(null,"the answer rhymes with" + " " + getHint(), "Hint", JOptionPane.INFORMATION_MESSAGE);
                    count = counter-5;
                }



                if(counter > 5){
                    JOptionPane.showMessageDialog(null, "Your Out of Guesses", "Better Luck next Time", JOptionPane.INFORMATION_MESSAGE);
                    wordleFrame.dispose();
                    return;
                }
//                System.out.println(counter);
            }
        }else{
            JOptionPane.showMessageDialog(null, "Try Again", "Guess is invalid", JOptionPane.INFORMATION_MESSAGE);
            clearWordlePanels();
            count = 0;
            validator="";
        }



    }
    private void clearWordlePanels(){
        // conditions set for clearing the appropriate panels on the screen based on how many attempts taken
        for(int i = 0; i < 1; i++){
            wordleDisplayArray[counter].clearWordlePanel();

            if(counter == 0){
                getActivePanel().setWordlePanelText("", counter, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter+1, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter+2, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter+3, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter+4, Color.WHITE);
            }

            if(counter == 1){
                getActivePanel().setWordlePanelText("", counter-1, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter+1, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter+2, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter+3, Color.WHITE);
            }
            if(counter == 2){
                getActivePanel().setWordlePanelText("", counter-2, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter-1, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter+1, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter+2, Color.WHITE);
            }

            if(counter == 3){
                getActivePanel().setWordlePanelText("", counter-3, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter-2, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter-1, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter+1, Color.WHITE);
            }
            if(counter == 4){
                getActivePanel().setWordlePanelText("", counter-4, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter-3, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter-2, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter-1, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter, Color.WHITE);
            }
            if(counter == 5){
                getActivePanel().setWordlePanelText("", counter-5, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter-4, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter-3, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter-2, Color.WHITE);
                getActivePanel().setWordlePanelText("", counter-1, Color.WHITE);
            }


        }
    }

    private boolean wordComparator(String userAnswer){
//        System.out.println(getHint());
        // conditions set to make the screen color yellow green or grey depending on if the guess is correct or not

        java.util.List<String> genStringList = Arrays.asList(genString.split(""));
        String[] userAnswerArray = userAnswer.split("");
        List<Boolean> matchingWordsCheck = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            if(genStringList.contains(userAnswerArray[i])){
                if(genStringList.get(i).equals(userAnswerArray[i])){
                    getActivePanel().setWordlePanelText(userAnswerArray[i], i, Color.GREEN);
                    matchingWordsCheck.add(true);
                }else{
                    getActivePanel().setWordlePanelText(userAnswerArray[i], i, Color.YELLOW);
                    matchingWordsCheck.add(false);

                }
            }else{
                getActivePanel().setWordlePanelText(userAnswerArray[i], i, Color.GRAY);
                matchingWordsCheck.add(false);
            }
        }
        validator = "";
        return !matchingWordsCheck.contains(false);
    }

    public wordWindow getActivePanel(){
        return this.wordleDisplayArray[counter];
    }

    public String getGenString(){// obtaining the string generated to be guessed
        Main word = new Main();
        String getWord = word.passGenWord();
        return getWord;


    }
    public String getHint(){//obtaining the hint if the user can get the answer after 5 incorrect guesses
        Main hint = new Main();
        String getHints = hint.hintRhyme();
        return getHints;
    }


}
