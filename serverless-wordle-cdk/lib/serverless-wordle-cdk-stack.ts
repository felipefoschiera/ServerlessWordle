import { Stack, StackProps } from 'aws-cdk-lib';
import { Construct } from 'constructs';
import { createTables } from './tables';

export class ServerlessWordleCdkStack extends Stack {
  constructor(scope: Construct, id: string, props?: StackProps) {
    super(scope, id, props);

    createTables(this);

  }
}
