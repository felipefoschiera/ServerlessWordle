import { AttributeType, BillingMode, ProjectionType, Table } from "aws-cdk-lib/aws-dynamodb";
import { Construct } from "constructs";
import { WordleFunctions, WordleTables } from "./props";

export interface TableProps {
    readonly functions: WordleFunctions;
}

export function createTables(scope: Construct, props: TableProps): WordleTables {
    return {
        game: createGame(scope, props),
        word: createWord(scope, props),
        wordGuess: createWordGuess(scope, props),
    };
}

function createGame(scope: Construct, { functions }: TableProps): Table {
    const table = new Table(scope, 'Game', {
        tableName: 'Game',
        partitionKey: { name: 'date', type: AttributeType.STRING },
        sortKey: { name: 'timestamp', type: AttributeType.NUMBER },
        billingMode: BillingMode.PAY_PER_REQUEST,
    });

    table.grantWriteData(functions.gameGenerator);
    table.grant(functions.gameGenerator, 'dynamodb:DescribeTable')
    table.grantReadData(functions.gameStarter);
    table.grantReadData(functions.wordGuesser);

    return table;
}

function createWord(scope: Construct, { functions }: TableProps): Table {
    const table = new Table(scope, 'Word', {
        tableName: 'Word',
        partitionKey: { name: 'id', type: AttributeType.STRING },
        billingMode: BillingMode.PAY_PER_REQUEST,
    });

    table.addGlobalSecondaryIndex({
        indexName: 'QueryByWord',
        partitionKey: { name: 'word', type: AttributeType.STRING },
        projectionType: ProjectionType.ALL,
    });

    table.grantReadData(functions.gameGenerator);
    table.grant(functions.gameGenerator, 'dynamodb:DescribeTable')
    table.grantReadData(functions.wordGuesser);

    return table;
}

function createWordGuess(scope: Construct, { functions }: TableProps): Table {
    const table = new Table(scope, 'WordGuess', {
        tableName: 'WordGuess',
        partitionKey: {
            name: 'game_userid',
            type: AttributeType.STRING,
        },
        sortKey: {
            name: 'guess_number',
            type: AttributeType.NUMBER
        },
        billingMode: BillingMode.PAY_PER_REQUEST,
    });

    table.grantReadData(functions.gameStarter);
    table.grantReadWriteData(functions.wordGuesser);

    return table;
}