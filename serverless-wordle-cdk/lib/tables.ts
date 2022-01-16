import { AttributeType, BillingMode, ProjectionType, Table } from "aws-cdk-lib/aws-dynamodb";
import { Construct } from "constructs";
import { WordleTables } from "./props";

export function createTables(scope: Construct): WordleTables {
    return {
        game: createGame(scope),
        word: createWord(scope),
        wordGuess: createWordGuess(scope),
    };
}

function createGame(scope: Construct): Table {
    return new Table(scope, 'Game', {
        tableName: 'Game',
        partitionKey: { name: 'id', type: AttributeType.STRING },
        billingMode: BillingMode.PAY_PER_REQUEST,
    });
}

function createWord(scope: Construct): Table {
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

    return table;
}

function createWordGuess(scope: Construct): Table {
    return new Table(scope, 'WordGuess', {
        tableName: 'WordGuess',
        partitionKey: {
            name: 'game',
            type: AttributeType.STRING,
        },
        sortKey: {
            name: 'user_id',
            type: AttributeType.STRING
        },
        billingMode: BillingMode.PAY_PER_REQUEST,
    });
}