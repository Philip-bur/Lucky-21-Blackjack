# Blackjack Console

QuizQuest wird zu einem einfachen, textbasierten Blackjack-Spiel fuer die
Konsole umgestaltet. In dieser Phase besteht das Projekt bewusst nur aus
Dokumentation. Die Umsetzung des Spiels folgt spaeter.

## Spielidee

Der Spieler startet mit einem Guthaben von 100 Chips und tritt gegen den
Dealer an. Ziel ist es, mit den eigenen Karten moeglichst nahe an 21 Punkte
zu kommen, ohne 21 zu ueberschreiten.

Eine Runde soll sich an folgendem Ablauf orientieren:

```text
========================================
           BLACKJACK CONSOLE
========================================

Willkommen bei Blackjack!

Dein Guthaben: 100 Chips
Einsatz eingeben: 20

----------------------------------------
Dealer:
[ ?? ] [  K  ]

Deine Karten:
[  8  ] [  7  ]        Punktzahl: 15
----------------------------------------

Was moechtest du tun?

1. Karte ziehen
2. Halten
3. Spiel beenden

Auswahl: 1
```

## Geplante Spielregeln

- Ein Standarddeck mit 52 Karten wird verwendet.
- Zahlenkarten zaehlen ihren Kartenwert.
- Bube, Dame und Koenig zaehlen jeweils 10 Punkte.
- Ein Ass zaehlt als 1 oder 11 Punkte, je nachdem, was fuer die Hand
  vorteilhafter ist.
- Der Dealer zieht Karten, bis er mindestens 17 Punkte erreicht.
- Ein Blackjack soll mit einer hoeheren Auszahlung belohnt werden.
- Bei einem Gleichstand soll der Einsatz zurueckgegeben werden.

## Projektstatus

Aktuell enthaelt das Projekt nur die Textdateien `README.md`, `features.md`
und `agents.md`. Es gibt noch keinen Programmcode und keine Python-Datei.

## Autor

@Philip-bur / Philip Buranich / wi26b160
