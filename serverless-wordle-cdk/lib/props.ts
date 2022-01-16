import { Table } from "aws-cdk-lib/aws-dynamodb";
import { Rule } from "aws-cdk-lib/aws-events";
import { Function } from "aws-cdk-lib/aws-lambda";
import { Bucket } from "aws-cdk-lib/aws-s3";

export interface WordleStackResources {
    readonly buckets: WordleBuckets;
    readonly functions: WordleFunctions;
    readonly tables: WordleTables;
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
    readonly wordUploader: Function;
}

export interface WordleBuckets {
    readonly wordList: Bucket;
}

export interface WordleRules {
    readonly gameGeneratorTrigger: Rule;
}