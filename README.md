# Translation-Exerciser
A utility that makes it easy to learn and rehearse translations of words. For every word list you practice you need to drag-and-drop as many translations as you can. If the answer eludes you you can press the reveal button to reveal the answer or just leave it unanswered. Words can have several translations in which case you will need to select multiple answers.

When a session is ended you will get a lot of statistics over how well you did. If you ran the session in 'Test' mode all your errors will be stored in a special list so you can practice on them later, just select  'Previous Errors' when launching a new session.

To create your own lists just create a text file with a single line per word you want to translate, format them as: "wordToTranslate = Translation 1; Translation 2; ...; Translation Final". Then launch the program and choose 'Import list' from the 'File' menu. Check "wordlists_source" for examples of whole lists.

## Running
To run, either extract the program from the 'Exported.zip' file or compile it yourself (see instructions below).

## Compiling
To compile the program you first need to download the source files for the library specified in the Dependencies section. When the source files have been obtained there should be no problem compiling the program. Once it is compiled it can be run by launching the class TranslationExerciser (in the gui-package).

If you want to create a runnable jar-file, just include all the compiled class files and create an appropriate manifest file which points to gui.TranslationExerciser as the main class. Once the jar-file has been made it will need the folders "textures" and "languages" in the same folder as the program for it to run correctly. 

## Dependencies
To compile the program you will need my [Utilities library](https://github.com/Sebastian-0/Utilities).

## License
This program is free to use as long as you comply to the MIT license (see LICENSE for details).