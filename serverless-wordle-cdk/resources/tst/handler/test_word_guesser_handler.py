import unittest
from src.handler import word_guesser_handler
from src.handler.word_guesser_handler import LetterGuessStatus, MAX_TRIES
from moto import mock_dynamodb2
from datetime import date
import boto3
import time


@mock_dynamodb2
class TestWordGuesser(unittest.TestCase):

    def setUp(self):
        """
        Create database resources and mock tables
        """

        self.VALID_WORD = 'other'
        self.INVALID_WORD = 'abcde'
        self.DATE = date.today().strftime('%d-%m-%Y')
        self.TIMESTAMP = int(time.time())
        self.USER_ID = 'test-user'

        self.dynamodb = boto3.resource('dynamodb', region_name='us-east-1')

        self.game_table = self.dynamodb.create_table(
            TableName='Game',
            KeySchema=[
                {
                    'AttributeName': 'date',
                    'KeyType': 'HASH'
                },
                {
                    'AttributeName': 'timestamp',
                    'KeyType': 'RANGE'
                }
            ],
            AttributeDefinitions=[
                {
                    'AttributeName': 'date',
                    'AttributeType': 'S'
                },
                {
                    'AttributeName': 'timestamp',
                    'AttributeType': 'N'
                }
            ]
        )

        self.word_table = self.dynamodb.create_table(
            TableName='Word',
            KeySchema=[
                {
                    'AttributeName': 'id',
                    'KeyType': 'HASH'
                }
            ],
            AttributeDefinitions=[
                {
                    'AttributeName': 'id',
                    'AttributeType': 'S'
                },
                {
                    'AttributeName': 'word',
                    'AttributeType': 'S'
                }
            ],
            GlobalSecondaryIndexes=[
                {
                    'IndexName': 'QueryByWord',
                    'KeySchema': [
                        {
                            'AttributeName': 'word',
                            'KeyType': 'HASH'
                        },
                    ],
                    'Projection': {
                        'ProjectionType': 'ALL',
                    }
                }
            ]
        )

        self.word_table.put_item(Item={
            'id': '0',
            'word': self.VALID_WORD
        })

        self.word_guess = self.dynamodb.create_table(
            TableName='WordGuess',
            KeySchema=[
                {
                    'AttributeName': 'game_userid',
                    'KeyType': 'HASH'
                },
                {
                    'AttributeName': 'guess_number',
                    'KeyType': 'RANGE'
                }
            ],
            AttributeDefinitions=[
                {
                    'AttributeName': 'game_userid',
                    'AttributeType': 'S'
                },
                {
                    'AttributeName': 'guess_number',
                    'AttributeType': 'N'
                }
            ]
        )

        self.word_table.put_item(Item={
            'id': '0',
            'word': self.VALID_WORD
        })

        self.GAME = {
            'date': self.DATE,
            'timestamp': self.TIMESTAMP,
            'word': self.VALID_WORD
        }

        self.game_table.put_item(Item=self.GAME)

    def tearDown(self):
        """
        Delete database resources and mock tables
        """
        self.game_table.delete()
        self.word_table.delete()
        self.word_guess.delete()
        self.dynamodb = None

    def test_check_valid_word(self):
        word_is_valid = word_guesser_handler.check_valid_word(
            self.VALID_WORD, self.dynamodb
        )
        self.assertTrue(word_is_valid)

    def test_check_invalid_word(self):
        word_is_valid = word_guesser_handler.check_valid_word(
            self.INVALID_WORD, self.dynamodb
        )

        self.assertFalse(word_is_valid)

    def test_get_today_game(self):
        expected_game = {
            'date': self.DATE,
            'timestamp': self.TIMESTAMP,
            'word': self.VALID_WORD
        }

        game = word_guesser_handler.get_today_game(self.dynamodb)

        self.assertEqual(game, expected_game)

    def test_get_guess_history(self):

        word_guesser_handler.store_guess_history(
            self.VALID_WORD, self.GAME, self.USER_ID, 0, self.dynamodb)
        word_guesser_handler.store_guess_history(
            self.VALID_WORD, self.GAME, self.USER_ID, 1, self.dynamodb)

        expected_tries = 2

        tries = len(word_guesser_handler.get_guess_history(
            self.GAME, self.USER_ID, self.dynamodb))
        self.assertEqual(tries, expected_tries)

    def test_store_guess_history(self):
        expected_object = {
            'game_userid': f'{self.DATE}_{self.USER_ID}',
            'guess_number': 0,
            'word': self.VALID_WORD,
            'timestamp': unittest.mock.ANY
        }

        guess_history = word_guesser_handler.store_guess_history(
            self.VALID_WORD, self.GAME, self.USER_ID, 0, self.dynamodb
        )

        self.assertEqual(guess_history, expected_object)

        expected_object['guess_number'] = 1
        guess_history = word_guesser_handler.store_guess_history(
            self.VALID_WORD, self.GAME, self.USER_ID, 1, self.dynamodb
        )

        self.assertEqual(guess_history, expected_object)

    def test_compare_guess(self):
        test_word = 'oateh'
        expected_word_results = [
            {'letter': 'o', 'status': LetterGuessStatus.CORRECT.value},
            {'letter': 'a', 'status': LetterGuessStatus.MISS.value},
            {'letter': 't', 'status': LetterGuessStatus.PRESENT.value},
            {'letter': 'e', 'status': LetterGuessStatus.CORRECT.value},
            {'letter': 'h', 'status': LetterGuessStatus.PRESENT.value},
        ]

        expected_result = {
            'results': [
                {
                    'guess_number': 0,
                    'word': test_word,
                    'results': expected_word_results
                }
            ]
        }

        test_guess = {
            'word': test_word,
            'guess_number': 0
        }

        result = word_guesser_handler.compare_guesses_to_word(
            [test_guess], self.GAME
        )
        self.maxDiff = None
        self.assertEqual(expected_result, result)

    def test_compare_guess_repeated_letter(self):
        test_word = 'ohteh'
        expected_word_results = [
            {'letter': 'o', 'status': LetterGuessStatus.CORRECT.value},
            {'letter': 'h', 'status': LetterGuessStatus.PRESENT.value},
            {'letter': 't', 'status': LetterGuessStatus.PRESENT.value},
            {'letter': 'e', 'status': LetterGuessStatus.CORRECT.value},
            {'letter': 'h', 'status': LetterGuessStatus.MISS.value},
        ]

        expected_result = {
            'results': [
                {
                    'guess_number': 0,
                    'word': test_word,
                    'results': expected_word_results
                }
            ]
        }

        test_guess = {
            'word': test_word,
            'guess_number': 0
        }

        result = word_guesser_handler.compare_guesses_to_word(
            [test_guess], self.GAME
        )

        self.assertEqual(expected_result, result)

    def test_handler(self):
        result_correct = word_guesser_handler.handler(
            {'word': 'other', 'user_id': 'test-user'}, None
        )
        self.assertEqual(result_correct['statusCode'], 200)

        result_no_values = word_guesser_handler.handler(
            {'word': '', 'user-id': ''}, None
        )
        self.assertEqual(result_no_values['statusCode'], 400)

        result_no_word = word_guesser_handler.handler(
            {'word': '', 'user-id': 'test-user'}, None
        )
        self.assertEqual(result_no_word['statusCode'], 400)

        result_no_user = word_guesser_handler.handler(
            {'word': 'other', 'user-id': ''}, None
        )
        self.assertEqual(result_no_user['statusCode'], 400)

        result_no_event = word_guesser_handler.handler({}, None)
        self.assertEqual(result_no_event['statusCode'], 400)

    def test_game_ends_after_max_tries(self):
        wrong_word = 'ohteh'

        for i in range(MAX_TRIES):
            result = word_guesser_handler.handler(
                {'word': wrong_word, 'user_id': 'test-user'}, None
            )

        self.assertEqual(result['game_ended'], True)

    def test_game_ends_after_right_guess(self):
        wrong_word = 'ohteh'
        right_word = self.VALID_WORD

        word_guesser_handler.handler(
            {'word': wrong_word, 'user_id': 'test-user'}, None
        )

        result = word_guesser_handler.handler(
            {'word': right_word, 'user_id': 'test-user'}, None
        )

        self.assertEqual(result['game_ended'], True)
