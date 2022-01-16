import { Stack, StackProps } from 'aws-cdk-lib';
import { Construct } from 'constructs';
import { WordleStackResources } from './props';
import { createBuckets } from './buckets';
import { createFunctions } from './functions';
import { createTables } from './tables';
import { createRules } from './rules';

export class ServerlessWordleCdkStack extends Stack {
  public readonly resources: WordleStackResources;

  constructor(scope: Construct, id: string, props?: StackProps) {
    super(scope, id, props);

    const buckets = createBuckets(this);
    const functions = createFunctions(this, { buckets });
    const tables = createTables(this, { functions });
    const rules = createRules(this, { functions });

    this.resources = {
      buckets,
      tables,
      functions,
      rules,
    };
  }
}
