# Szablon aplikacji - grafika rastrowa

## Opis aplikacji
Aplikacja umożliwia wczytywanie obrazów, ich modyfikację oraz zapis zmodyfikowanych plików. 
Wykorzystuje wzorzec Model-View-Controller (MVC), który pozwala na oddzielenie logiki biznesowej od interfejsu użytkownika.
Dzięki temu podejściu aplikacja jest modularna, łatwa w rozbudowie i utrzymaniu.

## Funkcjonalności aplikacji:
- Wczytywanie obrazu z pliku.
- Wyświetlanie wczytanego obrazu w lewym panelu.
- Usuwanie obrazu z panelu lewego i prawego.
- Kopiowanie obrazu z lewego panelu do panelu prawego.
- Modyfikacja wczytanego obrazu.
- Wyświetlanie zmodyfikowanego obrazu w panelu prawym.
- Zapisywanie zmodyfikowanego obrazu do pliku.

## Architektura aplikacji
Aplikacja została podzielona zgodnie ze wzorcem **Model-View-Controller (MVC)** na trzy główne warstwy:

### 1. Model (pakiet `models`)
Warstwa modelu odpowiada za przechowywanie danych oraz operacje na nich. 
W aplikacji odpowiada za przechowywanie obrazów i umożliwienie ich modyfikacji.
- `ImageModel` – Przechowuje obraz oraz udostępnia metody do jego modyfikacji.
- `CircleModel` – Reprezentuje model koła.
- `RectangleModel` – Reprezentuje model prostokąta.

### 2. Widok (pakiet `views`)
Warstwa widoku odpowiada za interakcję z użytkownikiem oraz wyświetlanie informacji. 
Nie przechowuje danych biznesowych ani logiki aplikacji – służy wyłącznie do prezentacji danych.
- `MainFrame` – Główne okno aplikacji.
- `ImagePanel` – Panel, w którym wyświetlany jest obraz.
- `MenuBar` – Pasek menu umożliwiający wybór opcji (np. otwarcie pliku, zapis pliku, modyfikacja obrazu).
- `CircleDialog` – Okno dialogowe umożliwiające użytkownikowi podanie parametrów koła.
- `RectangleDialog` – Okno dialogowe umożliwiające użytkownikowi podanie parametrów prostokąta.

### 3. Kontroler (pakiet `controllers`)
Warstwa kontrolera odpowiada za zarządzanie przepływem danych między widokiem a modelem.
Odbiera akcje użytkownika i przekazuje je do odpowiednich klas modelu, a następnie aktualizuje widok.
- `ImageController` – Obsługuje operacje na obrazach, takie jak wczytywanie, kopiowanie, czyszczenie i modyfikacja.
- `FileController` – Odpowiada za zapis obrazów do pliku.

## Uruchomienie aplikacji
Aby uruchomić aplikację, należy skompilować projekt i uruchomić główną klasę `MainFrame`.

## Zadanie 0 
Opis zadania do wykonania w ramach Zadania 0 znajduję się w pliku [zadanie-0.md](zadanie-0.md).
