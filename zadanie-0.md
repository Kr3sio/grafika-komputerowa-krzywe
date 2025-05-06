# Zadanie 0

### 1. Uzupełnienie klasy `RectangleModel`.
- Dodanie konstruktora przyjmującego parametry: współrzędne (startX, startY), szerokość (width), wysokość (height) oraz kolor (color).
- Klasa powinna zapewniać dostęp do tych wartości poprzez odpowiednie metody getter.

### 2. Implementacja klasy `RectangleDialog` na podstawie `CircleDialog`.
- Klasa powinna umożliwiać użytkownikowi wprowadzenie współrzędnych prostokąta, jego wymiarów oraz wybór koloru.
- Należy dodać pola tekstowe `JTextField` do wprowadzania wartości liczbowych oraz możliwość wyboru koloru.

### 3. Rozszerzenie `ImageController` o metodę `drawRectangle(RectangleModel rectangle)`.
1. Metoda powinna pobrać aktualny obraz z lewego panelu i wywołać operację rysowania prostokąta na obrazie.

### 4. Dodanie opcji rysowania prostokąta do menu aplikacji - `MenuBar`.
1. Dodać nową pozycję menu w sekcji `Edycja`.
2. Dodać nasłuch na wybranie opcji w menu umożliwiający otwarcie okna dialogowego `RectangleDialog` i edycję obrazu.

### 5. Dodanie nowej funkcjonalności: rysowanie poziomych linii na obrazie.
1. Utworzyć nową klasę np. `LinesDialog`, która pozwoli użytkownikowi wprowadzić parametry rysowanych linii: Liczbę linii lub Odstęp między liniami i Kolor linii.
2. Rozszerzyć `ImageController` o metodę `drawHorizontalLines`.
3. Dodać nową pozycję w menu, która pozwoli na wywołanie nowej funkcjonalności.

### 6. Implementacja funkcji kopiowania obrazu z prawego panelu do lewego.
1. Dodać nową pozycję menu w sekcji `Prawy panel`, która umożliwi skopiowanie obrazu do lewego panelu.
2. Zaimplementować metodę `copyRightPanel()` w `ImageController`, analogiczną do `copyLeftPanel()`.
3. Dodać nasłuch na wybranie opcji menu, aby wywoływał funkcję kopiowania obrazu z prawego panelu.