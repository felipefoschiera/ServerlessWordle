import { Table } from "aws-cdk-lib/aws-dynamodb";

export interface WordleTables {
    readonly game: Table;
    readonly word: Table;
    readonly wordGuess: Table;
}