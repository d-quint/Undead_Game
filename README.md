## Welcome to Undead Game!

## DOWNLOAD .JAR FILE

[Undead Game v1.0.0](https://filebin.net/archive/jzofr3ozo92g7boc/zip)

To run the .jar file, extract it, open a terminal, go to the extracted .jar file's path, and type the following command:

```bash
java -jar Undead_Game.jar
```

## ABOUT

Halloween is nearly approaching and all the superstitions about the dead are being revived again. There is a belief that dead ones may come back to life and interact with the living ones. With this phenomenon they are called undead. In the celebration of Halloween, tales of the undead are being brought up again and for their tale to survive they need to compete. 

## COMPILING & RUNNING

1. Clone this repo.
2. Make sure Java 8 is installed.
3. Compile the main class using the following command:

    ```bash
    javac src/Main.java -d bin -cp src
    ```

  This will create a `Main.class` file in the `bin` directory, and all the classes that it uses.

4. Run the main class from the current directory using the following command:

    ```bash
    java -cp bin Main
    ```

5. Alternatively, just double click the `run.bat` file in the current directory which would automatically run these two commands for you.

## USAGE

Having been inspired by how users interact with bots in Discord, this game is played by typing prefixed commands into the terminal.

### COMMANDS

| **COMMAND NAME** |   **ARG 1**   |  **ARG 2**  |                              **DESCRIPTION**                              |   **SAMPLE USAGE**  |
|:----------------:|:-------------:|:-----------:|:-------------------------------------------------------------------------:|:-------------------:|
|       START      |       -       |      -      | Starts the game instance, allowing you to use more game-related commands. |       `!start`      |
|       HELP       |       -       |      -      |                    Lists all currently usable commands.                   |       `!help`       |
|       EXIT       |       -       |      -      |                             Exits the program.                            |       `!exit`       |
|     SETPREFIX    |   NEW_PREFIX  |      -      |                   Sets a new prefix for typing commands.                  |    `!setprefix >`   |
|      SETNAME     |    NEW_NAME   |      -      |                      Sets a new name for the player.                      |   `!setname Marcy`  |
|     POPULATE     |       -       |      -      |                 Raises an undead from the depths of hell.                 |     `!populate`     |
|    LISTUNDEADS   |       -       |      -      |                   Lists all the undeads you have raised.                  |    `!listundeads`   |
|      ATTACK      | ATTACKER_NAME | TARGET_NAME |                Command an undead to attack another undead.                | `!attack Finn Lich` |
|      CLEANUP     |       -       |      -      |                    Deletes all currently dead undeads.                    |      `!cleanup`     |
|       CLOSE      |       -       |      -      |        Closes the game instance and returns back to the main menu.        |       `!close`      |

NOTE: The prefix is set to `!` by default. You can change it by using the `SETPREFIX` command. The prefix can be any character, but it is recommended to use a character that is not used by any other command, such as a non-alphanumeric character.

## CONTRIBUTING

This was made possible by the combined efforts of Group 50/50 -2 of TN22:

- John Christian Paglinawan
- Dane Ross Quintano
- Dharmveer Sandhu
