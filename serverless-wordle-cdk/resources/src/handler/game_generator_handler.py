import logging
import random
import time
import boto3
from boto3.dynamodb.conditions import Key

logger = logging.getLogger()
logger.setLevel(logging.INFO)


def create_game(random_word: str, dynamodb: boto3.resource) -> None:
    game_table = dynamodb.Table('Game')
    timestamp = int(time.time())

    new_game = {
        'id': str(game_table.item_count),
        'word': random_word,
        'timestamp': timestamp
    }

    game_table.put_item(Item=new_game)

    logging.info(f'New game: {new_game}')


def error_message(message: str) -> str:
    return {
        'statusCode': 500,
        'message': message
    }


def get_random_word(dynamodb: boto3.resource) -> str:
    word_table = dynamodb.Table('Word')
    logging.info(f"Found {word_table.item_count} words in the Word table.")

    if word_table.item_count == 0:
        raise IndexError('Word item count is zero')

    random_index = random.randrange(word_table.item_count)
    logging.info(f"Random index: {random_index}")

    response = word_table.query(
        KeyConditionExpression=Key('id').eq(str(random_index))
    )

    if len(response['Items']) == 0:
        raise KeyError(
            f'Failed to retrieve a random item with index {random_index}'
        )

    random_item = response['Items'][0]
    random_word = random_item['word']
    logging.info(f"Random word: {random_word}")

    return random_word


def handler(event, context, dynamodb=None):
    if not dynamodb:
        dynamodb = boto3.resource('dynamodb')

    logging.info(f"Function triggered to generate a game.")

    try:
        random_word = get_random_word(dynamodb)
    except (KeyError, IndexError) as ex:
        logging.error(ex)
        return {
            'statusCode': 500,
            'body': str(ex)
        }

    create_game(random_word, dynamodb)

    return {
        'statusCode': 200,
        'body': f'GameGeneratorHandler random word: {random_word}\n'
    }


if __name__ == '__main__':
    handler({}, {})
