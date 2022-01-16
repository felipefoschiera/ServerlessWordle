import { Table } from "aws-cdk-lib/aws-dynamodb";
import { Rule } from "aws-cdk-lib/aws-events";
import { Function } from "aws-cdk-lib/aws-lambda";

export interface WordleStackResources {
    readonly tables: WordleTables;
    readonly functions: WordleFunctions;
    readonly rules: WordleRules;
}

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

export interface WordleRules {
    readonly gameGeneratorTrigger: Rule;
}