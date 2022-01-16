import { Stack, StackProps } from 'aws-cdk-lib';
import { Construct } from 'constructs';
import { WordleStackResources } from './props';
import { createTables } from './tables';
import { createFunctions } from './functions';

export class ServerlessWordleCdkStack extends Stack {
  public readonly resources: WordleStackResources;

  constructor(scope: Construct, id: string, props?: StackProps) {
    super(scope, id, props);

    const tables = createTables(this);
    const functions = createFunctions(this);

    this.resources = {
      tables,
      functions,
    };
  }
}
