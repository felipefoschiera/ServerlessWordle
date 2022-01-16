import { Code, Function, Runtime } from "aws-cdk-lib/aws-lambda";
import { Construct } from "constructs";
import { WordleFunctions } from './props';

export function createFunctions(scope: Construct): WordleFunctions {
    return {
        gameGenerator: createGameGenerator(scope),
        gameStarter: createGameStarter(scope),
        wordGuesser: createWordGuesser(scope),
    };
}

function createGameGenerator(scope: Construct): Function {
    return new Function(scope, 'GameGenerator', {
        functionName: 'GameGenerator',
        description: 'Lambda function triggered daily to generate a new game',
        runtime: Runtime.PYTHON_3_9,
        code: Code.fromAsset("resources/src/handler"),
        handler: 'game_generator_handler.handler',
    });
}

function createGameStarter(scope: Construct): Function {
    return new Function(scope, 'GameStarter', {
        functionName: 'GameStarter',
        description: 'Lambda function to start a new game for the user',
        runtime: Runtime.PYTHON_3_9,
        code: Code.fromAsset("resources/src/handler"),
        handler: 'game_starter_handler.handler',
    });
}

function createWordGuesser(scope: Construct): Function {
    return new Function(scope, 'WordGuesser', {
        functionName: 'WordGuesser',
        description: 'Lambda function to process a word guess',
        runtime: Runtime.PYTHON_3_9,
        code: Code.fromAsset("resources/src/handler"),
        handler: 'word_guesser_handler.handler',
    });
}
