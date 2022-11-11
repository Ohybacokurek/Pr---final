import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;


public class Application {
    public static int turn = 0;

    public static void gameStart(ArrayList<Integer> playerHand, ArrayList<Integer> player2Hand, ArrayList<Integer> talon, ArrayList<Integer> tableDeck) {
        System.out.println("Rozdávám karty....");
        for (int i = 0; i < 10; i++) {
            if (i%2 == 0) {
                getCard(playerHand, talon);
            } else {
                getCard(player2Hand, talon);
            }
        }
        getCard(tableDeck, talon);

    }
    public static void talon(ArrayList<Integer> talon) {
        for (int i = 0; i < 32; i++)
            talon.add(i);
    }
    public static void printCards(ArrayList<Integer> array) {
        String output = "";
        for (int i = 0; i < array.size(); i++) {
                switch (cardType(array.get(i))) {
                    case 0: output += "Sedmička ";
                        break;
                    case 1: output += "Osmička ";
                        break;
                    case 2: output += "Devítka ";
                        break;
                    case 3: output += "Desítka ";
                        break;
                    case 4: output += "Spodek ";
                        break;
                    case 5: output += "Měnič ";
                        break;
                    case 6: output += "Král ";
                        break;
                    case 7: output += "Eso ";
                        break;
                }
                // Kule - 1 Srdce - 2 Zelený - 3 Žaludy - 4
                    switch (cardColor(array.get(i))) {
                        case 0: output += "Kule";
                            break;
                        case 1: output += "Srdce";
                            break;
                        case 2: output += "Zelený";
                            break;
                        case 3: output += "Žaludy";
                            break;
                    }
                    if (array.size() - 1 != i) output += " | ";



        }
        System.out.println(output);
    }


    public static void getCard(ArrayList<Integer> array, ArrayList<Integer> talon) {
                array.add(talon.get(0));
                talon.remove(0);
    }

    public static int cardType(int num) {
        return num % 8;
    }

    public static int cardColor(int num) {
        return num/8; // Kule - 1 Srdce - 2 Zelený - 3 Žaludy - 4
    }
    public static void shuffleCards(ArrayList<Integer> talon) {
        Collections.shuffle(talon);
    }
    public static void askForColor(ArrayList<Integer> tableCard, ArrayList<Integer> hand, int cardIndex) {
        Scanner sc = new Scanner(System.in);
        int ans;
        do {
            System.out.println("Zadej barvu 1 - Kule | 2 - Srdce | 3 - Zelený | 4 - Žaludy ");
            ans = Integer.parseInt(sc.nextLine());
        } while (ans < 1 || ans > 4);

        tableCard.remove(0);
        tableCard.add(5+((ans-1)*8));
        hand.remove(cardIndex - 1);

    }
    public static void playCard(ArrayList<Integer> playerHand, ArrayList<Integer> tableCard, ArrayList<Integer> talon, ArrayList<Integer> enemy) {
        Scanner sc = new Scanner(System.in);
        int cardIndex;
        int temp = 0;
        do {
            System.out.println("Zadej Kartu kterou chceš zahrát (1 - " + playerHand.size() + ") nebo 0 pro líznutí");
            cardIndex = Integer.parseInt(sc.nextLine());
            while (cardIndex < 0 || cardIndex > playerHand.size()) {
                System.out.println("Nemůžeš zahrát tuto kartu | 0 pro líznutí");
                cardIndex = Integer.parseInt(sc.nextLine());
            }
            if (cardIndex == 0) break;
            temp = playerHand.get(cardIndex - 1);
        } while (cardIndex != -1 && cardType(temp) != 5 && cardType(temp) != cardType(tableCard.get(0)) && cardColor(temp) != cardColor(tableCard.get(0)));

        if (cardIndex != 0) {
            switch(cardType(playerHand.get(cardIndex - 1))) {
                case 5: askForColor(tableCard, playerHand, cardIndex);
                    break;
                case 0:
                    for (int i = 0; i < 2; i++) {
                        getCard(enemy,talon);
                    }
                case 7: turn++;
                default:
                    tableCard.remove(0);
                    tableCard.add(playerHand.get(cardIndex - 1));
                    playerHand.remove(cardIndex - 1);
            }
        } else {
            getCard(playerHand, talon);
        }
    }

    public static void aiPlayCard(ArrayList<Integer> aiHand, ArrayList<Integer> tableCard, ArrayList<Integer> talon, ArrayList<Integer> enemy) {
        int tTemp = tableCard.get(0);
        int cardIndex = -1;
        for (int i = 0; i < aiHand.size(); i++) {
            if (cardType(aiHand.get(i)) == 5 || cardType(tTemp) == cardType(aiHand.get(i)) || cardColor(tTemp) == cardColor(aiHand.get(i))) {
                cardIndex = i;
                break;
            }
        }
        if (cardIndex == -1) getCard(aiHand, talon);
        else {
            switch(cardType(aiHand.get(cardIndex))) {
                case 0:
                    for (int i = 0; i < 2; i++) {
                        getCard(enemy,talon);
                    }
                case 7: turn++;
                default:
                    tableCard.remove(0);
                    tableCard.add(aiHand.get(cardIndex));
                    aiHand.remove(cardIndex);
            }
        }


    }


    public static void main(String[] args) {
        ArrayList<Integer> talon=new ArrayList<>();
        ArrayList<Integer> playerHand=new ArrayList<>();
        ArrayList<Integer> player2Hand=new ArrayList<>();
        ArrayList<Integer> tableDeck=new ArrayList<>();
        talon(talon);
        shuffleCards(talon);
        gameStart(playerHand, player2Hand, talon, tableDeck);

        while(playerHand.size() != 0 && player2Hand.size() != 0) {
            if (turn % 2 ==0) {
                System.out.println("Karta na stole:");
                printCards(tableDeck);
                System.out.println("\n");
                System.out.println("Počet karet soupeře je: "+ player2Hand.size() +" Tvoje karty :");
                printCards(playerHand);
                playCard(playerHand, tableDeck, talon, player2Hand);
            } else {
                System.out.println("Hraje soupeř...");
                aiPlayCard(player2Hand, tableDeck, talon, playerHand);
            }
            turn++;
        }
        if (playerHand.size() == 0) System.out.println("Gratuluji Vyhrál Jsi :)");
        else System.out.println("Prohrál jsi :C");

    }
}
