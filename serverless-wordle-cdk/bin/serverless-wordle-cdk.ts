#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { ServerlessWordleCdkStack } from '../lib/serverless-wordle-cdk-stack';

const app = new cdk.App();
new ServerlessWordleCdkStack(app, 'ServerlessWordleCdkStack', {
  env: {
    account: process.env.CDK_DEFAULT_ACCOUNT,
    region: process.env.CDK_DEFAULT_REGION
  },
});