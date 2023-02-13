# Huffman algorithm
Huffman's algorithm is a statistical data compression method. A text (or binary) file is
represented by a series of bytes (8-bit sequence). The principle of the algorithm is to transform each of these
bytes into a sequence of bits whose length is variable. The length of the new sequence which represents a
specific byte is calculated based on its frequency in the file. The algorithm will ensure that the bytes
rarer ones will be encoded by a longer sequence (and possibly more than 8 bits) while the more
frequent ones will be coded by a shorter sequence and less than 8 bits. It has been shown that the gains obtained by
the more frequent bytes outweigh the losses caused by the longer codes of the less frequent elements.
The net result is therefore a file that is smaller than its original size.

## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
