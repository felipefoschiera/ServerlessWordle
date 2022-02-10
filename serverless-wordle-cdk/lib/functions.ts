import { Code, Function, Runtime } from "aws-cdk-lib/aws-lambda";
import { S3EventSource } from "aws-cdk-lib/aws-lambda-event-sources";
import { RetentionDays } from "aws-cdk-lib/aws-logs";
import { EventType } from "aws-cdk-lib/aws-s3";
import { Construct } from "constructs";
import { WordleBuckets, WordleFunctions } from './props';

export interface FunctionProps {
    readonly buckets: WordleBuckets;
}

export function createFunctions(scope: Construct, props: FunctionProps): WordleFunctions {
    return {
        gameGenerator: createGameGenerator(scope),
        gameStarter: createGameStarter(scope),
        wordGuesser: createWordGuesser(scope),
        wordUploader: createWordUploader(scope, props),
    };
}

function createGameGenerator(scope: Construct): Function {
    return new Function(scope, 'GameGenerator', {
        functionName: 'GameGenerator',
        description: 'Lambda function triggered daily to generate a new game',
        runtime: Runtime.JAVA_11,
        code: Code.fromAsset('resources/assets/java-lambda.zip'),
        handler: 'com.serverlesswordle.handler.GameGeneratorHandler::handleRequest',
        memorySize: 256,
        logRetention: RetentionDays.ONE_WEEK,
    });
}

function createGameStarter(scope: Construct): Function {
    return new Function(scope, 'GameStarter', {
        functionName: 'GameStarter',
        description: 'Lambda function to start a new game for the user',
        runtime: Runtime.PYTHON_3_9,
        code: Code.fromAsset('resources/src/handler'),
        handler: 'game_starter_handler.handler',
        environment: {
            GAME_TABLE_NAME: "Game",
            WORD_TABLE_NAME: "Word"
        }
    });
}

function createWordGuesser(scope: Construct): Function {
    return new Function(scope, 'WordGuesser', {
        functionName: 'WordGuesser',
        description: 'Lambda function to process a word guess',
        runtime: Runtime.PYTHON_3_9,
        code: Code.fromAsset('resources/src/handler'),
        handler: 'word_guesser_handler.handler',
    });
}

function createWordUploader(scope: Construct, { buckets }: FunctionProps): Function {
    const wordUploader = new Function(scope, 'WordUploader', {
        functionName: 'WordUploader',
        description: 'Lambda function to upload words to the Word table',
        runtime: Runtime.PYTHON_3_9,
        code: Code.fromAsset('resources/src/handler'),
        handler: 'word_uploader_handler.handler',
    });

    const s3PutEventSource = new S3EventSource(buckets.wordList, {
        events: [
            EventType.OBJECT_CREATED_PUT
        ]
    });

    wordUploader.addEventSource(s3PutEventSource);

    return wordUploader;
}