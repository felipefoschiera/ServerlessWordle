# Welcome to your CDK TypeScript project!

This is a blank project for TypeScript development with CDK.

The `cdk.json` file tells the CDK Toolkit how to execute your app.

## Useful commands

- `npm run build` compile typescript to js
- `npm run watch` watch for changes and compile
- `npm run test` perform the jest unit tests
- `cdk diff` compare deployed stack with current state
- `cdk synth` emits the synthesized CloudFormation template
- `cdk bootstrap aws://ACCOUNT_ID/REGION` bootstrap the AWS environment
- `cdk deploy` deploy this stack to your default AWS account/region

## How to build and deploy

- `gradlew packageFat` generates JAR file
- `cp build/distributions/java.zip ../assets/java-lambda.zip` copies the file to the assets dir
- `cdk synth`
