import { RemovalPolicy } from "aws-cdk-lib";
import { Bucket } from "aws-cdk-lib/aws-s3";
import { Construct } from "constructs";
import { WordleBuckets } from "./props";

export function createBuckets(scope: Construct): WordleBuckets {
    return {
        wordList: createWordList(scope),
    };
}

function createWordList(scope: Construct): Bucket {
    return new Bucket(scope, 'WordList', {
        bucketName: 'wordlegame-word-list',
        autoDeleteObjects: true,
        removalPolicy: RemovalPolicy.DESTROY,
    });
}