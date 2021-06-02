# Design Document

Detailed description of the project.



## Work Folw

![WorkFlow](https://github.com/thienkaito/WordSearchTGBot/blob/master/Image/Workflow.png)


## Functional Details and Project Structure

### 1. Class MyAmazingBot

#### It is the class extends `TelegramLongPollingBot` (class built by [@rubenlagus](https://github.com/rubenlagus))

- Class variables:
  
  | Variable    | Function                           | Type                       |
  | :--------   | :----------------------------------| :------------------------- |
  | `GameList`  | Save game information of each user | `HashMap<String, Game>`    |


- Class methods:
  
  | Name         | Function                           | Type                                                                |
  | :--------    | :-------                           | :------------------------- |
  | `getBotUsername()`   | This method must always return **Bot username**.                                       | `String` |
  | `getBotToken()`   | This method must always return **Bot Token**.                                             | `String` |
  | `onUpdateReceived()`   | This method will manage incoming messages, send messages to users, control the game, mainly responsible for various logical events. | `Void` |

### 2. Class Game

#### The Game class store information about the player's game.
- Class variables:

  | Variable    | Function                           | Type                       |
  | :--------   | :----------------------------------| :------------------------- |
  | `grid`      |  The grid of this Game         | `Class Grid`               |
  | `WordAnswered`      | The words that have been answered       | `List<String>`               |
  | `PosAnswered`      | The pos of the words that have been answered       | `List<Integer>`               |
  | `GameHeart`      | The current Lives that player has       | `Int`               |




- Class methods:

  | Name         | Function                           | Type                                                                |
  | :--------    | :-------                           | :------------------------- |
  | `Game()`   | Initialize Game: create new grid,...                                     | `Void` |
  | `readWords(String filename)`   | Read list of word from **filename**                                   | `List<String>` |
  | `createWordSearch(List<String> words)`   | Try to fill up the empty grid with random words from **List<String> words**                                          | `Grid` |
  | `tryPlaceWord(String word)`   | Try to place **word** in the grid. | `int` |
  | `tryLocation(String word, int dir, int pos)`   | Try to location the **word** with position and direction. | `int` |
  | `CheckAnswer(String Ans)`   | Check the Answer. Mark the positions of the correct answers.                                           | `Boolean` |
  | `CreateTable()`   | This method will convert grid of this game to String. | `String` |

### 3. Class Grid

#### The Grid class store a character grid and related information.
- Class variables

  | Variable    | Function                           | Type                       |
  | :--------   | :----------------------------------| :------------------------- |
  | `cells`      |  Map of grid         | `char[][]`               |
  | `Solutions`      | The list of random words, which generated this grid       | `List<String>`

## Execution plan

### Create a Telegram Bot

- Get the library and add it to project.
- Register bot.

### Build the game

- Find a way to create a grid containing at least 10 answers.
- Create Game Logic.
- Because the game is built with the Telegram platform, so the game interface is created with only unicode chareacters and emoji.