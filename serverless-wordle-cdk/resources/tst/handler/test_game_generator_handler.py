import unittest
from src.handler import game_generator_handler
from moto import mock_dynamodb2
import boto3


@mock_dynamodb2
class TestGameGenerator(unittest.TestCase):

    def setUp(self):
        """
        Create database resources and mock tables
        """
        self.TEST_WORD = 'bread'
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
                }
            ]
        )

        self.word_table.put_item(Item={
            'id': '0',
            'word': self.TEST_WORD
        })

    def tearDown(self):
        """
        Delete database resources and mock tables
        """
        self.game_table.delete()
        self.word_table.delete()
        self.dynamodb = None

    def test_get_random_word(self):
        random_word = game_generator_handler.get_random_word(self.dynamodb)

        self.assertEqual(self.TEST_WORD, random_word)

    def test_create_game(self):
        expected_game = {
            'date': unittest.mock.ANY,
            'timestamp': unittest.mock.ANY,
            'word': self.TEST_WORD
        }
        new_game = game_generator_handler.create_game(self.TEST_WORD, self.dynamodb)

        self.assertEqual(new_game, expected_game)

    def test_handler(self):
        
        result = game_generator_handler.handler({}, None)
        
        self.assertEqual(result['statusCode'], 200)


if __name__ == '__main__':
    unittest.main()
