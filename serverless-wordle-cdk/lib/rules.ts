import { Rule, Schedule } from "aws-cdk-lib/aws-events";
import { LambdaFunction } from "aws-cdk-lib/aws-events-targets";
import { Construct } from "constructs";
import { WordleFunctions, WordleRules } from "./props";

export interface RuleProps {
    readonly functions: WordleFunctions;
};

export function createRules(scope: Construct, props: RuleProps): WordleRules {
    return {
        gameGeneratorTrigger: createGameGeneratorTriggerRule(scope, props),
    };
}

function createGameGeneratorTriggerRule(scope: Construct, { functions }: RuleProps): Rule {
    const target = functions.gameGenerator;
    const eventRule = new Rule(scope, 'GameGeneratorTriggerRule', {
        ruleName: 'GameGeneratorTriggerRule',
        description: 'Event rule to generate a new game on a schedule',
        schedule: Schedule.cron({ minute: '0', hour: '0' }),
    });

    eventRule.addTarget(new LambdaFunction(target, {
        retryAttempts: 3,
    }));

    return eventRule;
}