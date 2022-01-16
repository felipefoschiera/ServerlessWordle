import { Stack, StackProps } from 'aws-cdk-lib';
import { Construct } from 'constructs';
import { WordleStackResources } from './props';
import { createTables } from './tables';
import { createFunctions } from './functions';
import { createRules } from './rules';

export class ServerlessWordleCdkStack extends Stack {
  public readonly resources: WordleStackResources;

  constructor(scope: Construct, id: string, props?: StackProps) {
    super(scope, id, props);

    const functions = createFunctions(this);
    const tables = createTables(this, { functions });
    const rules = createRules(this, { functions });

    this.resources = {
      tables,
      functions,
      rules,
    };
  }
}
