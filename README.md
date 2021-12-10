# Program-P1
Integer Multiplication Experiment
by Thomas Kwashnak, Emily Balboni, and Priscilla Esteves

*This is a markdown file, so viewing on GitHub or a Markdown compatible viewer is advised.* Repository can be found at [link](https://github.com/CSC-215-Coding-Assignments/Program-P1)

## Experiment Overview











## Running this Program
Firstly, make sure that you have Java version 17. If you open this application up on eclipse, then it will give you an option to download OpenJDK 17 in the IDE itself.

There were a few rewrites of the entire program, and all have been included. Below are the different implementations that we have in this repository.
 - We first modified, as a group, the implementation of the provided experiment. See [BiglyIntExperiment.java](/src/StarterCode/BiglyIntExperiment.java) (Found in the StarterCode Package)
 - Then, Thomas attempted implemented threading in the FirstThreading package (Main method found at [DataLogger.java](src/FirstThreading/Experiment.java)). However, this re-created each thread after each individual digit / method
 - **OUR CURRENT IMPLEMENTATION:** Lastly, Thomas condensed everything into a single class such that the experiment took full advantage of not needing to re-create any threads. This is the final experiment that we used in our calculations, found in the **Experiment** Package. Everything pertaining to running the experiment is in [Experiment.java](src/Experiment/Experiment.java). The other class is just an enum that contains the generator for each BiglyInt type.

## Development Notes
Experiment uses Java 17, run on IntelliJ 2021