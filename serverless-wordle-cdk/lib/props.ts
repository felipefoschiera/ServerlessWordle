import { Table } from "aws-cdk-lib/aws-dynamodb";
import { Function } from "aws-cdk-lib/aws-lambda";

export interface WordleTables {
    readonly game: Table;
    readonly word: Table;
    readonly wordGuess: Table;
}

export interface WordleFunctions {
    readonly gameGenerator: Function;
    readonly gameStarter: Function;
    readonly wordGuesser: Function;
}