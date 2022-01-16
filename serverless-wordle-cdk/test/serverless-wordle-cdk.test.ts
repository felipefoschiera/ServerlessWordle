import * as cdk from 'aws-cdk-lib';
import { Template } from 'aws-cdk-lib/assertions';
import * as ServerlessWordleCdk from '../lib/serverless-wordle-cdk-stack';

test('DynamoDB Tables Created', () => {
    const app = new cdk.App();
    // WHEN
    const stack = new ServerlessWordleCdk.ServerlessWordleCdkStack(app, 'MyTestStack');
    // THEN
    const template = Template.fromStack(stack);

    template.hasResourceProperties('AWS::DynamoDB::Table', {
        TableName: "Game"
    });

    template.hasResourceProperties('AWS::DynamoDB::Table', {
        TableName: "Word"
    });

    template.hasResourceProperties('AWS::DynamoDB::Table', {
        TableName: "WordGuess"
    });
});

test('Lambda Functions Created', () => {
    const app = new cdk.App();
    // WHEN
    const stack = new ServerlessWordleCdk.ServerlessWordleCdkStack(app, 'MyTestStack');
    // THEN
    const template = Template.fromStack(stack);

    template.hasResourceProperties('AWS::Lambda::Function', {
        FunctionName: 'GameGenerator'
    });

    template.hasResourceProperties('AWS::Lambda::Function', {
        FunctionName: 'GameStarter'
    });

    template.hasResourceProperties('AWS::Lambda::Function', {
        FunctionName: 'WordGuesser'
    });
})

test('Event Rules Created', () => {
    const app = new cdk.App();
    // WHEN
    const stack = new ServerlessWordleCdk.ServerlessWordleCdkStack(app, 'MyTestStack');
    // THEN
    const template = Template.fromStack(stack);

    template.hasResourceProperties('AWS::Events::Rule', {
        Name: 'GameGeneratorTriggerRule'
    });

})