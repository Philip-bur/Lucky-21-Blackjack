import java.util.Scanner;

public class Main {
    private static final int STARTING_CHIPS = 100;
    private static final int DECK_SIZE = 52;
    private static final int MAX_HAND_SIZE = 12;

    private static class Card {
        String symbol;
        int value;

        Card(String symbol, int value) {
            this.symbol = symbol;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Card[] deck = createDeck();
        int chips = STARTING_CHIPS;
        boolean playing = true;

        printHeader();
        System.out.println("Willkommen bei Blackjack!");

        while (playing && chips > 0) {
            System.out.println("\nDein Guthaben: " + chips + " Chips");
            int bet = readBet(scanner, chips);

            if (bet == 0) {
                break;
            }

            shuffleDeck(deck);
            chips -= bet;
            int roundResult = playRound(scanner, deck, bet, chips);

            if (roundResult < 0) {
                playing = false;
            } else {
                chips = roundResult;
                playing = askForAnotherRound(scanner, chips);
            }
        }

        if (chips == 0) {
            System.out.println("\nDu hast keine Chips mehr. Das Spiel ist vorbei.");
        } else {
            System.out.println("\nDanke fuer das Spiel!");
        }
        scanner.close();
    }

    private static int playRound(Scanner scanner, Card[] deck, int bet, int chips) {
        Card[] playerHand = new Card[MAX_HAND_SIZE];
        Card[] dealerHand = new Card[MAX_HAND_SIZE];
        int playerCount = 0;
        int dealerCount = 0;
        int nextCard = 0;

        playerHand[playerCount++] = deck[nextCard++];
        dealerHand[dealerCount++] = deck[nextCard++];
        playerHand[playerCount++] = deck[nextCard++];
        dealerHand[dealerCount++] = deck[nextCard++];

        int playerScore = handValue(playerHand, playerCount);
        int dealerScore = handValue(dealerHand, dealerCount);

        if (playerScore == 21 || dealerScore == 21) {
            printTable(dealerHand, dealerCount, playerHand, playerCount, true);
            return settleBlackjack(chips, bet, playerScore == 21, dealerScore == 21);
        }

        boolean playerStands = false;
        while (!playerStands && playerScore < 21) {
            printTable(dealerHand, dealerCount, playerHand, playerCount, false);
            int choice = readChoice(scanner);

            if (choice == 1) {
                playerHand[playerCount++] = deck[nextCard++];
                playerScore = handValue(playerHand, playerCount);
            } else if (choice == 2) {
                playerStands = true;
            } else {
                System.out.println("Die Runde wurde beendet.");
                return -1;
            }
        }

        if (playerScore <= 21) {
            while (handValue(dealerHand, dealerCount) < 17) {
                dealerHand[dealerCount++] = deck[nextCard++];
            }
        }

        dealerScore = handValue(dealerHand, dealerCount);
        printTable(dealerHand, dealerCount, playerHand, playerCount, true);
        return settleRound(chips, bet, playerScore, dealerScore);
    }

    private static int settleBlackjack(int chips, int bet, boolean playerBlackjack,
                                       boolean dealerBlackjack) {
        if (playerBlackjack && dealerBlackjack) {
            System.out.println("Gleichstand! Dein Einsatz wird zurueckgegeben.");
            return chips + bet;
        }
        if (playerBlackjack) {
            int winnings = bet + bet * 3 / 2;
            System.out.println("Blackjack! Du gewinnst " + winnings + " Chips.");
            return chips + winnings;
        }
        System.out.println("Der Dealer hat Blackjack. Du verlierst den Einsatz.");
        return chips;
    }

    private static int settleRound(int chips, int bet, int playerScore, int dealerScore) {
        if (playerScore > 21) {
            System.out.println("Du bist ueber 21. Du verlierst den Einsatz.");
            return chips;
        }
        if (dealerScore > 21 || playerScore > dealerScore) {
            System.out.println("Du gewinnst " + bet * 2 + " Chips!");
            return chips + bet * 2;
        }
        if (playerScore == dealerScore) {
            System.out.println("Gleichstand! Dein Einsatz wird zurueckgegeben.");
            return chips + bet;
        }
        System.out.println("Der Dealer gewinnt.");
        return chips;
    }

    private static boolean askForAnotherRound(Scanner scanner, int chips) {
        if (chips == 0) {
            return false;
        }
        System.out.print("\nNoch eine Runde spielen? (j/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.equals("j") || answer.equals("ja");
    }

    private static int readBet(Scanner scanner, int chips) {
        while (true) {
            System.out.print("Einsatz eingeben (0 zum Beenden): ");
            String input = scanner.nextLine().trim();

            try {
                int bet = Integer.parseInt(input);
                if (bet >= 0 && bet <= chips) {
                    return bet;
                }
            } catch (NumberFormatException exception) {
                // Bei einer falschen Eingabe wird die Frage wiederholt.
            }

            System.out.println("Bitte gib einen Einsatz zwischen 0 und " + chips + " ein.");
        }
    }

    private static int readChoice(Scanner scanner) {
        while (true) {
            System.out.print("Auswahl: ");
            String input = scanner.nextLine().trim();

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 3) {
                    return choice;
                }
            } catch (NumberFormatException exception) {
                // Bei einer falschen Eingabe wird die Frage wiederholt.
            }

            System.out.println("Bitte waehle 1, 2 oder 3.");
        }
    }

    private static void printTable(Card[] dealerHand, int dealerCount, Card[] playerHand,
                                   int playerCount, boolean revealDealer) {
        System.out.println("\n----------------------------------------");
        System.out.println("Dealer:");
        System.out.print("[ " + (revealDealer ? dealerHand[0].symbol : "??") + " ] ");

        for (int cardIndex = 1; cardIndex < dealerCount; cardIndex++) {
            System.out.print("[ " + dealerHand[cardIndex].symbol + " ] ");
        }

        if (revealDealer) {
            System.out.println("       Punktzahl: " + handValue(dealerHand, dealerCount));
        } else {
            System.out.println();
        }

        System.out.println("\nDeine Karten:");
        for (int cardIndex = 0; cardIndex < playerCount; cardIndex++) {
            System.out.print("[ " + playerHand[cardIndex].symbol + " ] ");
        }
        System.out.println("       Punktzahl: " + handValue(playerHand, playerCount));
        System.out.println("----------------------------------------");

        if (!revealDealer) {
            System.out.println("\nWas moechtest du tun?\n");
            System.out.println("1. Karte ziehen");
            System.out.println("2. Halten");
            System.out.println("3. Spiel beenden\n");
        }
    }

    private static int handValue(Card[] hand, int cardCount) {
        int score = 0;
        int aces = 0;

        for (int cardIndex = 0; cardIndex < cardCount; cardIndex++) {
            score += hand[cardIndex].value;
            if (hand[cardIndex].value == 11) {
                aces++;
            }
        }

        // Ein Ass zaehlt als 1 statt 11, wenn die Hand sonst ueber 21 waere.
        while (score > 21 && aces > 0) {
            score -= 10;
            aces--;
        }
        return score;
    }

    private static Card[] createDeck() {
        String[] suits = { "H", "D", "C", "S" };
        String[] symbols = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "J", "Q", "K" };
        Card[] deck = new Card[DECK_SIZE];
        int cardIndex = 0;

        for (int suitIndex = 0; suitIndex < suits.length; suitIndex++) {
            for (int symbolIndex = 0; symbolIndex < symbols.length; symbolIndex++) {
                int value = symbolIndex == 0 ? 11 : Math.min(symbolIndex + 1, 10);
                deck[cardIndex++] = new Card(symbols[symbolIndex] + suits[suitIndex], value);
            }
        }
        return deck;
    }

    private static void shuffleDeck(Card[] deck) {
        for (int cardIndex = deck.length - 1; cardIndex > 0; cardIndex--) {
            int swapIndex = (int) (Math.random() * (cardIndex + 1));
            Card temporary = deck[cardIndex];
            deck[cardIndex] = deck[swapIndex];
            deck[swapIndex] = temporary;
        }
    }

    private static void printHeader() {
        System.out.println("========================================");
        System.out.println("           LUCKY 21 - BLACKJACK");
        System.out.println("========================================\n");
    }
}
