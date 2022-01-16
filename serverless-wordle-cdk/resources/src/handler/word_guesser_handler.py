import logging
import boto3
import time
from enum import Enum
from datetime import date
from boto3.dynamodb.conditions import Key

logger = logging.getLogger()
logger.setLevel(logging.INFO)

MAX_TRIES = 6


class LetterGuessStatus(Enum):
    CORRECT = 'correct'
    PRESENT = 'present'
    MISS = 'miss'


def check_valid_word(word: str, dynamodb: boto3.resource) -> bool:
    word_table = dynamodb.Table('Word')

    response = word_table.query(
        IndexName='QueryByWord',
        KeyConditionExpression=Key('word').eq(word),
    )

    return len(response['Items']) > 0


def get_today_game(dynamodb: boto3.resource) -> dict:
    game_table = dynamodb.Table('Game')

    today = date.today().strftime('%d-%m-%Y')
    response = game_table.query(
        Limit=1,
        ScanIndexForward=False,
        KeyConditionExpression=Key('date').eq(today)
    )

    if len(response['Items']) == 0:
        raise KeyError(f'Failed to retrieve latest game for date {today}')

    game = response['Items'][0]
    logging.info(f'Retrieved game {game}')
    return game


def get_guess_history(game: dict, user_id: str, dynamodb: boto3.resource) -> dict:
    word_guess_table = dynamodb.Table('WordGuess')

    game_date = game['date']
    composite_key = f'{game_date}_{user_id}'

    results = word_guess_table.query(
        KeyConditionExpression=Key('game_userid').eq(composite_key)
    )

    return results['Items']


def store_guess_history(word: str, game: dict, user_id: str, guess_tries: int, dynamodb: boto3.resource) -> dict:
    word_guess_table = dynamodb.Table('WordGuess')

    game_date = game['date']
    composite_key = f'{game_date}_{user_id}'
    timestamp = int(time.time())

    guess_object = {
        'game_userid': composite_key,
        'guess_number': guess_tries,
        'word': word,
        'timestamp': timestamp
    }

    word_guess_table.put_item(Item=guess_object)
    logging.info(f'New guess: {guess_object}')
    return guess_object


def check_guess_correct(guess: dict, game: dict) -> bool:
    return guess['word'] == game['word']


def compare_guesses_to_word(guesses: list, game: dict) -> dict:
    game_word = game['word']
    logging.info(f'Comparing guesses {guesses} to game word {game_word}')

    result_guesses = []

    for guess in guesses:
        word = guess['word']

        guess_object = {
            'guess_number': guess['guess_number'],
            'word': word,
        }

        letter_counter = {}
        for letter in game_word:
            if letter not in letter_counter:
                letter_counter[letter] = 1
            else:
                letter_counter[letter] += 1

        word_results = []

        for i in range(len(word)):
            status = None
            if word[i] == game_word[i]:
                status = LetterGuessStatus.CORRECT.value
                letter_counter[word[i]] -= 1
            else:
                if word[i] in game_word and letter_counter[word[i]] > 0:
                    status = LetterGuessStatus.PRESENT.value
                    letter_counter[word[i]] -= 1
                else:
                    status = LetterGuessStatus.MISS.value

            word_results.append({
                'letter': word[i],
                'status': status
            })

        guess_object['results'] = word_results
        result_guesses.append(guess_object)

    return {'results': result_guesses}


def validate_event(event: dict) -> None:
    if 'word' not in event or not event['word']:
        message = f'No word was provided for the guess'
        raise ValueError(message)

    if 'user_id' not in event or not event['user_id']:
        message = f'No user id was provided for the guess'
        raise ValueError(message)


def handler(event, context, dynamodb=None):
    if not dynamodb:
        dynamodb = boto3.resource('dynamodb')

    logging.info(f'Function triggered for a word guess')

    try:
        validate_event(event)
    except ValueError as ex:
        logging.error(ex)
        return {
            'statusCode': 400,
            'game_ended': False,
            'body': str(ex)
        }

    word_guessed = event['word']
    user_id = event['user_id']
    valid_word = check_valid_word(word_guessed, dynamodb)

    try:
        game = get_today_game(dynamodb)
    except KeyError as ex:
        logging.error(ex)
        return {
            'statusCode': 500,
            'game_ended': False,
            'body': str(ex)
        }

    guess_history = get_guess_history(game, user_id, dynamodb)

    guess_correct = False
    if len(guess_history) > 0:
        guess_correct = check_guess_correct(guess_history[-1], game)

    if guess_correct or len(guess_history) >= MAX_TRIES:
        return {
            'statusCode': 200,
            'game_ended': True,
            'body': compare_guesses_to_word(guess_history, game)
        }

    new_guess = store_guess_history(
        word_guessed, game, user_id, len(guess_history), dynamodb
    )

    guess_history.append(new_guess)

    result = compare_guesses_to_word(guess_history, game)
    logging.info(f'Guesses comparison: {result}')

    guess_correct = check_guess_correct(new_guess, game)
    game_ends = len(guess_history) >= MAX_TRIES or guess_correct

    return {
        'statusCode': 200,
        'game_ended': game_ends,
        'body': result
    }


if __name__ == '__main__':
    handler({'word': 'other', 'user_id': 'test-id'}, {})
